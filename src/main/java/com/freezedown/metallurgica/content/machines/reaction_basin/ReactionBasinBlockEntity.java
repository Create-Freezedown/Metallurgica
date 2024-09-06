package com.freezedown.metallurgica.content.machines.reaction_basin;

import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.List;

public class ReactionBasinBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation, IReactionBasinContainer {
    private static final int MAX_SIZE = 16;
    
    protected LazyOptional<IFluidHandler> fluidCapability;
    protected boolean forceFluidLevelUpdate;
    protected FluidTank tankInventory;
    protected BlockPos controller;
    protected BlockPos lastKnownPos;
    protected boolean updateConnectivity;
    protected int luminosity;
    protected int width;
    protected int height;
    
    private static final int SYNC_RATE = 8;
    protected int syncCooldown;
    protected boolean queuedSync;
    
    // For rendering purposes only
    private LerpedFloat fluidLevel;
    
    public ReactionBasinBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        tankInventory = createInventory();
        fluidCapability = LazyOptional.of(() -> tankInventory);
        forceFluidLevelUpdate = true;
        updateConnectivity = false;
        height = 1;
        width = 1;
        
    }
    
    protected SmartFluidTank createInventory() {
        return new SmartFluidTank(getCapacityMultiplier(), this::onFluidStackChanged);
    }
    
    public static int getCapacityMultiplier() {
        return AllConfigs.server().fluids.fluidTankCapacity.get() * 1000;
    }
    
    protected void onFluidStackChanged(FluidStack newFluidStack) {
        if (!hasLevel())
            return;
        
        FluidType attributes = newFluidStack.getFluid()
                .getFluidType();
        int luminosity = (int) (attributes.getLightLevel(newFluidStack) / 1.2f);
        boolean reversed = attributes.isLighterThanAir();
        int maxY = (int) ((getFillState() * height) + 1);
        
        for (int yOffset = 0; yOffset < height; yOffset++) {
            boolean isBright = reversed ? (height - yOffset <= maxY) : (yOffset < maxY);
            int actualLuminosity = isBright ? luminosity : luminosity > 0 ? 1 : 0;
            
            for (int xOffset = 0; xOffset < width; xOffset++) {
                for (int zOffset = 0; zOffset < width; zOffset++) {
                    BlockPos pos = this.worldPosition.offset(xOffset, yOffset, zOffset);
                    ReactionBasinBlockEntity tankAt = ConnectivityHandler.partAt(getType(), level, pos);
                    if (tankAt == null)
                        continue;
                    level.updateNeighbourForOutputSignal(pos, tankAt.getBlockState()
                            .getBlock());
                    if (tankAt.luminosity == actualLuminosity)
                        continue;
                    tankAt.setLuminosity(actualLuminosity);
                }
            }
        }
        
        if (!level.isClientSide) {
            setChanged();
            sendData();
        }
        
        if (isVirtual()) {
            if (fluidLevel == null)
                fluidLevel = LerpedFloat.linear()
                        .startWithValue(getFillState());
            fluidLevel.chase(getFillState(), .5f, LerpedFloat.Chaser.EXP);
        }
    }
    
    @Override
    public void tick() {
        super.tick();
        if (syncCooldown > 0) {
            syncCooldown--;
            if (syncCooldown == 0 && queuedSync)
                sendData();
        }
        
        if (lastKnownPos == null)
            lastKnownPos = getBlockPos();
        else if (!lastKnownPos.equals(worldPosition) && worldPosition != null) {
            onPositionChanged();
            return;
        }
        
        if (updateConnectivity)
            updateConnectivity();
        if (fluidLevel != null)
            fluidLevel.tickChaser();
    }
    private void onPositionChanged() {
        removeController(true);
        lastKnownPos = worldPosition;
    }
    protected void updateConnectivity() {
        updateConnectivity = false;
        if (level.isClientSide)
            return;
        if (!isController())
            return;
        ConnectivityHandler.formMulti(this);
    }
    
    private void refreshCapability() {
        LazyOptional<IFluidHandler> oldCap = fluidCapability;
        fluidCapability = LazyOptional.of(() -> handlerForCapability());
        oldCap.invalidate();
    }
    
    private IFluidHandler handlerForCapability() {
        return isController() ? tankInventory : getControllerBE() != null ? getControllerBE().handlerForCapability() : new FluidTank(0);
    }
    
    protected void setLuminosity(int luminosity) {
        if (level.isClientSide)
            return;
        if (this.luminosity == luminosity)
            return;
        this.luminosity = luminosity;
        sendData();
    }
    
    public float getFillState() {
        return (float) tankInventory.getFluidAmount() / tankInventory.getCapacity();
    }
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    
    }
    
    @Override
    public BlockPos getController() {
        return isController() ? worldPosition : controller;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public ReactionBasinBlockEntity getControllerBE() {
        if (isController())
            return this;
        BlockEntity blockEntity = level.getBlockEntity(controller);
        if (blockEntity instanceof ReactionBasinBlockEntity)
            return (ReactionBasinBlockEntity) blockEntity;
        return null;
    }
    
    @Override
    public boolean isController() {
        return controller == null || worldPosition.getX() == controller.getX()
                && worldPosition.getY() == controller.getY() && worldPosition.getZ() == controller.getZ();
    }
    
    @Override
    public void setController(BlockPos pos) {
        if (level.isClientSide && !isVirtual())
            return;
        if (controller.equals(this.controller))
            return;
        this.controller = controller;
        refreshCapability();
        setChanged();
        sendData();
    }
    public void applyFluidTankSize(int blocks) {
        tankInventory.setCapacity(blocks * getCapacityMultiplier());
        int overflow = tankInventory.getFluidAmount() - tankInventory.getCapacity();
        if (overflow > 0)
            tankInventory.drain(overflow, IFluidHandler.FluidAction.EXECUTE);
        forceFluidLevelUpdate = true;
    }
    public void removeController(boolean keepFluids) {
        if (level.isClientSide)
            return;
        updateConnectivity = true;
        if (!keepFluids)
            applyFluidTankSize(1);
        controller = null;
        width = 1;
        height = 1;
        onFluidStackChanged(tankInventory.getFluid());
        
        BlockState state = getBlockState();
        if (ReactionBasinBlock.isRB(state)) {
            state = state.setValue(ReactionBasinBlock.BOTTOM, true);
            state = state.setValue(ReactionBasinBlock.TOP, true);
            state = state.setValue(ReactionBasinBlock.SHAPE, ReactionBasinBlock.Shape.PLAIN);
            getLevel().setBlock(worldPosition, state, 22);
        }
        
        refreshCapability();
        setChanged();
        sendData();
    }
    public static int getMaxHeight() {
        return AllConfigs.server().fluids.fluidTankMaxHeight.get();
    }
    @Override
    public Direction.Axis getMainConnectionAxis() {
        return Direction.Axis.Y;
    }
    
    @Override
    public BlockPos getLastKnownPos() {
        return lastKnownPos;
    }
    
    @Override
    public void preventConnectivityUpdate() {
        updateConnectivity = false;
    }
    
    @Override
    public void notifyMultiUpdated() {
        BlockState state = this.getBlockState();
        if (FluidTankBlock.isTank(state)) { // safety
            state = state.setValue(FluidTankBlock.BOTTOM, getController().getY() == getBlockPos().getY());
            state = state.setValue(FluidTankBlock.TOP, getController().getY() + height - 1 == getBlockPos().getY());
            level.setBlock(getBlockPos(), state, 6);
        }
        onFluidStackChanged(tankInventory.getFluid());
        setChanged();
    }
    
    @Override
    public int getMaxLength(Direction.Axis longAxis, int width) {
        if (longAxis == Direction.Axis.Y)
            return getMaxHeight();
        return getMaxWidth();
    }
    
    @Override
    public int getMaxWidth() {
        return MAX_SIZE;
    }
    
    @Override
    public int getHeight() {
        return height;
    }
    
    @Override
    public void setHeight(int height) {
        this.height = height;
    }
    
    @Override
    public int getWidth() {
        return width;
    }
    
    @Override
    public void setWidth(int width) {
        this.width = width;
    }
    
    @Override
    public boolean hasTank() {
        return true;
    }
    @Override
    public boolean hasInventory() {
        return true;
    }
    
    @Override
    public int getTankSize(int tank) {
        return getCapacityMultiplier();
    }
    
    @Override
    public void setTankSize(int tank, int blocks) {
        applyFluidTankSize(blocks);
    }
    
    @Override
    public IFluidTank getTank(int tank) {
        return tankInventory;
    }
    
    @Override
    public FluidStack getFluid(int tank) {
        return tankInventory.getFluid()
                .copy();
    }
}
