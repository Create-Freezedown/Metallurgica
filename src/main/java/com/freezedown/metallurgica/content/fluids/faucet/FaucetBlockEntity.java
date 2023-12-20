package com.freezedown.metallurgica.content.fluids.faucet;

import com.freezedown.metallurgica.foundation.util.WeakConsumerWrapper;
import com.freezedown.metallurgica.registry.MetallurgicaPackets;
import com.mojang.math.Vector3f;
import com.simibubi.create.Create;
import com.simibubi.create.content.fluids.FluidFX;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.VecHelper;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;

import java.util.List;

import static com.freezedown.metallurgica.content.fluids.faucet.FaucetBlock.FACING;

public class FaucetBlockEntity extends SmartBlockEntity {
    /** amount of MB to extract from the input at a time */
    public static final int PACKET_SIZE = 90;
    /** Transfer rate of the faucet */
    public static final int MB_PER_TICK = 10;
    
    public static final BlockEntityTicker<FaucetBlockEntity> SERVER_TICKER = (level, pos, world, self) -> self.tick();
    /** If true, faucet is currently pouring */
    private FaucetState faucetState = FaucetState.OFF;
    /** If true, redstone told this faucet to stop, so stop when ready */
    private boolean stopPouring = false;
    /** Current fluid in the faucet */
    private FluidStack drained = FluidStack.EMPTY;
    /** Fluid for rendering, used to reduce the number of packets. There is a brief moment where {@link this#drained} is empty but we should be rendering something */
    @Getter
    private FluidStack renderFluid = FluidStack.EMPTY;
    /** Used for pulse detection */
    private boolean lastRedstoneState = false;
    
    /** Fluid handler of the input to the faucet */
    private LazyOptional<IFluidHandler> inputHandler;
    /** Fluid handler of the output from the faucet */
    private LazyOptional<IFluidHandler> outputHandler;
    /** Listener for when the input handler is invalidated */
    private final NonNullConsumer<LazyOptional<IFluidHandler>> inputListener = new WeakConsumerWrapper<>(this, (self, handler) -> this.inputHandler = null);
    /** Listener for when the output handler is invalidated */
    private final NonNullConsumer<LazyOptional<IFluidHandler>> outputListener = new WeakConsumerWrapper<>(this, (self, handler) -> this.outputHandler = null);
    
