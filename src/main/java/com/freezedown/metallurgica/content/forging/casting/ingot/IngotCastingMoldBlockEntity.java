package com.freezedown.metallurgica.content.forging.casting.ingot;

import com.freezedown.metallurgica.content.fluids.types.MoltenMetal;
import com.freezedown.metallurgica.foundation.util.ClientUtil;
import com.freezedown.metallurgica.foundation.util.recipe.helper.TagItemOutput;
import com.freezedown.metallurgica.registry.MetallurgicaMetals;
import com.freezedown.metallurgica.registry.MetallurgicaTags;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;
import java.util.List;

public class IngotCastingMoldBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation {
    protected LazyOptional<IFluidHandler> fluidCapability;
    protected LazyOptional<IItemHandlerModifiable> itemCapability;
    protected boolean forceFluidLevelUpdate;
    // For rendering purposes only
    private LerpedFloat fluidLevel;
    protected int luminosity;
    
    public SmartFluidTankBehaviour tank;
    protected SmartInventory inventory;
    
    private boolean shouldShatter;
    
    private boolean contentsChanged;
    private double internalTemperature;
    private double lastTemperature;
    private final double minTemperature = 20;
    
    private boolean hasCasting;
    
    private static final int SYNC_RATE = 8;
    protected int syncCooldown;
    protected boolean queuedSync;
    
    public IngotCastingMoldBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        inventory = new IngotCastingMoldInventory(1, this).forbidInsertion().withMaxStackSize(1);
        itemCapability = LazyOptional.of(() -> new CombinedInvWrapper(inventory));
        forceFluidLevelUpdate = true;
        lastTemperature = 20;
    }
    
    @Override
    public void tick() {
        if (!inventory.getStackInSlot(0).isEmpty()) {
            tank.forbidInsertion();
        } else {
            tank.allowInsertion();
        }
        super.tick();
        if (syncCooldown > 0) {
            syncCooldown--;
            if (syncCooldown == 0 && queuedSync)
                sendData();
        }
        if (level == null || level.isClientSide)
            return;
        String metalName = "";
        
        if (getTankInventory().getFluid().getFluid() instanceof MoltenMetal moltenMetal && moltenMetal.getTemperature() > internalTemperature && lastTemperature != moltenMetal.getTemperature()) {
            internalTemperature = moltenMetal.getTemperature();
            lastTemperature = moltenMetal.getTemperature();
        }
        
        if (internalTemperature > minTemperature) {
            if (level.getGameTime() % 20 == 0) {
                internalTemperature-= 0.1d;
            }
        }
        
        
        
        for (MetallurgicaMetals metalEntry : MetallurgicaMetals.values()) {
            if (metalEntry.METAL.getMolten().get().isSame(getTankInventory().getFluid().getFluid())) {
                hasCasting = true;
                if (getTankInventory().getFluidAmount() < 90) {
                    internalTemperature = metalEntry.METAL.getMolten().get().getTemperature();
                }
                metalName = metalEntry.name().toLowerCase();
                if (getTankInventory().getFluidAmount() >= 90 && hasCasting && internalTemperature < metalEntry.METAL.meltingPoint - 100 && inventory.getStackInSlot(0).isEmpty()) {
                    ItemStack output = TagItemOutput.fromTag(MetallurgicaTags.forgeItemTag("ingots/" + metalName)).get();
                    getTankInventory().drain(90, IFluidHandler.FluidAction.EXECUTE);
                    inventory.setStackInSlot(0, output);
                    break;
                }
            } else {
                hasCasting = false;
                internalTemperature = 20;
            }
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
        ClientUtil.temp().forGoggles(tooltip);
        ClientUtil.lang().add(ClientUtil.temperature(internalTemperature)).forGoggles(tooltip, 1);
        return containedFluidTooltip(tooltip, isPlayerSneaking,
                this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY));
    }
    
    
    public float getFillState() {
        return (float) tank.getPrimaryHandler().getFluidAmount() / tank.getPrimaryHandler().getCapacity();
    }
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        tank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.INPUT, this, 1, 90, true)
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
    public void invalidate() {
        super.invalidate();
        itemCapability.invalidate();
        fluidCapability.invalidate();
    }
    
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return itemCapability.cast();
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return fluidCapability.cast();
        return super.getCapability(cap, side);
    }
    public void notifyChangeOfContents() {
        contentsChanged = true;
    }
    
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        
        int prevLum = luminosity;
        luminosity = compound.getInt("Luminosity");
        shouldShatter = compound.getBoolean("ShouldShatter");
        inventory.deserializeNBT(compound.getCompound("Output"));
        internalTemperature = compound.getDouble("InternalTemperature");
        hasCasting = compound.getBoolean("HasCasting");
        
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
        compound.put("Output", inventory.serializeNBT());
        compound.putDouble("InternalTemperature", internalTemperature);
        compound.putBoolean("HasCasting", hasCasting);
        super.write(compound, clientPacket);
        if (!clientPacket)
            return;
        if (forceFluidLevelUpdate)
            compound.putBoolean("ForceFluidLevel", true);
        if (queuedSync)
            compound.putBoolean("LazySync", true);
        forceFluidLevelUpdate = false;
    }
    
    @Override
    public void notifyUpdate() {
        super.notifyUpdate();
    }
    
    public IFluidTank getTankInventory() {
        return tank.getPrimaryHandler();
    }
    
    public ItemStack getOutput() {
        return inventory.getStackInSlot(0);
    }
}