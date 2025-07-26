package dev.metallurgists.metallurgica.foundation.worldgen.feature.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;

public class MagmaConduitFeatureConfig implements FeatureConfiguration {
    public static final Codec<MagmaConduitFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                BlockState.CODEC
                        .fieldOf("filler")
                        .forGetter(config -> config.filler),
                Codec.list(BlockState.CODEC)
                        .fieldOf("ore_pool")
                        .forGetter(config -> config.orePool),
                Codec.list(BlockState.CODEC)
                        .fieldOf("rich_ore_pool")
                        .forGetter(config -> config.richOrePool),
                Codec.intRange(-64, 256)
                        .fieldOf("max_y")
                        .forGetter(config -> config.maxY),
                Codec.intRange(-64, 256)
                        .fieldOf("min_y")
                        .forGetter(config -> config.minY)
        ).apply(instance, MagmaConduitFeatureConfig::new);
    });

    public final BlockState filler;
    public final List<BlockState> orePool;
    public final List<BlockState> richOrePool;
    public final int maxY;
    public final int minY;

    public MagmaConduitFeatureConfig(BlockState filler, List<BlockState> orePool, List<BlockState> richOrePool, int maxY, int minY) {
        this.filler = filler;
        this.orePool = orePool;
        this.richOrePool = richOrePool;
        this.maxY = maxY;
        this.minY = minY;
    }
}
