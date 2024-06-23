package com.freezedown.metallurgica.foundation.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class MLargeOreVeinFeature extends MBaseConfigDrivenOreFeature<MConfigDrivenOreFeatureConfiguration> {
    public MLargeOreVeinFeature() {
        super(MConfigDrivenOreFeatureConfiguration.CODEC);
    }
    
    @Override
    public boolean place(FeaturePlaceContext<MConfigDrivenOreFeatureConfiguration> pContext) {
        RandomSource random = pContext.random();
        BlockPos blockpos = pContext.origin();
        WorldGenLevel worldgenlevel = pContext.level();
        MConfigDrivenOreFeatureConfiguration oreconfiguration = pContext.config();
        return true;
    }
}
