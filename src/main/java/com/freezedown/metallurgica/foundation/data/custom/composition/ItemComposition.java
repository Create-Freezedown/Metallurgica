package com.freezedown.metallurgica.foundation.data.custom.composition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

import java.util.List;

@SuppressWarnings("deprecation")
public record ItemComposition(Item item, List<Element> elements) {
    public static final Codec<ItemComposition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(ItemComposition::item),
            Codec.list(Element.CODEC).fieldOf("elements").forGetter(ItemComposition::elements)
    ).apply(instance, ItemComposition::new));
}
