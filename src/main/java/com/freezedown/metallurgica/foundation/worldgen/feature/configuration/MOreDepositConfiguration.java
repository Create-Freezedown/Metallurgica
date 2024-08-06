package com.freezedown.metallurgica.foundation.worldgen.feature.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record MOreDepositConfiguration(BlockState primaryStone, BlockState secondaryStone, BlockState mineralStone, BlockState deposit) implements FeatureConfiguration {
    public static final Codec<MOreDepositConfiguration> CODEC = RecordCodecBuilder.create((fields) -> {
        return fields.group(
            BlockState.CODEC.fieldOf("primaryStone").forGetter((v) -> {
            return v.primaryStone;
        }), BlockState.CODEC.fieldOf("secondaryStone").forGetter((v) -> {
            return v.secondaryStone;
        }), BlockState.CODEC.fieldOf("mineralStone").forGetter((v) -> {
            return v.mineralStone;
        }), BlockState.CODEC.fieldOf("deposit").forGetter((v) -> {
            return v.deposit;
        })).apply(fields, MOreDepositConfiguration::new);
    });
    
    public MOreDepositConfiguration(BlockState primaryStone, BlockState secondaryStone, BlockState mineralStone, BlockState deposit) {
        this.primaryStone = primaryStone;
        this.secondaryStone = secondaryStone;
        this.mineralStone = mineralStone;
        this.deposit = deposit;
    }
    
    public BlockState getPrimaryStone() {
        return primaryStone;
    }
    
    public BlockState getSecondaryStone() {
        return secondaryStone;
    }
    
    public BlockState getMineralStone() {
        return mineralStone;
    }
    
    public BlockState getDeposit() {
        return deposit;
    }
}
