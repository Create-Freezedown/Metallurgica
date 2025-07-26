package dev.metallurgists.metallurgica.foundation.worldgen.feature.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record CrystalFeatureConfig(Type type, BlockState crystalBlock) implements FeatureConfiguration {
    public static final Codec<CrystalFeatureConfig> CODEC = RecordCodecBuilder.create((fields) -> {
        return fields.group(
                Type.CODEC.fieldOf("type").forGetter((v) -> {
                    return v.type;
                }),
                BlockState.CODEC.fieldOf("crystalBlock").forGetter((v) -> {
                    return v.crystalBlock;
                })
                ).apply(fields, CrystalFeatureConfig::new);
    });

    public enum Type {
        CUBIC,
        HEXAGONAL,
        TETRAGONAL,
        ORTHORHOMBIC,
        MONOCLINIC,
        TRICLINIC,
        AMORPHOUS

        ;

        static final PrimitiveCodec<Type> CODEC = new PrimitiveCodec<Type>() {

            @Override
            public <T> DataResult<Type> read(DynamicOps<T> dynamicOps, T t) {
                return dynamicOps.getStringValue(t).map(Type::valueOf);
            }

            @Override
            public <T> T write(DynamicOps<T> dynamicOps, Type type) {
                return dynamicOps.createString(type.toString());
            }

            public String toString() {
                return "CrystalType";
            }
        };
    }
}
