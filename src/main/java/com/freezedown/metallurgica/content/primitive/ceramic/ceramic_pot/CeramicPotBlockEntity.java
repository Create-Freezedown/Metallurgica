package com.freezedown.metallurgica.content.primitive.ceramic.ceramic_pot;

import com.freezedown.metallurgica.foundation.util.PistonPushable;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@PistonPushable
public class CeramicPotBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {
    protected LazyOptional<IFluidHandler> fluidCapability;
    protected boolean forceFluidLevelUpdate;
    // For rendering purposes only
    private LerpedFloat fluidLevel;
    protected int luminosity;
    
    public SmartFluidTankBehaviour tank;
    
    private boolean shouldShatter;
    
    private static final int SYNC_RATE = 8;
    protected int syncCooldown;
    protected boolean queuedSync;
    
    public CeramicPotBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        forceFluidLevelUpdate = true;
    }
    
    @Override
    public void tick() {
        super.tick();
        if (syncCooldown > 0) {
            syncCooldown--;
            if (syncCooldown == 0 && queuedSync)
                sendData();
        }
        
    }
    
    protected void setLuminosity(int luminosity) {
        if (level.isClientSide)
            return;
        if (this.luminosity == luminosity)
            return;
        this.luminosity = luminosity;
        sendData();
    }
    
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return containedFluidTooltip(tooltip, isPlayerSneaking,
                this.getCapability(ForgeCapabilities.FLUID_HANDLER));
    }
    
    public float getFillState() {
        return (float) tank.getPrimaryHandler().getFluidAmount() / tank.getPrimaryHandler().getCapacity();
    }
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        tank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.INPUT, this, 1, 1000, true)
                .whenFluidUpdates(() -> {
                    if (!hasLevel())
                        return;
                    
                    FluidType attributes = tank.getPrimaryHandler().getFluid().getFluid().getFluidType();
                    int luminosity = (int) (attributes.getLightLevel(tank.getPrimaryHandler().getFluid()) / 1.2f);
                    int temperature = attributes.getTemperature();
                    shouldShatter = temperature >= 1000;
                    setLuminosity(luminosity);
                    
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
                });
        
        behaviours.add(tank);
        
        fluidCapability = LazyOptional.of(() -> {
            LazyOptional<? extends IFluidHandler> inputCap = tank.getCapability();
            return new CombinedTankWrapper(inputCap.orElse(null));
        });
    }
    
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        
        int prevLum = luminosity;
        luminosity = compound.getInt("Luminosity");
        shouldShatter = compound.getBoolean("ShouldShatter");
        
        tank.getPrimaryHandler().readFromNBT(compound.getCompound("TankContent"));
        if (tank.getPrimaryHandler().getSpace() < 0)
            tank.getPrimaryHandler().drain(-tank.getPrimaryHandler().getSpace(), IFluidHandler.FluidAction.EXECUTE);
        
        if (compound.contains("ForceFluidLevel") || fluidLevel == null)
            fluidLevel = LerpedFloat.linear()
                    .startWithValue(getFillState());
        
        if (!clientPacket)
            return;
        
        float fillState = getFillState();
        if (compound.contains("ForceFluidLevel") || fluidLevel == null)
            fluidLevel = LerpedFloat.linear()
                    .startWithValue(fillState);
        fluidLevel.chase(fillState, 0.5f, LerpedFloat.Chaser.EXP);
        
        if (luminosity != prevLum && hasLevel())
            level.getChunkSource()
                    .getLightEngine()
                    .checkBlock(worldPosition);
        
        if (compound.contains("LazySync"))
            fluidLevel.chase(fluidLevel.getChaseTarget(), 0.125f, LerpedFloat.Chaser.EXP);
    }
    
    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.put("TankContent", tank.getPrimaryHandler().writeToNBT(new CompoundTag()));
        compound.putInt("Luminosity", luminosity);
        compound.putBoolean("ShouldShatter", shouldShatter);
        super.write(compound, clientPacket);
        if (!clientPacket)
            return;
        if (forceFluidLevelUpdate)
            compound.putBoolean("ForceFluidLevel", true);
        if (queuedSync)
            compound.putBoolean("LazySync", true);
        forceFluidLevelUpdate = false;
    }
    
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER)
            return fluidCapability.cast();
        return super.getCapability(cap, side);
    }
    public void sendDataImmediately() {
        syncCooldown = 0;
        queuedSync = false;
        sendData();
    }
    @Override
    public void invalidate() {
        super.invalidate();
        fluidCapability.invalidate();
    }
    @Override
    public void notifyUpdate() {
        super.notifyUpdate();
    }
    
    public IFluidTank getTankInventory() {
        return tank.getPrimaryHandler();
    }
}
