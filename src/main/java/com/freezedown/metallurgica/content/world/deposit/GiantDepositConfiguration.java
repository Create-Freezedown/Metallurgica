package com.freezedown.metallurgica.content.world.deposit;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class GiantDepositConfiguration implements FeatureConfiguration {

    public static final Codec<GiantDepositConfiguration> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                Codec.INT.fieldOf("below_surface_depth").forGetter(config -> config.belowSurfaceDepth),
                Codec.INT.fieldOf("above_bedrock_depth").forGetter(config -> config.aboveBedrockDepth)
        ).apply(instance, GiantDepositConfiguration::new);
    });

    public final int belowSurfaceDepth;
    public final int aboveBedrockDepth;

    public GiantDepositConfiguration(int belowSurfaceDepth, int aboveBedrockDepth) {
        this.belowSurfaceDepth = belowSurfaceDepth;
        this.aboveBedrockDepth = aboveBedrockDepth;

    }
}
