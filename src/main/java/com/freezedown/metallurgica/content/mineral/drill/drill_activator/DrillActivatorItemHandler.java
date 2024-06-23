package com.freezedown.metallurgica.content.mineral.drill.drill_activator;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class DrillActivatorItemHandler implements IItemHandler {
    
    private DrillActivatorBlockEntity blockEntity;
    
    public DrillActivatorItemHandler(DrillActivatorBlockEntity be) {
        this.blockEntity = be;
    }
    
    @Override
    public int getSlots() {
        return 1;
    }
    
    @Override
    public ItemStack getStackInSlot(int slot) {
        return blockEntity.item;
    }
    
    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (!blockEntity.canAcceptItem())
            return stack;
        if (!simulate)
            blockEntity.setItem(stack);
        return ItemStack.EMPTY;
    }
    
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack remainder = blockEntity.item.copy();
        ItemStack split = remainder.split(amount);
        if (!simulate)
            blockEntity.setItem(remainder);
        return split;
    }
    
    @Override
    public int getSlotLimit(int slot) {
        return Math.min(64, getStackInSlot(slot).getMaxStackSize());
    }
    
    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return true;
    }
}
