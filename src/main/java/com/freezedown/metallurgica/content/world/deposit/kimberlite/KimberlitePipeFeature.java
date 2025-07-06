package com.freezedown.metallurgica.content.world.deposit.kimberlite;

import net.minecraft.core.BlockPos;
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
        int minHeight = worldGenLevel.getMinBuildHeight();
        int originX = origin.getX();
        int originZ = origin.getZ();

        if (!isSurfaceBlock(worldGenLevel, origin)) {
            return false;
        }

        for (int y = origin.getY(); y > worldGenLevel.getMinBuildHeight(); y--) {
            BlockPos pos = new BlockPos(originX, y, originZ);
            int radius = getRadius(worldGenLevel, origin, y, config);
            for(BlockPos pos1 : BlockPos.betweenClosed(pos.offset(-radius, 0, -radius), pos.offset(radius, 0, radius))) {
                if (!worldGenLevel.getBlockState(pos1).isAir() && shouldPlace(radius, pos, pos1)) {
                    worldGenLevel.setBlock(pos1, Blocks.EMERALD_BLOCK.defaultBlockState(), 3);
                }
            }
        }

        return false;
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

    private boolean shouldPlace(int radius, BlockPos pos, BlockPos pos1) {
        int i1 = pos1.getX() - pos.getX();
        int j1 = pos1.getZ() - pos.getZ();
        return i1 * i1 + j1 * j1 <= radius * radius;
    }
}
