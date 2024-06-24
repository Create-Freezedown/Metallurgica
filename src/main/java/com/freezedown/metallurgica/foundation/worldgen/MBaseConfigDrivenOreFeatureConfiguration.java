package com.freezedown.metallurgica.foundation.worldgen;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class MBaseConfigDrivenOreFeatureConfiguration implements FeatureConfiguration {
    public final MOreFeatureConfigEntry entry;
    public final float discardChanceOnAirExposure;
    
    public MBaseConfigDrivenOreFeatureConfiguration(MOreFeatureConfigEntry entry, float discardChance) {
        this.entry = entry;
        this.discardChanceOnAirExposure = discardChance;
    }
    
    public MOreFeatureConfigEntry getEntry() {
        return entry;
    }
    
    public int getClusterSize() {
        return entry.clusterSize.get();
    }
    
    public float getDiscardChanceOnAirExposure() {
        return discardChanceOnAirExposure;
    }
}
