package com.freezedown.metallurgica.foundation.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.infrastructure.worldgen.LayerPattern;

import java.util.List;

public class MConfigDrivenLayeredOreFeatureConfiguration extends MBaseConfigDrivenOreFeatureConfiguration {
    public static final Codec<MConfigDrivenLayeredOreFeatureConfiguration> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                MOreFeatureConfigEntry.CODEC
                        .fieldOf("entry")
                        .forGetter(config -> config.entry),
                Codec.floatRange(0.0F, 1.0F)
                        .fieldOf("discard_chance_on_air_exposure")
                        .forGetter(config -> config.discardChanceOnAirExposure),
                Codec.list(LayerPattern.CODEC)
                        .fieldOf("layer_patterns")
                        .forGetter(config -> config.layerPatterns)
        ).apply(instance, MConfigDrivenLayeredOreFeatureConfiguration::new);
    });
    
    private final List<LayerPattern> layerPatterns;
    
    public MConfigDrivenLayeredOreFeatureConfiguration(MOreFeatureConfigEntry entry, float discardChance, List<LayerPattern> layerPatterns) {
        super(entry, discardChance);
        this.layerPatterns = layerPatterns;
    }
    
    public List<LayerPattern> getLayerPatterns() {
        return layerPatterns;
    }
}
