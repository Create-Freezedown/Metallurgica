package com.freezedown.metallurgica.foundation.worldgen.feature.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record LakeConfiguration(int size) implements FeatureConfiguration {
    public static final Codec<LakeConfiguration> CODEC = RecordCodecBuilder.create((fields) -> {
        return fields.group(
            Codec.INT.fieldOf("size").forGetter((v) -> {
                return v.size;
            })).apply(fields, LakeConfiguration::new);
    });
}
