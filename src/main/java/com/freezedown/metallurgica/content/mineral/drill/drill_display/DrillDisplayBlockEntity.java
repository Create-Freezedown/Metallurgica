package com.freezedown.metallurgica.content.mineral.drill.drill_display;

import com.freezedown.metallurgica.content.mineral.drill.drill_activator.DrillActivatorBlockEntity;
import com.freezedown.metallurgica.content.mineral.drill.drill_tower.DrillTowerBlockEntity;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;
import java.util.List;

import static com.freezedown.metallurgica.content.mineral.drill.drill_display.DrillDisplayBlock.getAttachedDirection;

public class DrillDisplayBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {
    public SmartInventory displayInventory = (new SmartInventory(1, this)).withMaxStackSize(4096);
    public LazyOptional<IItemHandlerModifiable> itemCapability = LazyOptional.of(() -> {
        return new CombinedInvWrapper(this.displayInventory);
    });
    public DrillDisplayBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.displayInventory.forbidInsertion();
    }
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }
    @Override
    public void tick() {
        super.tick();
        if (this.level == null) {
            return;
        }
        IItemHandlerModifiable items = itemCapability.orElse(new ItemStackHandler());
        if (this.level.getBlockEntity(worldPosition.relative(getAttachedDirection(this.getBlockState()))) instanceof DrillActivatorBlockEntity drillActivator) {
            ItemStack stack = drillActivator.itemHandler.getStackInSlot(0);
            if (!stack.isEmpty()) {
                if (items.getStackInSlot(0).getCount() < 4096 && items.getStackInSlot(0).getItem() == stack.getItem()) {
                    drillActivator.itemHandler.getStackInSlot(0).shrink(1);
                    items.getStackInSlot(0).grow(1);
                }
                if (items.getStackInSlot(0).isEmpty()) {
                    drillActivator.itemHandler.getStackInSlot(0).shrink(1);
                    items.setStackInSlot(0, stack.copy());
                }
            }
        }
    }
    
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        this.displayInventory.deserializeNBT(compound.getCompound("DrillDisplayItems"));
    }
    
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("DrillDisplayItems", this.displayInventory.serializeNBT());
    }
    public void invalidate() {
        super.invalidate();
        this.itemCapability.invalidate();
    }
    
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        return this.itemCapability.cast();
    }
    
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        Lang.translate("gui.goggles.drill_display").forGoggles(tooltip);
        IItemHandlerModifiable items = itemCapability.orElse(new ItemStackHandler());
        boolean isEmpty = true;
        
        for (int i = 0; i < items.getSlots(); i++) {
            ItemStack stackInSlot = items.getStackInSlot(i);
            if (stackInSlot.isEmpty())
                continue;
            Lang.text("")
                    .add(Components.translatable(stackInSlot.getDescriptionId())
                            .withStyle(ChatFormatting.GRAY))
                    .add(Lang.text(" x" + stackInSlot.getCount())
                            .style(ChatFormatting.GREEN))
                    .forGoggles(tooltip, 1);
            isEmpty = false;
        }
        if (isEmpty)
            tooltip.remove(0);
        
        return true;
    }
}
