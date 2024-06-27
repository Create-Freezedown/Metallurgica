package com.freezedown.metallurgica.foundation.worldgen.feature.configuration;

import com.freezedown.metallurgica.foundation.worldgen.MBaseConfigDrivenOreFeatureConfiguration;
import com.freezedown.metallurgica.foundation.worldgen.MOreFeatureConfigEntry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

import java.util.List;

public class MConfigDrivenOreFeatureConfiguration extends MBaseConfigDrivenOreFeatureConfiguration {
    public static final Codec<MConfigDrivenOreFeatureConfiguration> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                MOreFeatureConfigEntry.CODEC
                        .fieldOf("entry")
                        .forGetter(config -> config.entry),
                Codec.floatRange(0.0F, 1.0F)
                        .fieldOf("discard_chance_on_air_exposure")
                        .forGetter(config -> config.discardChanceOnAirExposure),
                Codec.list(OreConfiguration.TargetBlockState.CODEC)
                        .fieldOf("targets")
                        .forGetter(config -> config.targetStates)
        ).apply(instance, MConfigDrivenOreFeatureConfiguration::new);
    });
    
    private final List<OreConfiguration.TargetBlockState> targetStates;
    
    public MConfigDrivenOreFeatureConfiguration(MOreFeatureConfigEntry entry, float discardChance, List<OreConfiguration.TargetBlockState> targetStates) {
        super(entry, discardChance);
        this.targetStates = targetStates;
    }
    
    public List<OreConfiguration.TargetBlockState> getTargetStates() {
        return targetStates;
    }
}
