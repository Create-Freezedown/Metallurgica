package com.freezedown.metallurgica.foundation.worldgen.feature.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record MOreDepositConfiguration(BlockStateProvider decorativeBlock, BlockStateProvider outerBlock, BlockStateProvider innerBlock, BlockStateProvider depositBlock, IntProvider depth, Boolean underwater) implements FeatureConfiguration {
    public static final Codec<MOreDepositConfiguration> CODEC = RecordCodecBuilder.create((fields) -> {
        return fields.group(BlockStateProvider.CODEC.fieldOf("decorativeBlock").forGetter((v) -> {
            return v.decorativeBlock;
        }), BlockStateProvider.CODEC.fieldOf("outerBlock").forGetter((v) -> {
            return v.outerBlock;
        }), BlockStateProvider.CODEC.fieldOf("innerBlock").forGetter((v) -> {
            return v.innerBlock;
        }), BlockStateProvider.CODEC.fieldOf("liquidBlock").forGetter((v) -> {
            return v.depositBlock;
        }), IntProvider.codec(10, 255).fieldOf("depth").forGetter((v) -> {
            return v.depth;
        }), Codec.BOOL.fieldOf("underwater").orElse(false).forGetter((v) -> {
            return v.underwater;
        })).apply(fields, MOreDepositConfiguration::new);
    });
    
    public MOreDepositConfiguration(BlockStateProvider decorativeBlock, BlockStateProvider outerBlock, BlockStateProvider innerBlock, BlockStateProvider depositBlock, IntProvider depth, Boolean underwater) {
        this.decorativeBlock = decorativeBlock;
        this.outerBlock = outerBlock;
        this.innerBlock = innerBlock;
        this.depositBlock = depositBlock;
        this.depth = depth;
        this.underwater = underwater;
    }
    
    public BlockStateProvider getDecorativeBlock() {
        return decorativeBlock;
    }
    public BlockStateProvider getOuterBlock() {
        return this.outerBlock;
    }
    public BlockStateProvider getInnerBlock() {
        return this.innerBlock;
    }
    public BlockStateProvider getDepositBlock() {
        return this.depositBlock;
    }
    public IntProvider getDepth() {
        return this.depth;
    }
    public Boolean isUnderwater() {
        return this.underwater;
    }
}
