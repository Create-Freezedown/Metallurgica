package com.freezedown.metallurgica.foundation.material.block;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MaterialBlockItem extends BlockItem {
    protected MaterialBlockItem(MaterialBlock block, Properties properties) {
        super(block, properties);
    }

    public static MaterialBlockItem create(MaterialBlock block, Item.Properties properties) {
        return new MaterialBlockItem(block, properties);
    }

    @Override
    @NotNull
    public MaterialBlock getBlock() {
        return (MaterialBlock) super.getBlock();
    }

    @Override
    public String getDescriptionId() {
        return getBlock().getDescriptionId();
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        return getDescriptionId();
    }

    @Override
    public Component getDescription() {
        return getBlock().getName();
    }

    @Override
    public Component getName(ItemStack stack) {
        return getDescription();
    }

}
