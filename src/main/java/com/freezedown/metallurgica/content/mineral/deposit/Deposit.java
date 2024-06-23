package com.freezedown.metallurgica.content.mineral.deposit;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("deprecation")
public record Deposit(ItemStack mineralItem, float chance, float minimumEfficiency, BlockState replaceWith) {
    public static final Codec<ItemStack> SINGLE_ITEM_CODEC = RecordCodecBuilder.create((instance) -> instance.group(Registry.ITEM.byNameCodec().fieldOf("id").forGetter(ItemStack::getItem)).apply(instance, (item) -> new ItemStack(item, 1)));
    
    public static final Codec<Deposit> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SINGLE_ITEM_CODEC.fieldOf("mineralItem").forGetter(Deposit::mineralItem),
            Codec.FLOAT.optionalFieldOf("chance", 50.0f).forGetter(Deposit::chance),
            Codec.FLOAT.optionalFieldOf("minimumEfficiency", 1.0f).forGetter(Deposit::minimumEfficiency),
            BlockState.CODEC.optionalFieldOf("replaceWith", Blocks.STONE.defaultBlockState()).forGetter(Deposit::replaceWith)
    ).apply(instance, Deposit::new));
    
    public Deposit(ItemStack mineralItem, float chance, float minimumEfficiency, BlockState replaceWith) {
        this.mineralItem = mineralItem;
        this.chance = chance;
        this.minimumEfficiency = minimumEfficiency;
        this.replaceWith = replaceWith;
    }
    public ItemStack mineralItem() {
        return mineralItem;
    }
    
    public float chance() {
        return chance;
    }
    
    public float minimumEfficiency() {
        return minimumEfficiency;
    }
    
    public BlockState replaceWith() {
        return replaceWith;
    }
    
    
}
