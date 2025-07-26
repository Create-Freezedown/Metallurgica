package dev.metallurgists.metallurgica.content.fluids.channel.channel_depot;

import dev.metallurgists.metallurgica.foundation.util.MetalLang;
import com.google.common.collect.ImmutableList;
import com.simibubi.create.AllParticleTypes;
import com.simibubi.create.AllTags;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.fluids.FluidFX;
import com.simibubi.create.content.fluids.particle.FluidParticleData;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.utility.*;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.data.IntAttached;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.lang.LangBuilder;
import net.createmod.catnip.math.VecHelper;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import java.util.*;

@SuppressWarnings("removal")
public class ChannelDepotBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {
    private boolean areFluidsMoving;
    public SmartFluidTankBehaviour inputTank;
    protected SmartFluidTankBehaviour outputTank;
    private FilteringBehaviour filtering;
    private boolean contentsChanged;
    private Couple<SmartFluidTankBehaviour> tanks;
    protected LazyOptional<IFluidHandler> fluidCapability;
    
    
    //Implement this once we have Channels
    List<Direction> disabledSpoutputs;
    Direction preferredSpoutput;
    protected List<FluidStack> spoutputFluidBuffer;
    int recipeBackupCheck;
    List<IntAttached<FluidStack>> visualizedOutputFluids;
    public static final int OUTPUT_ANIMATION_TIME = 10;
    
