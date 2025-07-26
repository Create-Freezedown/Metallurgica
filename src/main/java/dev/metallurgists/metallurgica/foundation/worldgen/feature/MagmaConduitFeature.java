package dev.metallurgists.metallurgica.foundation.worldgen.feature;

import dev.metallurgists.metallurgica.foundation.worldgen.feature.configuration.MagmaConduitFeatureConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.List;

public class MagmaConduitFeature extends Feature<MagmaConduitFeatureConfig> {
    public MagmaConduitFeature() {
        super(MagmaConduitFeatureConfig.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<MagmaConduitFeatureConfig> context) {
        WorldGenLevel world = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();

        // Parameters for the vein
        int maxLength = 128; // Maximum length of each branch
        int maxBranches = 5; // Maximum number of branches per segment
        double branchChance = 0.2; // Chance to create a new branch
        int veinThickness = 2; // Thickness of the vein

        // Block states for the vein
        BlockState filler = context.config().filler; // Filler block (e.g., stone)
        BlockState ore = getRandomBlock(context.config().orePool, random); // Regular ore block
        BlockState richOre = getRandomBlock(context.config().richOrePool, random); // Rich ore block

        // Start the recursive vein generation
        generateBranch(world, random, origin, maxLength, maxBranches, branchChance, veinThickness, filler, ore, richOre);

        return true;
    }

    private void generateBranch(WorldGenLevel world, RandomSource random, BlockPos start, int lengthRemaining, int branchesRemaining, double branchChance, int thickness, BlockState filler, BlockState ore, BlockState richOre) {
        if (lengthRemaining <= 0 || branchesRemaining <= 0) {
            return; // Stop if the branch has reached its maximum length or branch limit
        }

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos().set(start);

        // Generate the vein segment
        for (int i = 0; i < lengthRemaining; i++) {
            // Randomly change direction
            pos.move(
                    random.nextInt(3) - 1, // X (-1, 0, 1)
                    random.nextInt(3) - 1, // Y (-1, 0, 1)
                    random.nextInt(3) - 1  // Z (-1, 0, 1)
            );

            // Generate the vein thickness around the current position
            for (int x = -thickness; x <= thickness; x++) {
                for (int y = -thickness; y <= thickness; y++) {
                    for (int z = -thickness; z <= thickness; z++) {
                        BlockPos currentPos = pos.offset(x, y, z);

                        // Place ore or rich ore with some randomness
                        if (random.nextFloat() < 0.3) { // 70% chance to place ore
                            if (random.nextFloat() < 0.02) { // 2% chance for rich ore
                                world.setBlock(currentPos, richOre, 3);
                            } else {
                                world.setBlock(currentPos, ore, 3);
                            }
                        } else { // 30% chance to place filler
                            world.setBlock(currentPos, filler, 3);
                        }
                    }
                }
            }

            // Randomly create a new branch
            if (random.nextFloat() < branchChance) {
                BlockPos branchOrigin = pos.immutable();
                generateBranch(world, random, branchOrigin, lengthRemaining / 2, branchesRemaining - 1, branchChance, thickness, filler, ore, richOre);
            }
        }
    }

    private BlockState getRandomBlock(List<BlockState> blockStates, RandomSource random) {
        return blockStates.get(random.nextInt(blockStates.size()));
    }
}
