package dev.metallurgists.metallurgica.content.world.deposit.kimberlite;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class KimberlitePipeFeature extends Feature<KimberlitePipeConfiguration> {

    public KimberlitePipeFeature() {
        super(KimberlitePipeConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<KimberlitePipeConfiguration> pContext) {
        RandomSource random = pContext.random();
        BlockPos origin = pContext.origin();
        WorldGenLevel worldGenLevel = pContext.level();
        KimberlitePipeConfiguration config = pContext.config();

        origin = dropOrigin(worldGenLevel, origin, config);
        if (origin == null) {
            return false;
        }

        int originX = origin.getX();
        int originZ = origin.getZ();

        if (!isSurfaceBlock(worldGenLevel, origin)) {
            return false;
        }

        for (int y = origin.getY(); y > worldGenLevel.getMinBuildHeight(); y--) {
            BlockPos pos = new BlockPos(originX, y, originZ);
            int radius = getRadius(worldGenLevel, origin, y, config);
            for(BlockPos pos1 : BlockPos.betweenClosed(pos.offset(-radius, 0, -radius), pos.offset(radius, 0, radius))) {
                if (!worldGenLevel.getBlockState(pos1).isAir() && shouldPlace(worldGenLevel, radius, pos, pos1)) {
                    worldGenLevel.setBlock(pos1, Blocks.EMERALD_BLOCK.defaultBlockState(), 3);
                }
            }
        }
        createCraterAtOrigin(worldGenLevel, origin, config.topRadius);
        //if (config.hasTuffRing) {
        //    createTuffRing(worldGenLevel, origin, config.topRadius);
        //}

        return true;
    }

    public BlockPos dropOrigin(WorldGenLevel world, BlockPos origin, KimberlitePipeConfiguration config) {
        for (int y = 0; y <= 10; y++) {
            if(!isSurfaceBlock(world, origin)) {
                origin = origin.below();
            } else {
                break;
            }
        }
        if (!isSurfaceBlock(world, origin)) return null;
        return origin;
    }

    public int getRadius(WorldGenLevel level, BlockPos origin, int height, KimberlitePipeConfiguration config) {
        int minHeight = level.getMinBuildHeight();
        int maxHeight = origin.getY();
        int topRadius = config.topRadius;
        int bottomRadius = config.bottomRadius;
        // Calculate the radius based on the height and configuration
        if (height < minHeight || height > maxHeight) {
            return 0; // Outside the valid height range
        }
        // Blend the radius from top to bottom based on the height
        double heightFactor = (double) (maxHeight - height) / (maxHeight - minHeight);
        int radius = (int) (topRadius + (bottomRadius - topRadius) * heightFactor);
        return Math.max(radius, 0); // Ensure radius is non-negative
    }

    private boolean isSurfaceBlock(WorldGenLevel world, BlockPos pos) {
        return world.canSeeSky(pos);
    }

    private boolean shouldPlace(WorldGenLevel world, int radius, BlockPos pos, BlockPos pos1) {
        if (world.getBlockState(pos1).is(BlockTags.LOGS) || world.getBlockState(pos1).is(BlockTags.LEAVES) || world.getBlockState(pos1).is(BlockTags.REPLACEABLE_BY_TREES)) return false;
        int i1 = pos1.getX() - pos.getX();
        int j1 = pos1.getZ() - pos.getZ();
        return i1 * i1 + j1 * j1 <= radius * radius;
    }

    private void createCraterAtOrigin(WorldGenLevel world, BlockPos origin, int radius) {
        //Create a three-dimensional oval crater at the origin
        int height = 4; // Height of the crater
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                for (int y = -height; y <= height; y++) {
                    if (x * x + z * z + y * y <= radius * radius) {
                        BlockPos pos = origin.offset(x, y, z);
                        if (!world.getBlockState(pos).isAir()) {
                            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                        }
                    }
                }
            }
        }
    }

    private void createTuffRing(WorldGenLevel world, BlockPos origin, int radius) {
        // Create a tuff ring around the crater, made of smaller clumps of tuff placed in a circular pattern
        int lagerRadius = radius + 4; // Larger radius for the tuff ring
        for (int x = -lagerRadius; x <= lagerRadius; x++) {
            for (int z = -lagerRadius; z <= lagerRadius; z++) {
                if (x * x + z * z <= lagerRadius * lagerRadius) {
                    BlockPos pos = origin.offset(x, 0, z);
                    if (world.getBlockState(pos).isAir()) {
                        world.setBlock(pos, Blocks.TUFF.defaultBlockState(), 3);
                    }
                }
            }
        }
    }
}
