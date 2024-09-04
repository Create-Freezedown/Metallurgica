/*
package com.freezedown.metallurgica.foundation.worldgen.feature.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record MSandDepositConfiguration(BlockState primarySand, BlockState secondarySand) implements FeatureConfiguration {
    public static final Codec<MSandDepositConfiguration> CODEC = RecordCodecBuilder.create((fields) -> {
        return fields.group(
                BlockState.CODEC.fieldOf("primarySand").forGetter((v) -> {
                    return v.primarySand;
                }), BlockState.CODEC.fieldOf("secondarySand").forGetter((v) -> {
                    return v.secondarySand;
                })).apply(fields, MSandDepositConfiguration::new);
    });


    public MSandDepositConfiguration(BlockState primarySand, BlockState secondarySand) {
        this.primarySand = primarySand;
        this.secondarySand = secondarySand;
    }

    public BlockState getPrimarySand() {
        return primarySand;
    }

    public BlockState getSecondarySand() {
        return secondarySand;
    }
}
*/
