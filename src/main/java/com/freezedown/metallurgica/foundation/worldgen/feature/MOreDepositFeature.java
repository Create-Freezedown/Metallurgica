package com.freezedown.metallurgica.foundation.worldgen.feature;

import com.freezedown.metallurgica.foundation.worldgen.feature.configuration.MOreDepositConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import javax.annotation.Nullable;

import static net.minecraft.world.level.block.Block.UPDATE_ALL;

public class MOreDepositFeature extends Feature<MOreDepositConfiguration> {
    
    public MOreDepositFeature(Codec<MOreDepositConfiguration> pCodec) {
        super(pCodec);
    }
    
    public boolean place(FeaturePlaceContext<MOreDepositConfiguration> pContext) {
        MOreDepositConfiguration depositConfiguration = pContext.config();
        RandomSource random = pContext.random();
        BlockPos origin = pContext.origin();
        WorldGenLevel worldGenLevel = pContext.level();
        BlockStateProvider decorativeBlock = depositConfiguration.getDecorativeBlock();
        BlockStateProvider outerBlock = depositConfiguration.getOuterBlock();
        BlockStateProvider innerBlock = depositConfiguration.getInnerBlock();
        BlockStateProvider depositBlock = depositConfiguration.getDepositBlock();
        Boolean underwater = depositConfiguration.isUnderwater();
        int depth = depositConfiguration.getDepth().sample(random);
        boolean suitableEnvironment = false;
        
        if (depth > 80 && underwater) {
            depth = 80;
        }
        
        if (underwater && worldGenLevel.getBlockState(origin.above(2)).is(Blocks.WATER)) {
            suitableEnvironment = true;
        } else if (!underwater && !worldGenLevel.getBlockState(origin.below()).is(Blocks.WATER)) {
            suitableEnvironment = true;
        }
        
        if (suitableEnvironment && worldGenLevel.getBlockState(origin.below()).getMaterial().isSolid()) {
            placePartialDiagonal(worldGenLevel, random, origin.above(), outerBlock, 3, 0.8, decorativeBlock);
            placeDiagonal(worldGenLevel, random, origin.above(), outerBlock, 3, 3, 0.66, decorativeBlock);
            placePartialDiagonal(worldGenLevel, random, origin, outerBlock, 3, 0.7, decorativeBlock);
            placeStraight(worldGenLevel, random, origin, outerBlock, 3, 3, 1, decorativeBlock);
            placeDiagonal(worldGenLevel, random, origin, outerBlock, 0, 3, 1.6, null);
            placeStraight(worldGenLevel, random, origin.below(), outerBlock, 3, 3, 1, null);
            placeDiagonal(worldGenLevel, random, origin.below(), outerBlock, 0, 3, 1.6, null);
            if (depth > 10) {
                for (int y = 1; y <= (depth / 2) + 1; y++) {
                    placeStraight(worldGenLevel, random, origin.below(y), outerBlock, 0, 2, 0.8, null);
                    placeDiagonal(worldGenLevel, random, origin.below(y), outerBlock, 0, 2, 0.75, null);
                    placeBlock(worldGenLevel, random, origin.below(y), depositBlock, 1, null);
                }
                
                for (int y = (depth / 2) + 2; y <= depth + 1; y++) {
                    placeStraight(worldGenLevel, random, origin.below(y), outerBlock, 0, 1, 0.33, null);
                    placeDiagonal(worldGenLevel, random, origin.below(y), outerBlock, 0, 1, 0.275, null);
                    placeBlock(worldGenLevel, random, origin.below(y), depositBlock, 1, null);
                }
            }
            if (underwater) {
                worldGenLevel.setBlock(origin, Blocks.WATER.defaultBlockState(), UPDATE_ALL);
            } else {
                worldGenLevel.setBlock(origin, Blocks.AIR.defaultBlockState(), UPDATE_ALL);
            }
            worldGenLevel.setBlock(origin.below(), innerBlock.getState(random, origin.below()), UPDATE_ALL);
            return true;
        } else {
            return false;
        }
    }
    
