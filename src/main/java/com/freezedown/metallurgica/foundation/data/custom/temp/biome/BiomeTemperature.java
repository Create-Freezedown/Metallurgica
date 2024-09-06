package com.freezedown.metallurgica.foundation.data.custom.temp.biome;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import java.util.List;

public record BiomeTemperature(ResourceLocation biomeLoc, double surfaceTemperature, List<ResourceLocation> temperatureBlendBlacklist) {
    public static final Codec<BiomeTemperature> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("biome").forGetter(BiomeTemperature::getLoc),
            Codec.DOUBLE.fieldOf("surfaceTemperature").orElse(23.0).forGetter(BiomeTemperature::surfaceTemperature),
            ResourceLocation.CODEC.listOf().fieldOf("temperatureBlendBlacklist").orElse(List.of()).forGetter(BiomeTemperature::getTemperatureBlendBlacklist)
    ).apply(instance, BiomeTemperature::new));
    
    public Biome getBiome() {
        return fromKey(ResourceKey.create(BuiltinRegistries.BIOME.key(), biomeLoc)).get();
    }
    
    public double getSurfaceTemperature() {
        return surfaceTemperature;
    }
    public ResourceLocation getLoc() {
        return biomeLoc;
    }
    public List<ResourceLocation> getTemperatureBlendBlacklist() {
        return temperatureBlendBlacklist;
    }
    
    public Holder<Biome> fromKey(ResourceKey<Biome> key) {
        return HolderLookup.forRegistry(BuiltinRegistries.BIOME).get(key).isPresent() ? HolderLookup.forRegistry(BuiltinRegistries.BIOME).get(key).get() : null;
    }
    
    public static JsonElement serializeBlacklist(List<ResourceLocation> blacklist) {
        JsonArray array = new JsonArray();
        for (ResourceLocation loc : blacklist) {
            array.add(loc.toString());
        }
        return array;
    }
    
}
