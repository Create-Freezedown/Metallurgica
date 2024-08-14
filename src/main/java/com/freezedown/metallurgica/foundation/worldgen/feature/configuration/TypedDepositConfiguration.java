package com.freezedown.metallurgica.foundation.worldgen.feature.configuration;

import com.freezedown.metallurgica.foundation.worldgen.feature.deposit.DepositCapacity;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record TypedDepositConfiguration(int maxWidth, int minWidth, int maxDepth, int minDepth, float depositBlockChance, DepositCapacity capacity, BlockState primaryStone, BlockState secondaryStone, BlockState mineralStone, BlockState deposit) implements FeatureConfiguration {
    public static final Codec<TypedDepositConfiguration> CODEC = RecordCodecBuilder.create((fields) -> {
        return fields.group(
                Codec.INT.fieldOf("maxWidth").forGetter(TypedDepositConfiguration::maxWidth),
                Codec.INT.fieldOf("minWidth").forGetter(TypedDepositConfiguration::minWidth),
                Codec.INT.fieldOf("maxDepth").forGetter(TypedDepositConfiguration::maxDepth),
                Codec.INT.fieldOf("minDepth").forGetter(TypedDepositConfiguration::minDepth),
                Codec.FLOAT.fieldOf("depositBlockChance").forGetter(TypedDepositConfiguration::depositBlockChance),
                DepositCapacity.CODEC.fieldOf("capacity").forGetter(TypedDepositConfiguration::capacity),
                BlockState.CODEC.fieldOf("primaryStone").forGetter(TypedDepositConfiguration::primaryStone),
                BlockState.CODEC.fieldOf("secondaryStone").forGetter(TypedDepositConfiguration::secondaryStone),
                BlockState.CODEC.fieldOf("mineralStone").forGetter(TypedDepositConfiguration::mineralStone),
                BlockState.CODEC.fieldOf("deposit").forGetter(TypedDepositConfiguration::deposit)
        ).apply(fields, TypedDepositConfiguration::new);
    });
    
    public TypedDepositConfiguration(int maxWidth, int minWidth, int maxDepth, int minDepth, float depositBlockChance, DepositCapacity capacity, BlockState primaryStone, BlockState secondaryStone, BlockState mineralStone, BlockState deposit) {
        this.maxWidth = maxWidth;
        this.minWidth = minWidth;
        this.maxDepth = maxDepth;
        this.minDepth = minDepth;
        this.depositBlockChance = depositBlockChance;
        this.capacity = capacity;
        this.primaryStone = primaryStone;
        this.secondaryStone = secondaryStone;
        this.mineralStone = mineralStone;
        this.deposit = deposit;
    }
    
    public int getMaxWidth() {
        return maxWidth;
    }
    
    public int getMinWidth() {
        return minWidth;
    }
    
    public int getMaxDepth() {
        return maxDepth;
    }
    
    public int getMinDepth() {
        return minDepth;
    }
    
    public float getDepositBlockChance() {
        return depositBlockChance;
    }
    
    public DepositCapacity getCapacity() {
        return capacity;
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