    private void placeStraight(WorldGenLevel worldGenLevel, RandomSource random, BlockPos blockPos, BlockStateProvider blockStateProvider, int minRadius, int maxRadius, double probability, @Nullable BlockStateProvider decorate) {
        minRadius--;
        for (int radius = minRadius; radius < maxRadius; radius++) {
            placeBlock(worldGenLevel, random, blockPos.north(radius), blockStateProvider, probability, decorate);
            placeBlock(worldGenLevel, random, blockPos.east(radius), blockStateProvider, probability, decorate);
            placeBlock(worldGenLevel, random, blockPos.south(radius), blockStateProvider, probability, decorate);
            placeBlock(worldGenLevel, random, blockPos.west(radius), blockStateProvider, probability, decorate);
        }
    }
    private void placeDiagonal(WorldGenLevel worldGenLevel, RandomSource random, BlockPos blockPos, BlockStateProvider blockStateProvider, int minRadius, int maxRadius, double probability, @Nullable BlockStateProvider decorate) {
        minRadius--;
        for (int radius = minRadius; radius < maxRadius; radius++) {
            placeBlock(worldGenLevel, random, blockPos.north(radius).east(), blockStateProvider, probability, decorate);
            placeBlock(worldGenLevel, random, blockPos.north(radius).west(), blockStateProvider, probability, decorate);
            placeBlock(worldGenLevel, random, blockPos.south(radius).west(), blockStateProvider, probability, decorate);
            placeBlock(worldGenLevel, random, blockPos.south(radius).east(), blockStateProvider, probability, decorate);
            placeBlock(worldGenLevel, random, blockPos.north().east(radius), blockStateProvider, probability, decorate);
            placeBlock(worldGenLevel, random, blockPos.north().west(radius), blockStateProvider, probability, decorate);
            placeBlock(worldGenLevel, random, blockPos.south().west(radius), blockStateProvider, probability, decorate);
            placeBlock(worldGenLevel, random, blockPos.south().east(radius), blockStateProvider, probability, decorate);
            placeBlock(worldGenLevel, random, blockPos.north(radius).east(radius), blockStateProvider, probability, decorate);
            placeBlock(worldGenLevel, random, blockPos.north(radius).west(radius), blockStateProvider, probability, decorate);
            placeBlock(worldGenLevel, random, blockPos.south(radius).west(radius), blockStateProvider, probability, decorate);
            placeBlock(worldGenLevel, random, blockPos.south(radius).east(radius), blockStateProvider, probability, decorate);
            placeBlock(worldGenLevel, random, blockPos.north(radius).east(radius), blockStateProvider, probability, decorate);
            placeBlock(worldGenLevel, random, blockPos.north(radius).west(radius), blockStateProvider, probability, decorate);
            placeBlock(worldGenLevel, random, blockPos.south(radius).west(radius), blockStateProvider, probability, decorate);
            placeBlock(worldGenLevel, random, blockPos.south(radius).east(radius), blockStateProvider, probability, decorate);
        }
    }
    private void placePartialDiagonal(WorldGenLevel worldGenLevel, RandomSource random, BlockPos blockPos, BlockStateProvider blockStateProvider, int radius, double probability, @Nullable BlockStateProvider decorate) {
        placeBlock(worldGenLevel, random, blockPos.north(radius).east(), blockStateProvider, probability, decorate);
        placeBlock(worldGenLevel, random, blockPos.north(radius).west(), blockStateProvider, probability, decorate);
        placeBlock(worldGenLevel, random, blockPos.south(radius).west(), blockStateProvider, probability, decorate);
        placeBlock(worldGenLevel, random, blockPos.south(radius).east(), blockStateProvider, probability, decorate);
        placeBlock(worldGenLevel, random, blockPos.north().east(radius), blockStateProvider, probability, decorate);
        placeBlock(worldGenLevel, random, blockPos.north().west(radius), blockStateProvider, probability, decorate);
        placeBlock(worldGenLevel, random, blockPos.south().west(radius), blockStateProvider, probability, decorate);
        placeBlock(worldGenLevel, random, blockPos.south().east(radius), blockStateProvider, probability, decorate);
    }
    private void placeBlock(WorldGenLevel worldGenLevel, RandomSource random, BlockPos blockPos, BlockStateProvider blockStateProvider, double probability, @Nullable BlockStateProvider decorate) {
        boolean passedProbability = false;
        if (probability >= 1) {
            passedProbability = true;
        } else {
            int randomNumber = (int)(Math.random()*((100))+1);
            if (randomNumber < probability*100) {
                passedProbability = true;
            }
        }
        if (passedProbability) {
            BlockState blockState = blockStateProvider.getState(random, blockPos);
            worldGenLevel.setBlock(blockPos, blockState, UPDATE_ALL);
            if (decorate != null) {
                boolean passedDecorateProbability = false;
                double decorateProbability = probability / 3;
                if (decorateProbability >= 1) {
                    passedDecorateProbability = true;
                } else {
                    int randomNumber2 = (int) (Math.random() * ((100)) + 1);
                    if (randomNumber2 < decorateProbability * 100) {
                        passedDecorateProbability = true;
                    }
                }
                if (passedDecorateProbability) {
                    BlockPos offsetPos = randomOffset(blockPos);
                    BlockState decorateBlockState = decorate.getState(random, offsetPos);
                    worldGenLevel.setBlock(offsetPos, decorateBlockState, UPDATE_ALL);
                    if (worldGenLevel.getBlockState(offsetPos.below()).isAir()) {
                        worldGenLevel.setBlock(offsetPos.below(), blockState, UPDATE_ALL);
                    }
                }
            }
        }
    }
    
    private BlockPos randomOffset(BlockPos blockPos) {
        int randomNumber = (int)(Math.random()*((15))+1);
        if (randomNumber <= 7) {
            return blockPos.above();
        } else if (randomNumber == 8) {
            return blockPos.north();
        } else if (randomNumber == 9) {
            return blockPos.east();
        } else if (randomNumber == 10) {
            return blockPos.south();
        } else if (randomNumber == 11) {
            return blockPos.west();
        } else if (randomNumber == 12) {
            return blockPos.north().east();
        } else if (randomNumber == 13) {
            return blockPos.north().west();
        } else if (randomNumber == 14) {
            return blockPos.south().east();
        } else {
            return blockPos.south().west();
        }
    }
}
