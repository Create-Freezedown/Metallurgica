package com.freezedown.metallurgica.foundation.worldgen.feature;

import com.freezedown.metallurgica.foundation.util.NoiseGenerator;
import com.freezedown.metallurgica.foundation.worldgen.feature.configuration.CrystalFeatureConfig;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.Random;

public class CrystalClusterFeature extends Feature<CrystalFeatureConfig> {
    public CrystalClusterFeature(Codec<CrystalFeatureConfig> pCodec) {
        super(pCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<CrystalFeatureConfig> featurePlaceContext) {
        CrystalFeatureConfig config = featurePlaceContext.config();
        Level world = featurePlaceContext.level().getLevel();
        BlockPos pos = featurePlaceContext.origin();
        generateCrystals(config, world, pos.getX() >> 4, pos.getZ() >> 4);
        return true;
    }

    public void generateCrystals(CrystalFeatureConfig config, Level world, int chunkX, int chunkZ) {
        int worldHeight = world.getHeight();
        for (int x = chunkX * 16; x < (chunkX + 1) * 16; x++) {
            for (int z = chunkZ * 16; z < (chunkZ + 1) * 16; z++) {
                for (int y = 1; y < worldHeight; y++) { // Start at y=1 to avoid surface
                    float noiseValue = (float) new NoiseGenerator(world.random,0.1).getNoiseValue(x, y, z);
                    if (noiseValue > 0.5) {
                        generateCrystal(world, x, y, z, config);
                    }
                }
            }
        }
    }

    private void generateCrystal(Level world, int x, int y, int z, CrystalFeatureConfig config) {
        switch (config.type()) {
            case CUBIC:
                generateCubicCrystal(world, x, y, z, config.crystalBlock());
                break;
            case HEXAGONAL:
                generateHexagonalCrystal(world, x, y, z, config.crystalBlock());
                break;
            case TETRAGONAL:
                generateTetragonalCrystal(world, x, y, z, config.crystalBlock());
                break;
            case ORTHORHOMBIC:
                generateOrthorhombicCrystal(world, x, y, z, config.crystalBlock());
                break;
            case MONOCLINIC:
                generateMonoclinicCrystal(world, x, y, z, config.crystalBlock());
                break;
            case TRICLINIC:
                generateTriclinicCrystal(world, x, y, z, config.crystalBlock());
                break;
            case AMORPHOUS:
                generateAmorphousCrystal(world, x, y, z, config.crystalBlock());
                break;
        }
    }

    private void generateCubicCrystal(Level world, int x, int y, int z, BlockState block) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = 0; dy <= 1; dy++) { // Height of 2 blocks
                for (int dz = -1; dz <= 1; dz++) {
                    world.setBlock(new BlockPos(x + dx, y + dy, z + dz), block, 3);
                }
            }
        }
    }

    private void generateHexagonalCrystal(Level world, int x, int y, int z, BlockState block) {
        int height = 3; // Example height
        for (int dy = 0; dy < height; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (Math.abs(dx + dz) <= 1) { // Hexagonal shape logic
                        world.setBlock(new BlockPos(x + dx, y + dy, z + dz), block, 3);
                    }
                }
            }
        }
    }

    private void generateTetragonalCrystal(Level world, int x, int y, int z, BlockState block) {
        for (int dy = 0; dy < 2; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -2; dz <= 2; dz++) { // Longer shape in the z-axis
                    world.setBlock(new BlockPos(x + dx, y + dy, z + dz), block, 3);
                }
            }
        }
    }

    private void generateOrthorhombicCrystal(Level world, int x, int y, int z, BlockState block) {
        for (int dy = 0; dy < 2; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (Math.abs(dx) + Math.abs(dz) <= 2) { // More rectangular shape
                        world.setBlock(new BlockPos(x + dx, y + dy, z + dz), block, 3);
                    }
                }
            }
        }
    }

    private void generateAmorphousCrystal(Level world, int x, int y, int z, BlockState block) {
        // Randomly generate blocks around the point
        Random rand = new Random();
        for (int i = 0; i < 10; i++) { // Place 10 blocks
            int dx = rand.nextInt(3) - 1;
            int dy = rand.nextInt(2);
            int dz = rand.nextInt(3) - 1;
            world.setBlock(new BlockPos(x + dx, y + dy, z + dz), block, 3);
        }
    }

    private void generateMonoclinicCrystal(Level world, int x, int y, int z, BlockState block) {
        int height = 5; // Height of the spike
        for (int i = 0; i < height; i++) {
            // Create a vertical spike
            world.setBlock(new BlockPos(x, y + i, z), block, 3);

            // Randomly place spikes diagonally
            if (i == height - 1) { // Only at the top of the spike
                placeDiagonalSpikes(world, x, y + i, z, block);
            }
        }
    }

    private void generateTriclinicCrystal(Level world, int x, int y, int z, BlockState block) {
        int height = 4; // Height of the spike
        for (int i = 0; i < height; i++) {
            // Create a vertical spike
            world.setBlock(new BlockPos(x, y + i, z), block, 3);

            // Randomly place spikes diagonally
            if (i == height - 1) { // Only at the top of the spike
                placeDiagonalSpikesTriclinic(world, x, y + i, z, block);
            }
        }
    }

    private void placeDiagonalSpikesTriclinic(Level world, int x, int y, int z, BlockState block) {
        Random rand = new Random();
        for (int i = 0; i < 4; i++) { // Randomly place 4 spikes
            int dx = rand.nextInt(3) - 1; // -1, 0, or 1
            int dz = rand.nextInt(3) - 1; // -1, 0, or 1

            // Ensure it's diagonal
            if (Math.abs(dx) + Math.abs(dz) == 1) {
                int spikeHeight = rand.nextInt(4) + 1; // Random height between 1 and 4
                for (int j = 0; j < spikeHeight; j++) {
                    world.setBlock(new BlockPos(x + dx, y + j + 1, z + dz), block, 3);
                }
            }
        }
    }

    private void placeDiagonalSpikes(Level world, int x, int y, int z, BlockState block) {
        Random rand = new Random();
        for (int i = 0; i < 3; i++) { // Randomly place 3 spikes
            int dx = rand.nextInt(3) - 1; // -1, 0, or 1
            int dz = rand.nextInt(3) - 1; // -1, 0, or 1

            // Ensure it's diagonal
            if (Math.abs(dx) + Math.abs(dz) == 1) {
                int spikeHeight = rand.nextInt(3) + 1; // Random height between 1 and 3
                for (int j = 0; j < spikeHeight; j++) {
                    world.setBlock(new BlockPos(x + dx, y + j + 1, z + dz), block, 3);
                }
            }
        }
    }
}
