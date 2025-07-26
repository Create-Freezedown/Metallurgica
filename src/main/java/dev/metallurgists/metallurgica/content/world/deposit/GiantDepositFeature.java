package dev.metallurgists.metallurgica.content.world.deposit;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class GiantDepositFeature extends Feature<GiantDepositConfiguration> {

    public GiantDepositFeature() {
        super(GiantDepositConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<GiantDepositConfiguration> pContext) {
        RandomSource random = pContext.random();
        BlockPos origin = pContext.origin();
        WorldGenLevel worldGenLevel = pContext.level();

        return false;
    }
}
