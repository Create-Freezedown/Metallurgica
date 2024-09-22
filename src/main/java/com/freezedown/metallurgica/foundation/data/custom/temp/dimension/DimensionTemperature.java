package com.freezedown.metallurgica.foundation.data.custom.temp.dimension;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public record DimensionTemperature(ResourceLocation dimensionLoc, double maxHeightTemperature, double minHeightTemperature) {
    public static final Codec<DimensionTemperature> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("dimension").forGetter(DimensionTemperature::getLoc),
            Codec.DOUBLE.fieldOf("maxHeightTemperature").orElse(23.0).forGetter(DimensionTemperature::maxHeightTemperature),
            Codec.DOUBLE.fieldOf("minHeightTemperature").orElse(23.0).forGetter(DimensionTemperature::minHeightTemperature)
    ).apply(instance, DimensionTemperature::new));

    public ResourceLocation getLoc() {
        return dimensionLoc;
    }

    public double getMaxHeightTemperature() {
        return maxHeightTemperature;
    }

    public double getMinHeightTemperature() {
        return minHeightTemperature;
    }
}
