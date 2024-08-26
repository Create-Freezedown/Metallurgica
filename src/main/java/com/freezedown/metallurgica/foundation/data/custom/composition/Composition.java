package com.freezedown.metallurgica.foundation.data.custom.composition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;

import java.util.List;

public record Composition(Item item, List<Element> elements) {
    public static final Codec<Composition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Registry.ITEM.byNameCodec().fieldOf("item").forGetter(Composition::item),
            Codec.list(Element.CODEC).fieldOf("elements").forGetter(Composition::elements)
    ).apply(instance, Composition::new));
}