    public ChannelDepotBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        areFluidsMoving = false;
        contentsChanged = true;
        tanks = Couple.create(inputTank, outputTank);
        visualizedOutputFluids = Collections.synchronizedList(new ArrayList<>());
        //Implement this once we have Channels
        disabledSpoutputs = new ArrayList<>();
        preferredSpoutput = null;
        spoutputFluidBuffer = new ArrayList<>();
        recipeBackupCheck = 20;
    }
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        filtering = new FilteringBehaviour(this, new ChannelDepotValueBox()).withCallback(newFilter -> contentsChanged = true)
                .forRecipes();
        behaviours.add(filtering);
        
        inputTank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.INPUT, this, 2, 1000, true)
                .whenFluidUpdates(() -> contentsChanged = true);
        outputTank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.OUTPUT, this, 2, 1000, true)
                .whenFluidUpdates(() -> contentsChanged = true)
                .forbidInsertion();
        behaviours.add(inputTank);
        behaviours.add(outputTank);
        
        fluidCapability = LazyOptional.of(() -> {
            LazyOptional<? extends IFluidHandler> inputCap = inputTank.getCapability();
            LazyOptional<? extends IFluidHandler> outputCap = outputTank.getCapability();
            return new CombinedTankWrapper(outputCap.orElse(null), inputCap.orElse(null));
        });
    }
    
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        preferredSpoutput = null;
        if (compound.contains("PreferredSpoutput"))
            preferredSpoutput = NBTHelper.readEnum(compound, "PreferredSpoutput", Direction.class);
        disabledSpoutputs.clear();
        ListTag disabledList = compound.getList("DisabledSpoutput", Tag.TAG_STRING);
        disabledList.forEach(d -> disabledSpoutputs.add(Direction.valueOf(((StringTag) d).getAsString())));
        spoutputFluidBuffer = NBTHelper.readCompoundList(compound.getList("FluidOverflow", Tag.TAG_COMPOUND),
                FluidStack::loadFluidStackFromNBT);
        
        if (!clientPacket)
            return;
        
        NBTHelper.iterateCompoundList(compound.getList("VisualizedFluids", Tag.TAG_COMPOUND),
                c -> visualizedOutputFluids
                        .add(IntAttached.with(OUTPUT_ANIMATION_TIME, FluidStack.loadFluidStackFromNBT(c))));
    }
    
    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        if (preferredSpoutput != null)
            NBTHelper.writeEnum(compound, "PreferredSpoutput", preferredSpoutput);
        ListTag disabledList = new ListTag();
        disabledSpoutputs.forEach(d -> disabledList.add(StringTag.valueOf(d.name())));
        compound.put("DisabledSpoutput", disabledList);
        compound.put("FluidOverflow",
                NBTHelper.writeCompoundList(spoutputFluidBuffer, fs -> fs.writeToNBT(new CompoundTag())));
        
        if (!clientPacket)
            return;
        
        compound.put("VisualizedFluids", NBTHelper.writeCompoundList(visualizedOutputFluids, ia -> ia.getValue()
                .writeToNBT(new CompoundTag())));
        visualizedOutputFluids.clear();
    }
    
    @Override
    public void destroy() {
        super.destroy();
        //spoutputBuffer.forEach(is -> Block.popResource(level, worldPosition, is));
    }
    @Override
    public void remove() {
        super.remove();
        onEmptied();
    }
    
    public void onEmptied() {
        getOperator().ifPresent(be -> be.basinRemoved = true);
    }
    
    @Override
    public void invalidate() {
        super.invalidate();
        fluidCapability.invalidate();
    }
    
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER)
            return fluidCapability.cast();
        return super.getCapability(cap, side);
    }
    
    @Override
    public void notifyUpdate() {
        super.notifyUpdate();
    }
    
    
    public boolean isEmpty() {
        return inputTank.isEmpty() && outputTank.isEmpty();
    }
    
    public void onWrenched(Direction face) {
        BlockState blockState = getBlockState();
        Direction currentFacing = blockState.getValue(ChannelDepotBlock.FACING);
        
        disabledSpoutputs.remove(face);
        if (currentFacing == face) {
            if (preferredSpoutput == face)
                preferredSpoutput = null;
            disabledSpoutputs.add(face);
        } else
            preferredSpoutput = face;
        
        updateSpoutput();
    }
    private void updateSpoutput() {
        BlockState blockState = getBlockState();
        Direction currentFacing = blockState.getValue(ChannelDepotBlock.FACING);
        Direction newFacing = Direction.DOWN;
        for (Direction test : Iterate.horizontalDirections) {
            boolean canOutputTo = ChannelDepotBlock.canOutputTo(level, worldPosition, test);
            if (canOutputTo && !disabledSpoutputs.contains(test))
                newFacing = test;
        }
        
        if (preferredSpoutput != null && ChannelDepotBlock.canOutputTo(level, worldPosition, preferredSpoutput)
                && preferredSpoutput != Direction.UP)
            newFacing = preferredSpoutput;
        
        if (newFacing == currentFacing)
            return;
        
        level.setBlockAndUpdate(worldPosition, blockState.setValue(ChannelDepotBlock.FACING, newFacing));
        
        if (newFacing.getAxis()
                .isVertical())
            return;
        
        IFluidHandler handler = outputTank.getCapability()
                .orElse(null);
        for (int slot = 0; slot < handler.getTanks(); slot++) {
            FluidStack fs = handler.getFluidInTank(slot)
                    .copy();
            if (fs.isEmpty())
                continue;
            if (acceptOutputs(ImmutableList.of(fs), true)) {
                handler.drain(fs, IFluidHandler.FluidAction.EXECUTE);
                acceptOutputs(ImmutableList.of(fs), false);
            }
        }
        
        notifyChangeOfContents();
        notifyUpdate();
    }
    @Override
    public void tick() {
        super.tick();
        if (level.isClientSide) {
            createFluidParticles();
            tickVisualizedOutputs();
        }
        
        if ((!spoutputFluidBuffer.isEmpty()) && !level.isClientSide)
            tryClearingSpoutputOverflow();
        if (!contentsChanged)
            return;
        
        contentsChanged = false;
        getOperator().ifPresent(be -> be.basinChecker.scheduleUpdate());
        
        for (Direction offset : Iterate.horizontalDirections) {
            BlockPos toUpdate = worldPosition.above()
                    .relative(offset);
            BlockState stateToUpdate = level.getBlockState(toUpdate);
            if (stateToUpdate.getBlock() instanceof ChannelDepotBlock
                    && stateToUpdate.getValue(ChannelDepotBlock.FACING) == offset.getOpposite()) {
                BlockEntity be = level.getBlockEntity(toUpdate);
                if (be instanceof ChannelDepotBlockEntity)
                    ((ChannelDepotBlockEntity) be).contentsChanged = true;
            }
        }
    }
    private void tryClearingSpoutputOverflow() {
        BlockState blockState = getBlockState();
        if (!(blockState.getBlock() instanceof ChannelDepotBlock))
            return;
        Direction direction = blockState.getValue(ChannelDepotBlock.FACING);
        BlockEntity be = level.getBlockEntity(worldPosition.below()
                .relative(direction));
        
        FilteringBehaviour filter = null;
        if (be != null) {
            filter = BlockEntityBehaviour.get(level, be.getBlockPos(), FilteringBehaviour.TYPE);
        }
        IFluidHandler targetTank = be == null ? null
                : be.getCapability(ForgeCapabilities.FLUID_HANDLER, direction.getOpposite())
                .orElse(null);
        
        boolean update = false;
        
        for (Iterator<FluidStack> iterator = spoutputFluidBuffer.iterator(); iterator.hasNext();) {
            FluidStack fluidStack = iterator.next();
            
            if (direction == Direction.DOWN) {
                iterator.remove();
                update = true;
                continue;
            }
            
            if (targetTank == null)
                break;
            
            for (boolean simulate : Iterate.trueAndFalse) {
                IFluidHandler.FluidAction action = simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE;
                int fill = targetTank instanceof SmartFluidTankBehaviour.InternalFluidHandler
                        ? ((SmartFluidTankBehaviour.InternalFluidHandler) targetTank).forceFill(fluidStack.copy(), action)
                        : targetTank.fill(fluidStack.copy(), action);
                if (fill != fluidStack.getAmount())
                    break;
                if (simulate)
                    continue;
                
                update = true;
                iterator.remove();
                visualizedOutputFluids.add(IntAttached.withZero(fluidStack));
            }
        }
        
        if (update) {
            notifyChangeOfContents();
            sendData();
        }
    }
    public float getTotalFluidUnits(float partialTicks) {
        int renderedFluids = 0;
        float totalUnits = 0;
        
        for (SmartFluidTankBehaviour behaviour : getTanks()) {
            if (behaviour == null)
                continue;
            for (SmartFluidTankBehaviour.TankSegment tankSegment : behaviour.getTanks()) {
                if (tankSegment.getRenderedFluid()
                        .isEmpty())
                    continue;
                float units = tankSegment.getTotalUnits(partialTicks);
                if (units < 1)
                    continue;
                totalUnits += units;
                renderedFluids++;
            }
        }
        
        if (renderedFluids == 0)
            return 0;
        if (totalUnits < 1)
            return 0;
        return totalUnits;
    }
    
    private Optional<BasinOperatingBlockEntity> getOperator() {
        if (level == null)
            return Optional.empty();
        BlockEntity be = level.getBlockEntity(worldPosition.above(2));
        if (be instanceof BasinOperatingBlockEntity)
            return Optional.of((BasinOperatingBlockEntity) be);
        return Optional.empty();
    }
    
    public FilteringBehaviour getFilter() {
        return filtering;
    }
    
    public void notifyChangeOfContents() {
        contentsChanged = true;
    }
    
    public boolean canContinueProcessing() {
        return spoutputFluidBuffer.isEmpty();
    }
    
    public boolean acceptOutputs(List<FluidStack> outputFluids, boolean simulate) {
        outputTank.allowInsertion();
        boolean acceptOutputsInner = acceptOutputsInner(outputFluids, simulate);
        outputTank.forbidInsertion();
        return acceptOutputsInner;
    }
    private boolean acceptOutputsInner(List<FluidStack> outputFluids, boolean simulate) {
        BlockState blockState = getBlockState();
        if (!(blockState.getBlock() instanceof ChannelDepotBlock))
            return false;
        
        Direction direction = blockState.getValue(ChannelDepotBlock.FACING);
        if (direction != Direction.DOWN) {
            
            BlockEntity be = level.getBlockEntity(worldPosition.below()
                    .relative(direction));
            IFluidHandler targetTank = be == null ? null
                    : be.getCapability(ForgeCapabilities.FLUID_HANDLER, direction.getOpposite())
                    .orElse(null);
            boolean externalTankNotPresent = targetTank == null;
            
            if (!outputFluids.isEmpty() && externalTankNotPresent) {
                // Special case - fluid outputs but output only accepts items
                targetTank = outputTank.getCapability()
                        .orElse(null);
                if (targetTank == null)
                    return false;
                if (!acceptFluidOutputsIntoChannel(outputFluids, simulate, targetTank))
                    return false;
            }
            
            if (simulate)
                return true;
            if (!externalTankNotPresent)
                for (FluidStack fluidStack : outputFluids)
                    spoutputFluidBuffer.add(fluidStack.copy());
            return true;
        }
        
        IFluidHandler targetTank = outputTank.getCapability()
                .orElse(null);
        
        
        if (outputFluids.isEmpty())
            return true;
        if (targetTank == null)
            return false;
        if (!acceptFluidOutputsIntoChannel(outputFluids, simulate, targetTank))
            return false;
        
        return true;
    }
    private boolean acceptFluidOutputsIntoChannel(List<FluidStack> outputFluids, boolean simulate, IFluidHandler targetTank) {
        for (FluidStack fluidStack : outputFluids) {
            IFluidHandler.FluidAction action = simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE;
            int fill = targetTank instanceof SmartFluidTankBehaviour.InternalFluidHandler
                    ? ((SmartFluidTankBehaviour.InternalFluidHandler) targetTank).forceFill(fluidStack.copy(), action)
                    : targetTank.fill(fluidStack.copy(), action);
            if (fill != fluidStack.getAmount())
                return false;
        }
        return true;
    }
    
    public static BlazeBurnerBlock.HeatLevel getHeatLevelOf(BlockState state) {
        if (state.hasProperty(BlazeBurnerBlock.HEAT_LEVEL))
            return state.getValue(BlazeBurnerBlock.HEAT_LEVEL);
        return AllTags.AllBlockTags.PASSIVE_BOILER_HEATERS.matches(state) && BlockHelper.isNotUnheated(state) ? BlazeBurnerBlock.HeatLevel.SMOULDERING : BlazeBurnerBlock.HeatLevel.NONE;
    }
    
    public Couple<SmartFluidTankBehaviour> getTanks() {
        return tanks;
    }
    
    private void tickVisualizedOutputs() {
        visualizedOutputFluids.forEach(IntAttached::decrement);
        visualizedOutputFluids.removeIf(IntAttached::isOrBelowZero);
    }
    
    private void createFluidParticles() {
        RandomSource r = level.random;
        
        if (!visualizedOutputFluids.isEmpty())
            createOutputFluidParticles(r);
        
        if (!areFluidsMoving && r.nextFloat() > 1 / 8f)
            return;
        
        int segments = 0;
        for (SmartFluidTankBehaviour behaviour : getTanks()) {
            if (behaviour == null)
                continue;
            for (SmartFluidTankBehaviour.TankSegment tankSegment : behaviour.getTanks())
                if (!tankSegment.isEmpty(0))
                    segments++;
        }
        if (segments < 2)
            return;
        
        float totalUnits = getTotalFluidUnits(0);
        if (totalUnits == 0)
            return;
        float fluidLevel = Mth.clamp(totalUnits / 2000, 0, 1);
        float rim = 2 / 16f;
        float space = 12 / 16f;
        float surface = worldPosition.getY() + rim + space * fluidLevel + 1 / 32f;
        
        if (areFluidsMoving) {
            createMovingFluidParticles(surface, segments);
            return;
        }
        
        for (SmartFluidTankBehaviour behaviour : getTanks()) {
            if (behaviour == null)
                continue;
            for (SmartFluidTankBehaviour.TankSegment tankSegment : behaviour.getTanks()) {
                if (tankSegment.isEmpty(0))
                    continue;
                float x = worldPosition.getX() + rim + space * r.nextFloat();
                float z = worldPosition.getZ() + rim + space * r.nextFloat();
                level.addAlwaysVisibleParticle(
                        new FluidParticleData(AllParticleTypes.BASIN_FLUID.get(), tankSegment.getRenderedFluid()), x,
                        surface, z, 0, 0, 0);
            }
        }
    }
    private void createOutputFluidParticles(RandomSource r) {
        BlockState blockState = getBlockState();
        if (!(blockState.getBlock() instanceof ChannelDepotBlock))
            return;
        Direction direction = blockState.getValue(ChannelDepotBlock.FACING);
        if (direction == Direction.DOWN)
            return;
        Vec3 directionVec = Vec3.atLowerCornerOf(direction.getNormal());
        Vec3 outVec = VecHelper.getCenterOf(worldPosition)
                .add(directionVec.scale(.65)
                        .subtract(0, 1 / 4f, 0));
        Vec3 outMotion = directionVec.scale(1 / 16f)
                .add(0, -1 / 16f, 0);
        
        for (int i = 0; i < 2; i++) {
            visualizedOutputFluids.forEach(ia -> {
                FluidStack fluidStack = ia.getValue();
                ParticleOptions fluidParticle = FluidFX.getFluidParticle(fluidStack);
                Vec3 m = VecHelper.offsetRandomly(outMotion, r, 1 / 16f);
                level.addAlwaysVisibleParticle(fluidParticle, outVec.x, outVec.y, outVec.z, m.x, m.y, m.z);
            });
        }
    }
    
    private void createMovingFluidParticles(float surface, int segments) {
        Vec3 pointer = new Vec3(1, 0, 0).scale(1 / 16f);
        float interval = 360f / segments;
        Vec3 centerOf = VecHelper.getCenterOf(worldPosition);
        float intervalOffset = (AnimationTickHolder.getTicks() * 18) % 360;
        
        int currentSegment = 0;
        for (SmartFluidTankBehaviour behaviour : getTanks()) {
            if (behaviour == null)
                continue;
            for (SmartFluidTankBehaviour.TankSegment tankSegment : behaviour.getTanks()) {
                if (tankSegment.isEmpty(0))
                    continue;
                float angle = interval * (1 + currentSegment) + intervalOffset;
                Vec3 vec = centerOf.add(VecHelper.rotate(pointer, angle, Direction.Axis.Y));
                level.addAlwaysVisibleParticle(
                        new FluidParticleData(AllParticleTypes.BASIN_FLUID.get(), tankSegment.getRenderedFluid()), vec.x(),
                        surface, vec.z(), 1, 0, 0);
                currentSegment++;
            }
        }
    }
    public boolean areFluidsMoving() {
        return areFluidsMoving;
    }
    
    public boolean setAreFluidsMoving(boolean areFluidsMoving) {
        this.areFluidsMoving = areFluidsMoving;
        return areFluidsMoving;
    }
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        MetalLang.translate("gui.goggles.channel_depot_contents")
                .forGoggles(tooltip);
        
        IFluidHandler fluids = fluidCapability.orElse(new FluidTank(0));
        boolean isEmpty = true;
        LangBuilder mb = MetalLang.translate("generic.unit.millibuckets");
        for (int i = 0; i < fluids.getTanks(); i++) {
            FluidStack fluidStack = fluids.getFluidInTank(i);
            if (fluidStack.isEmpty())
                continue;
            MetalLang.text("")
                    .add(MetalLang.fluidName(fluidStack)
                            .add(MetalLang.text(" "))
                            .style(ChatFormatting.GRAY)
                            .add(MetalLang.number(fluidStack.getAmount())
                                    .add(mb)
                                    .style(ChatFormatting.BLUE)))
                    .forGoggles(tooltip, 1);
            isEmpty = false;
        }
        
        if (isEmpty)
            tooltip.remove(0);
        
        return true;
    }
    
    class ChannelDepotValueBox extends ValueBoxTransform.Sided {
        
        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8, 12, 16.05);
        }
        
        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            return direction.getAxis()
                    .isHorizontal();
        }
        
    }
}
