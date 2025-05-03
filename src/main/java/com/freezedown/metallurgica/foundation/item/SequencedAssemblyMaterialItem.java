package com.freezedown.metallurgica.foundation.item;

import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.ItemFlag;
import net.createmod.catnip.theme.Color;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class SequencedAssemblyMaterialItem extends MaterialItem {

    public SequencedAssemblyMaterialItem(Properties properties, Material material, ItemFlag itemFlag) {
        super(properties.stacksTo(1), material, itemFlag);
    }

    public float getProgress(ItemStack stack) {
        if (!stack.hasTag())
            return 0;
        CompoundTag tag = stack.getTag();
        if (!tag.contains("SequencedAssembly"))
            return 0;
        return tag.getCompound("SequencedAssembly")
                .getFloat("Progress");
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(getProgress(stack) * 13);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return Color.mixColors(0xFF_FFC074, 0xFF_46FFE0, getProgress(stack));
    }
}
