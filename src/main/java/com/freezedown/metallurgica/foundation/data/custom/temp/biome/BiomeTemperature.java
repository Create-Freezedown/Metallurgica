package com.freezedown.metallurgica.foundation.data.custom.temp.biome;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public record BiomeTemperature(@Getter ResourceLocation biomeLoc, @Getter double surfaceTemperature, @Getter List<ResourceLocation> temperatureBlendBlacklist) {
    public static final Codec<BiomeTemperature> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("biome").forGetter(BiomeTemperature::getBiomeLoc),
            Codec.DOUBLE.fieldOf("surfaceTemperature").orElse(23.0).forGetter(BiomeTemperature::surfaceTemperature),
            ResourceLocation.CODEC.listOf().fieldOf("temperatureBlendBlacklist").orElse(List.of()).forGetter(BiomeTemperature::getTemperatureBlendBlacklist)
    ).apply(instance, BiomeTemperature::new));
    
    public Biome getBiome() {
        return fromKey(ResourceKey.create(ForgeRegistries.Keys.BIOMES, biomeLoc)).get();
    }

    public Holder<Biome> fromKey(ResourceKey<Biome> key) {
        return ForgeRegistries.BIOMES.getDelegate(key).isPresent() ? ForgeRegistries.BIOMES.getDelegate(key).get() : null;
    }
    
    public static JsonElement serializeBlacklist(List<ResourceLocation> blacklist) {
        JsonArray array = new JsonArray();
        for (ResourceLocation loc : blacklist) {
            array.add(loc.toString());
        }
        return array;
    }
    
}
