package com.freezedown.metallurgica.infastructure.loot_modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.world.item.ItemStack;


public class ItemStackPair {
    private final ItemStack input;
    private final ItemStack output;
    
    public ItemStackPair(ItemStack input, ItemStack output) {
        this.input = input;
        this.output = output;
    }
    
    public ItemStack getInput() {
        return input;
    }
    
    public ItemStack getOutput() {
        return output;
    }
    
    @SuppressWarnings("deprecation")
    public static final Codec<ItemStack> CODEC_NO_COUNT = RecordCodecBuilder.create((instance) -> instance.group(Registry.ITEM.byNameCodec().fieldOf("id").forGetter(ItemStack::getItem)).apply(instance, ItemStack::new));
    
    public static final Codec<ItemStackPair> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CODEC_NO_COUNT.fieldOf("input").forGetter(ItemStackPair::getInput),
            CODEC_NO_COUNT.fieldOf("output").forGetter(ItemStackPair::getOutput)
    ).apply(instance, ItemStackPair::new));
    
    
}
