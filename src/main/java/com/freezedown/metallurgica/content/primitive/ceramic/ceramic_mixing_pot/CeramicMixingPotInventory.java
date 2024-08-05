package com.freezedown.metallurgica.content.primitive.ceramic.ceramic_mixing_pot;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.foundation.item.SmartInventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

public class CeramicMixingPotInventory extends SmartInventory {
    
    private CeramicMixingPotBlockEntity blockEntity;
    
    public CeramicMixingPotInventory(int slots, CeramicMixingPotBlockEntity be) {
        super(slots, be, 16, true);
        this.blockEntity = be;
    }
    
    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        // Only insert if no other slot already has a stack of this item
        for (int i = 0; i < getSlots(); i++)
            if (i != slot && ItemHandlerHelper.canItemStacksStack(stack, inv.getStackInSlot(i)))
                return stack;
        return super.insertItem(slot, stack, simulate);
    }
    
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack extractItem = super.extractItem(slot, amount, simulate);
        if (!simulate && !extractItem.isEmpty())
            blockEntity.notifyChangeOfContents();
        return extractItem;
    }
    
}
