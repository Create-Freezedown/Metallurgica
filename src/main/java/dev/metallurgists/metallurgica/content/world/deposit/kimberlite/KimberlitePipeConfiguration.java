package dev.metallurgists.metallurgica.content.world.deposit.kimberlite;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class KimberlitePipeConfiguration implements FeatureConfiguration {

    public static final Codec<KimberlitePipeConfiguration> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                Codec.BOOL.fieldOf("tuff_ring").forGetter(config -> config.hasTuffRing),
                Codec.INT.fieldOf("top_radius").forGetter(config -> config.topRadius),
                Codec.INT.fieldOf("bottom_radius").forGetter(config -> config.bottomRadius)
        ).apply(instance, KimberlitePipeConfiguration::new);
    });

    public final boolean hasTuffRing;
    public final int topRadius;
    public final int bottomRadius;

    public KimberlitePipeConfiguration(boolean hasTuffRing, int topRadius, int bottomRadius) {
        this.hasTuffRing = hasTuffRing;
        this.topRadius = topRadius;
        this.bottomRadius = bottomRadius;
    }
}
