package com.freezedown.metallurgica.foundation.data.custom.temp;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public record BiomeTemperature(ResourceLocation biomeLoc, double surfaceTemperature) {
    public static final Codec<BiomeTemperature> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("biome").forGetter(BiomeTemperature::getLoc),
            Codec.DOUBLE.fieldOf("surfaceTemperature").orElse(23.0).forGetter(BiomeTemperature::surfaceTemperature)
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
    
    public Holder<Biome> fromKey(ResourceKey<Biome> key) {
        return HolderLookup.forRegistry(BuiltinRegistries.BIOME).get(key).isPresent() ? HolderLookup.forRegistry(BuiltinRegistries.BIOME).get(key).get() : null;
    }
}
