package com.freezedown.metallurgica.foundation.material.block;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class MaterialBlockItem extends BlockItem {
    protected MaterialBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    public static MaterialBlockItem create(Block block, Item.Properties properties) {
        if (!(block instanceof IMaterialBlock)) throw  new IllegalArgumentException("Block must implement IMaterialBlock");
        return new MaterialBlockItem(block, properties);
    }

    @Override
    @NotNull
    public Block getBlock() {
        return super.getBlock();
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
