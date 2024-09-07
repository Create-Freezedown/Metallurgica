package com.freezedown.metallurgica.foundation.worldgen;

import com.freezedown.metallurgica.foundation.worldgen.config.MSandFeatureConfigEntry;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class MBaseConfigDrivenSandFeatureConfiguration implements FeatureConfiguration {
    public final MSandFeatureConfigEntry entry;
    public final float discardChanceOnAirExposure;

    public MBaseConfigDrivenSandFeatureConfiguration(MSandFeatureConfigEntry entry, float discardChance) {
        this.entry = entry;
        this.discardChanceOnAirExposure = discardChance;
    }

    public MSandFeatureConfigEntry getEntry() {
        return entry;
    }

    public int getClusterSize() {
        return entry.clusterSize.get();
    }

    public float getDiscardChanceOnAirExposure() {
        return discardChanceOnAirExposure;
    }

}
