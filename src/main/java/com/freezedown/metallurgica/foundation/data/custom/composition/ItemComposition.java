package com.freezedown.metallurgica.foundation.data.custom.composition;

import com.freezedown.metallurgica.infastructure.element.data.SubComposition;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

import java.util.List;

@SuppressWarnings("deprecation")
public record ItemComposition(Item item, List<SubComposition> compositions) {
    public static final Codec<ItemComposition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(ItemComposition::item),
            Codec.list(SubComposition.CODEC).fieldOf("compositions").forGetter(ItemComposition::compositions)
    ).apply(instance, ItemComposition::new));

    public JsonObject toJson() {
        JsonObject json = new JsonObject();

        return json;
    }
}
