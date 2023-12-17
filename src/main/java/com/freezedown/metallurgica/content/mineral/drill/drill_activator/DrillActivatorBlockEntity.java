package com.freezedown.metallurgica.content.mineral.drill.drill_activator;

import com.freezedown.metallurgica.content.mineral.deposit.DepositTypes;
import com.freezedown.metallurgica.content.mineral.deposit.MineralDepositBlockEntity;
import com.freezedown.metallurgica.content.mineral.drill.drill_tower.DrillTowerBlock;
import com.freezedown.metallurgica.content.mineral.drill.drill_tower.DrillTowerBlockEntity;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.VersionedInventoryTrackerBehaviour;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;

import static com.freezedown.metallurgica.content.mineral.drill.drill_display.DrillDisplayBlock.getAttachedDirection;

@SuppressWarnings("removal")
public class DrillActivatorBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation {
    public ItemStack item;
    public DrillActivatorItemHandler itemHandler;
    public LazyOptional<IItemHandler> lazyHandler;
    public VersionedInventoryTrackerBehaviour invVersionTracker;
    public BlockPos publicPosition = this.getBlockPos();
    
    public DrillActivatorBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        item = ItemStack.EMPTY;
        itemHandler = new DrillActivatorItemHandler(this);
        lazyHandler = LazyOptional.of(() -> itemHandler);
    }
    public void setItem(ItemStack stack) {
        item = stack;
        invVersionTracker.reset();
        if (!level.isClientSide) {
            notifyUpdate();
            award(AllAdvancements.CHUTE);
        }
    }
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(invVersionTracker = new VersionedInventoryTrackerBehaviour(this));
    }
    protected boolean canAcceptItem(ItemStack stack) {
        return item.isEmpty();
    }
    public int height;
    public float efficiency;
    public boolean canActivate;
    public BlockPos depositPos;
    @Override
    public void tick() {
        super.tick();
        if (this.level == null) {
            return;
        }
        if (!(this.level.getBlockEntity(this.worldPosition.below()) instanceof DrillTowerBlockEntity)) {
            canActivate = false;
        }
        if (this.level.getBlockEntity(this.worldPosition.below()) instanceof DrillTowerBlockEntity drillTower) {
            height = drillTower.height;
            depositPos = drillTower.depositPos;
        }
        efficiency = (float) (1.1 * (height + 1) / 5);
        if (canActivate) {
            drillDeposit();
        }
        checkDrillsAndActivate();
    }
    private void checkDrillsAndActivate() {
        if (this.level == null) {
            return;
        }
        int supposedHeight = 0;
        for (int i = 0; i < height; i++) {
            if (this.level.getBlockState(worldPosition.below(i)).getBlock() instanceof DrillTowerBlock) {
                supposedHeight++;
            }
        }
        if (supposedHeight == height) {
            canActivate = true;
        } else {
            canActivate = false;
        }
    }
    public void drillDeposit() {
        if (this.level == null) {
            return;
        }
        if (this.depositPos == null) {
            return;
        }
        DepositTypes deposit = depositType(this.level.getBlockState(depositPos).getBlock());
        if (this.level.getBlockState(depositPos).is(deposit.getDepositBlock()) && efficiency >= deposit.getMinimumEfficiency() && this.level.getBlockEntity(depositPos) instanceof MineralDepositBlockEntity mineralDeposit) {
            if (this.level.random.nextFloat() <= (deposit.getChance()  / 100)) {
                mineralDeposit.mineralAmount--;
                if (itemHandler.getStackInSlot(0).getCount() < 64 && item.getItem() == deposit.getMineralItem()) {
                    item.grow(1);
                } else if (item.isEmpty() || item.getItem() == Items.AIR) {
                    item = new ItemStack(deposit.getMineralItem());
                }
            }
        }
    }
    
    public DepositTypes depositType(Block block) {
        return DepositTypes.getDepositTypeFromBlock(block);
    }
    
    
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        item = ItemStack.of(compound.getCompound("Item"));
        if (compound.contains("depositX")) {
            depositPos = new BlockPos(compound.getInt("depositX"), compound.getInt("depositY"), compound.getInt("depositZ"));
        }
        height = compound.getInt("height");
        efficiency = compound.getFloat("efficiency");
        canActivate = compound.getBoolean("canActivate");
    }
    
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("Item", item.serializeNBT());
        if (depositPos != null) {
            compound.putInt("depositX", depositPos.getX());
            compound.putInt("depositY", depositPos.getY());
            compound.putInt("depositZ", depositPos.getZ());
        }
        compound.putInt("height", height);
        compound.putFloat("efficiency", efficiency);
        compound.putBoolean("canActivate", canActivate);
    }
    
    @Override
    public void invalidate() {
        if (lazyHandler != null)
            lazyHandler.invalidate();
        super.invalidate();
    }
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return lazyHandler.cast();
        return super.getCapability(cap, side);
    }
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (this.level == null) {
            return false;
        }
        if (this.depositPos == null) {
            return false;
        }
        DepositTypes deposit = depositType(this.level.getBlockState(depositPos).getBlock());
        Lang.translate("gui.goggles.drill_activator").forGoggles(tooltip);
        if (efficiency < deposit.getMinimumEfficiency()) {
            Lang.translate("goggles.drill_activator.insufficient_efficiency").style(ChatFormatting.DARK_RED).forGoggles(tooltip);
        } else {
            Lang.translate("goggles.drill_activator.height", height).style(ChatFormatting.DARK_GREEN).forGoggles(tooltip);
            Lang.translate("goggles.drill_activator.efficiency", efficiency).style(ChatFormatting.DARK_GREEN).forGoggles(tooltip);
        }
        return true;
    }
}