    public FaucetBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    
    }
    
    /* Fluid handler */
    
    /**
     * Finds the fluid handler on the given side
     * @param side  Side to check
     * @return  Fluid handler
     */
    private LazyOptional<IFluidHandler> findFluidHandler(Direction side) {
        assert level != null;
        BlockEntity te = level.getBlockEntity(worldPosition.relative(side));
        if (te != null) {
            LazyOptional<IFluidHandler> handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
            if (handler.isPresent()) {
                return handler;
            }
        }
        return LazyOptional.empty();
    }
    
    /**
     * Gets the input fluid handler
     * @return  Input fluid handler
     */
    private LazyOptional<IFluidHandler> getInputHandler() {
        if (inputHandler == null) {
            inputHandler = findFluidHandler(getBlockState().getValue(FACING).getOpposite());
            if (inputHandler.isPresent()) {
                inputHandler.addListener(inputListener);
            }
        }
        return inputHandler;
    }
    
    /**
     * Gets the output fluid handler
     * @return  Output fluid handler
     */
    private LazyOptional<IFluidHandler> getOutputHandler() {
        if (outputHandler == null) {
            outputHandler = findFluidHandler(Direction.DOWN);
            if (outputHandler.isPresent()) {
                outputHandler.addListener(outputListener);
            }
        }
        return outputHandler;
    }
    
    /**
     * Called when a neighbor changes to invalidate the cached fluid handler
     * @param neighbor  Neighbor position that changed
     */
    public void neighborChanged(BlockPos neighbor) {
        // if the neighbor was below us, remove output
        if (worldPosition.equals(neighbor.above())) {
            outputHandler = null;
            // neighbor behind us
        } else if (worldPosition.equals(neighbor.relative(getBlockState().getValue(FACING)))) {
            inputHandler = null;
        }
    }
    
    
    /* Data */
    
    /**
     * Gets whether the faucet is pouring
     * @return True if pouring
     */
    public boolean isPouring() {
        return faucetState != FaucetState.OFF;
    }
    
    /* Activation */
    
    /**
     * Toggles pouring state and initiates transfer if appropriate. Called on right click and from redstone
     */
    public void activate() {
        // don't run on client
        if (level == null || level.isClientSide) {
            return;
        }
        // already pouring? we want to start
        switch (faucetState) {
            // off activates the faucet
            case OFF -> {
                stopPouring = false;
                doTransfer(true);
            }
            // powered deactivates the faucet, sync to client
            case POWERED -> {
                faucetState = FaucetState.OFF;
                syncToClient(FluidStack.EMPTY, false);
            }
            // pouring means we stop pouring as soon as possible
            case POURING -> stopPouring = true;
        }
    }
    
    /**
     * Flips hasSignal and schedules a tick if appropriate.
     * @param hasSignal  New signal state
     */
    public void handleRedstone(boolean hasSignal) {
        if (hasSignal != lastRedstoneState) {
            lastRedstoneState = hasSignal;
            if (hasSignal) {
                if (level != null){
                    level.scheduleTick(worldPosition, this.getBlockState().getBlock(), 2);
                }
            } else if (faucetState == FaucetState.POWERED) {
                faucetState = FaucetState.OFF;
                syncToClient(FluidStack.EMPTY, false);
            }
        }
    }
    private void createFluidParticles() {
        RandomSource r = level.random;
        if (!renderFluid.isEmpty() && isPouring()) {
            createOutputFluidParticles(r);
        }
    }
    private Vec3 spoutputOutVec(Direction direction, Vec3 directionVec) {
        return switch (direction) {
            case SOUTH -> VecHelper.getCenterOf(worldPosition).add(0, 1 / 16f, -2/16f);
            case EAST -> VecHelper.getCenterOf(worldPosition).add(-2/16f, 1 / 16f, 0);
            case NORTH -> VecHelper.getCenterOf(worldPosition).add(0, 1 / 16f, 2/16f);
            case WEST -> VecHelper.getCenterOf(worldPosition).add(2/16f, 1 / 16f, 0);
            case DOWN -> VecHelper.getCenterOf(worldPosition).add(directionVec.add(0, 15 / 16f, 0));
            default -> VecHelper.getCenterOf(worldPosition).add(directionVec.add(0, 1 / 16f, 0));
        };
    }
    private void createOutputFluidParticles(RandomSource r) {
        BlockState blockState = getBlockState();
        if (!(blockState.getBlock() instanceof FaucetBlock))
            return;
        Direction direction = blockState.getValue(FaucetBlock.FACING);
        Direction.Axis axis = direction.getAxis();
        Vec3 directionVec = Vec3.atLowerCornerOf(direction.getNormal());
        Vec3 outVec = spoutputOutVec(direction, directionVec);
        Vec3 outMotion = directionVec.scale(1 / 16f).add(0, -1 / 16f, 0);
        
        for (int i = 0; i < 2; i++) {
            FluidStack fluidStack = renderFluid;
            ParticleOptions fluidParticle = FluidFX.getFluidParticle(fluidStack);
            Vec3 m = VecHelper.offsetRandomly(outMotion, r, 1 / 16f);
            level.addAlwaysVisibleParticle(fluidParticle, outVec.x, outVec.y, outVec.z, m.x, m.y, m.z);
        }
    }
    private static final Vector3f RED = new Vector3f(1.0F, 0.0F, 0.0F);
    /**
     * Adds particles to the faucet
     * @param state    Faucet state
     * @param worldIn  World instance
     * @param pos      Faucet position
     */
    private static void addParticles(BlockState state, LevelAccessor worldIn, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        double x = (double)pos.getX() + 0.5D - 0.3D * (double)direction.getStepX();
        double y = (double)pos.getY() + 0.5D - 0.3D * (double)direction.getStepY();
        double z = (double)pos.getZ() + 0.5D - 0.3D * (double)direction.getStepZ();
        worldIn.addParticle(new DustParticleOptions(RED, 0.5f), x, y, z, 0.0D, 0.0D, 0.0D);
    }
    
    public void createRedstoneParticles() {
        if (this.isPouring() && this.renderFluid.isEmpty() && Create.RANDOM.nextFloat() < 0.25F) {
            addParticles(this.getBlockState(), level, worldPosition);
        }
    }
    /* Pouring */
    
    /** Handles server ticks */
    public void tick() {
        super.tick();
        // nothing to do if not pouring
        if (faucetState == FaucetState.OFF) {
            return;
            // if powered and we can transfer, schedule transfer for next tick
        } else if (faucetState == FaucetState.POWERED && doTransfer(false)) {
            faucetState = FaucetState.POURING;
            return;
        }
        if (level == null) {
            return;
        }
        if (level.isClientSide) {
            createRedstoneParticles();
            createFluidParticles();
        }
        // continue current stack
        if (!drained.isEmpty()) {
            pour();
            // stop if told to stop once done
        } else if (stopPouring) {
            reset();
            // otherwise keep going
        } else {
            doTransfer(true);
        }
    }
    /**
     * Initiate fluid transfer
     */
    private boolean doTransfer(boolean execute) {
        // still got content left
        LazyOptional<IFluidHandler> inputOptional = getInputHandler();
        LazyOptional<IFluidHandler> outputOptional = getOutputHandler();
        if (inputOptional.isPresent() && outputOptional.isPresent()) {
            // can we drain?
            IFluidHandler input = inputOptional.orElse(EmptyFluidHandler.INSTANCE);
            FluidStack drained = input.drain(PACKET_SIZE, IFluidHandler.FluidAction.SIMULATE);
            if (!drained.isEmpty() && !(drained.getFluid().getFluidType().getDensity() < 0)) {
                // can we fill
                IFluidHandler output = outputOptional.orElse(EmptyFluidHandler.INSTANCE);
                int filled = output.fill(drained, IFluidHandler.FluidAction.SIMULATE);
                if (filled > 0) {
                    // fill if requested
                    if (execute) {
                        // drain the liquid and transfer it, buffer the amount for delay
                        this.drained = input.drain(filled, IFluidHandler.FluidAction.EXECUTE);
                        
                        // sync to clients if we have changes
                        if (faucetState == FaucetState.OFF || !renderFluid.isFluidEqual(drained)) {
                            syncToClient(this.drained, true);
                        }
                        faucetState = FaucetState.POURING;
                        // pour after initial packet, in case we end up resetting later
                        pour();
                    }
                    return true;
                }
            }
            
            // if powered, keep faucet running
            if (lastRedstoneState) {
                // sync if either we were not pouring before (particle effects), or if the client thinks we have fluid
                if (execute && (faucetState == FaucetState.OFF || !renderFluid.isFluidEqual(FluidStack.EMPTY))) {
                    syncToClient(FluidStack.EMPTY, true);
                }
                faucetState = FaucetState.POWERED;
                return false;
            }
        }
        // reset if not powered, or if nothing to do
        if (execute) {
            reset();
        }
        return false;
    }
    
    /**
     * Takes the liquid inside and executes one pouring step.
     */
    private void pour() {
        if (drained.isEmpty()) {
            return;
        }
        
        // ensure we have an output
        LazyOptional<IFluidHandler> outputOptional = getOutputHandler();
        if (outputOptional.isPresent()) {
            FluidStack fillStack = drained.copy();
            fillStack.setAmount(Math.min(drained.getAmount(), MB_PER_TICK));
            
            // can we fill?
            IFluidHandler output = outputOptional.orElse(EmptyFluidHandler.INSTANCE);
            int filled = output.fill(fillStack, IFluidHandler.FluidAction.SIMULATE);
            if (filled > 0) {
                // update client if they do not think we have fluid
                if (!renderFluid.isFluidEqual(drained)) {
                    syncToClient(drained, true);
                }
                
                // transfer it
                this.drained.shrink(filled);
                fillStack.setAmount(filled);
                output.fill(fillStack, IFluidHandler.FluidAction.EXECUTE);
            }
        }
        else {
            // output got lost. all liquid buffered is lost.
            reset();
        }
    }
    
    /**
     * Resets TE to default state.
     */
    private void reset() {
        stopPouring = false;
        drained = FluidStack.EMPTY;
        if (faucetState != FaucetState.OFF || !renderFluid.isFluidEqual(drained)) {
            faucetState = FaucetState.OFF;
            syncToClient(FluidStack.EMPTY, false);
        }
    }
    
    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(worldPosition.getX(), worldPosition.getY() - 1, worldPosition.getZ(), worldPosition.getX() + 1, worldPosition.getY() + 1, worldPosition.getZ() + 1);
    }
    
    
    /* NBT and networking */
    
    /**
     * Sends an update to the client with the most recent
     * @param fluid       New fluid
     * @param isPouring   New isPouring status
     */
    private void syncToClient(FluidStack fluid, boolean isPouring) {
        renderFluid = fluid.copy();
        MetallurgicaPackets.sendToClientsNear(new FaucetActivationPacket(worldPosition, fluid, isPouring), level, worldPosition);
    }
    @Override
    public void notifyUpdate() {
        super.notifyUpdate();
    }
    
    /**
     * Sets draining fluid to specified stack.
     * @param fluid new FluidStack
     */
    public void onActivationPacket(FluidStack fluid, boolean isPouring) {
        // pouring and powered are interchangable on the client
        this.faucetState = isPouring ? FaucetState.POURING : FaucetState.OFF;
        this.renderFluid = fluid;
    }
    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putByte("state", (byte)faucetState.ordinal());
        compound.putBoolean("stop", stopPouring);
        compound.putBoolean("lastRedstone", lastRedstoneState);
        if (!drained.isEmpty()) {
            compound.put("drained", drained.writeToNBT(new CompoundTag()));
        }
        if (!renderFluid.isEmpty()) {
            compound.put("render_fluid", renderFluid.writeToNBT(new CompoundTag()));
        }
    }
    
    @Override
    public void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        
        faucetState = FaucetState.fromIndex(compound.getByte("state"));
        stopPouring = compound.getBoolean("stop");
        lastRedstoneState = compound.getBoolean("lastRedstone");
        // fluids
        if (compound.contains("drained", Tag.TAG_COMPOUND)) {
            drained = FluidStack.loadFluidStackFromNBT(compound.getCompound("drained"));
        } else {
            drained = FluidStack.EMPTY;
        }
        if (compound.contains("render_fluid", Tag.TAG_COMPOUND)) {
            renderFluid = FluidStack.loadFluidStackFromNBT(compound.getCompound("render_fluid"));
        } else {
            renderFluid = FluidStack.EMPTY;
        }
    }
    
    private enum FaucetState {
        OFF,
        POURING,
        POWERED;
        
        /** Gets the state for the given index */
        public static FaucetState fromIndex(int index) {
            switch (index) {
                case 1: return POURING;
                case 2: return POWERED;
            }
            return OFF;
        }
    }
}
