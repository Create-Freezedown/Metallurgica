/*
package com.freezedown.metallurgica.foundation.worldgen.feature;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.worldgen.feature.configuration.MSandDepositConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.phys.Vec3;

import static net.minecraft.world.level.block.Block.UPDATE_ALL;

public class SandDepositFeature extends Feature<MSandDepositConfiguration> {

    public SandDepositFeature() {super(MSandDepositConfiguration.CODEC);}

    @Override
    public boolean place(FeaturePlaceContext<MSandDepositConfiguration> context) {
        WorldGenLevel worldgenlevel = context.level();
        RandomSource randomsource = context.random();
        BlockPos pos = context.origin();
        MSandDepositConfiguration config = context.config();

        boolean large = randomsource.nextInt(5) == 0;
        int tipMin = (int) ((large ? 25 : 10) * 0.6);
        int tipRand = (int) ((large ? 35 : 20) * 0.3);
        int radiusMin = large ? 5 : 3;
        int radiusRand = large ? 3 : 1;

        int tip = tipMin + worldgenlevel.getRandom().nextInt(tipRand);
        int topX = worldgenlevel.getRandom().nextInt(tip) - tip / 2;
        int topZ = worldgenlevel.getRandom().nextInt(tip) - tip / 2;

        int radius = radiusMin + worldgenlevel.getRandom().nextInt(radiusRand);
        Vec3 to = new Vec3(pos.getX() + topX, pos.getY() - tip, pos.getZ() + topZ);

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                double fromCenter = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));
                if (fromCenter <= radius) {
                    Vec3 from = new Vec3(pos.getX() + x, pos.getY(), pos.getZ() + z);

                    if(worldgenlevel.getBlockState(posFromVec(from).below()).isAir()) {
                        continue;
                    }

                    Vec3 per = to.subtract(from).normalize();
                    Vec3 current = from.add(0, 0, 0);
                    double distance = from.distanceTo(to);

                    for (double i = 0; i < distance; i++) {
                        BlockPos targetPos = posFromVec(current);
                        if (i > 0 && i < distance / 1.3) {
                            int roll = randomsource.nextInt(3);
                            if (roll == 0) {
                                worldgenlevel.setBlock(targetPos, config.getSecondarySand(), UPDATE_ALL);
                            } else if (roll == 1) {
                                worldgenlevel.setBlock(targetPos, config.getPrimarySand(), UPDATE_ALL);
                        }
                        }
                        if (i <= 0) {
                            BlockPos getFromTarget = targetPos;
                            while (worldgenlevel.isEmptyBlock(getFromTarget.below())) {
                                if (randomsource.nextBoolean()) {
                                    worldgenlevel.setBlock(getFromTarget, config.getPrimarySand(), UPDATE_ALL);
                                } else {
                                    worldgenlevel.setBlock(getFromTarget, config.getSecondarySand(), UPDATE_ALL);
                                }
                                getFromTarget = getFromTarget.below();
                            }
                        }
                        current = current.add(per);
                    }
                }
            }
        }

        for (int x = -radius - 1; x <= radius + 1; x++) {
            for (int z = -radius - 1; z <= radius + 1; z++) {
                double fromCenter = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));
                if (fromCenter > radius && fromCenter <= radius + 1) {
                    BlockPos edgePos = pos.offset(x, 0, z);
                    BlockPos highestSolidPos = findHighestSolidBlock(worldgenlevel, edgePos);
                    BlockState blockToPlace = worldgenlevel.getBlockState(highestSolidPos.offset(0, -2, 0));
                    worldgenlevel.setBlock(highestSolidPos, blockToPlace, UPDATE_ALL);
                }
            }
        }

        for (int x = -radius - 2; x <= radius + 2; x++) {
            for (int z = -radius - 2; z <= radius + 2; z++) {
                if (randomsource.nextInt(10) < 3) { // 20% chance to place secondaryStone
                    BlockPos scatterPos = pos.offset(x, 0, z);
                    BlockPos highestSolidPos = findHighestSolidBlock(worldgenlevel, scatterPos);
                    if (randomsource.nextBoolean()) {
                        worldgenlevel.setBlock(highestSolidPos, config.getPrimarySand(), UPDATE_ALL);
                    } else {
                        worldgenlevel.setBlock(highestSolidPos, config.getSecondarySand(), UPDATE_ALL);
                    }
                }
            }
        }

        String sandDeposit = config.getPrimarySand().getBlock().getName().getString();
        Metallurgica.LOGGER.info("{} GENERATED AT: {} {} {}", sandDeposit, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    public BlockPos posFromVec(Vec3 vec3) {
        return new BlockPos((int) vec3.x(), (int) vec3.y(), (int) vec3.z());
    }

    private BlockPos findHighestSolidBlock(WorldGenLevel worldgenlevel, BlockPos pos) {
        BlockPos.MutableBlockPos mutablePos = pos.mutable();
        while (worldgenlevel.isEmptyBlock(mutablePos) && mutablePos.getY() > worldgenlevel.getMinBuildHeight()) {
            mutablePos.move(Direction.DOWN);
        }
        return mutablePos.immutable();
    }
}
*/
