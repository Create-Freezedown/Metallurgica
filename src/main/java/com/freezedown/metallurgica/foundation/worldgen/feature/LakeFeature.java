package com.freezedown.metallurgica.foundation.worldgen.feature;

import com.freezedown.metallurgica.foundation.worldgen.feature.configuration.LakeConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;

public class LakeFeature extends Feature<LakeConfiguration> {

    public LakeFeature() {
        super(LakeConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext featurePlaceContext) {
        int lakeRadius = featurePlaceContext.random().nextInt(10) + 15; // Randomize lake size (15 to 25 block radius)
        int waterLevel = getWaterLevel(featurePlaceContext.level().getLevel(), featurePlaceContext.origin(), lakeRadius);

        generateLake(featurePlaceContext.level().getLevel(), featurePlaceContext.origin(), lakeRadius, waterLevel, featurePlaceContext.random());
        return true;
    }

    private int getWaterLevel(Level world, BlockPos pos, int radius) {
        // Sample the terrain height around the proposed lake center
        int totalHeight = 0;
        int sampleCount = 0;

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                BlockPos samplePos = pos.offset(x, 0, z);
                totalHeight += world.getHeight(Heightmap.Types.WORLD_SURFACE, samplePos.getX(), samplePos.getZ());
                sampleCount++;
            }
        }

        // Return average terrain height as water level
        return totalHeight / sampleCount;
    }

    private void generateLake(Level world, BlockPos pos, int radius, int waterLevel, RandomSource random) {
        SimplexNoise lakeShapeNoise = new SimplexNoise(random); // Use noise for natural lake shape

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                double distanceFromCenter = Math.sqrt(x * x + z * z);

                // Check if the point is within the lake's radius and shaped by noise
                if (distanceFromCenter <= radius && lakeShapeNoise.getValue(x * 0.1, z * 0.1) > 0.4) {
                    int terrainHeight = world.getHeight(Heightmap.Types.WORLD_SURFACE, pos.getX() + x, pos.getZ() + z);
                    BlockPos lakePos = new BlockPos(pos.getX() + x, terrainHeight, pos.getZ() + z);

                    // Carve out terrain to match the water level
                    if (terrainHeight <= waterLevel) {
                        carveLakeBed(world, lakePos, waterLevel);
                    }
                }
            }
        }

        // Fill with water
        fillWithWater(world, pos, radius, waterLevel);

        // Add sand and gravel patches
        addShorelinePatches(world, pos, radius, waterLevel, random);
    }

    private void carveLakeBed(Level world, BlockPos lakePos, int waterLevel) {
        for (int y = lakePos.getY(); y < waterLevel; y++) {
            BlockPos bedPos = new BlockPos(lakePos.getX(), y, lakePos.getZ());
            world.setBlock(bedPos, Blocks.AIR.defaultBlockState(), 3); // Carve out lake bed
        }
    }

    private void fillWithWater(Level world, BlockPos pos, int radius, int waterLevel) {
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                double distanceFromCenter = Math.sqrt(x * x + z * z);

                if (distanceFromCenter <= radius) {
                    int terrainHeight = world.getHeight(Heightmap.Types.WORLD_SURFACE, pos.getX() + x, pos.getZ() + z);
                    BlockPos waterPos = new BlockPos(pos.getX() + x, waterLevel, pos.getZ() + z);

                    if (terrainHeight <= waterLevel) {
                        world.setBlock(waterPos, Blocks.WATER.defaultBlockState(), 3);
                    }
                }
            }
        }
    }

    private void addShorelinePatches(Level world, BlockPos lakeCenter, int radius, int waterLevel, RandomSource random) {
        for (int x = -radius - 1; x <= radius + 1; x++) {
            for (int z = -radius - 1; z <= radius + 1; z++) {
                // Calculate distance from center
                double distanceFromCenter = Math.sqrt(x * x + z * z);

                // Check if we are at the edge of the lake
                if (distanceFromCenter >= radius - 1 && distanceFromCenter <= radius) {
                    BlockPos pos = lakeCenter.offset(x, waterLevel, z);
                    int terrainHeight = world.getHeight(Heightmap.Types.WORLD_SURFACE, pos.getX(), pos.getZ());

                    // Check if we are right next to water
                    if (terrainHeight <= waterLevel && random.nextDouble() < 0.5) {
                        // Randomly decide between sand and gravel
                        Block blockToPlace = random.nextDouble() < 0.5 ? Blocks.SAND : Blocks.GRAVEL;

                        // Place sand/gravel above water
                        world.setBlock(pos.above(), blockToPlace.defaultBlockState(), 3);
                    }
                }
            }
        }
    }
}
