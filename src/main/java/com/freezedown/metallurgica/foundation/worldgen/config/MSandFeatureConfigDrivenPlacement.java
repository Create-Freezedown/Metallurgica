package com.freezedown.metallurgica.foundation.worldgen.config;


import com.freezedown.metallurgica.foundation.config.MetallurgicaConfigs;
import com.freezedown.metallurgica.foundation.worldgen.MetallurgicaPlacementModifiers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MSandFeatureConfigDrivenPlacement extends PlacementModifier {
    public static final Codec<MSandFeatureConfigDrivenPlacement> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                MSandFeatureConfigEntry.CODEC
                        .fieldOf("entry")
                        .forGetter(MSandFeatureConfigDrivenPlacement::getEntry)
        ).apply(instance, MSandFeatureConfigDrivenPlacement::new);
    });

    private final MSandFeatureConfigEntry entry;

    public MSandFeatureConfigDrivenPlacement(MSandFeatureConfigEntry entry) {
        this.entry = entry;
    }


    @Override
    public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
        int count = getCount(getFrequency(), random);
        if (count == 0) {
            return Stream.empty();
        }

        int minY = getMinY();
        int maxY = getMaxY();

        return IntStream.range(0, count)
                .mapToObj(i -> pos)
                .map(p -> {
                    int x = random.nextInt(16) + p.getX();
                    int z = random.nextInt(16) + p.getZ();
                    int y = Mth.randomBetweenInclusive(random, minY, maxY);
                    return new BlockPos(x, y, z);
                });
    }

    public int getCount(float frequency, RandomSource random) {
        int floored = Mth.floor(frequency);
        return floored + (random.nextFloat() < (frequency - floored) ? 1 : 0);
    }

    @Override
    public PlacementModifierType<?> type() {
        return MetallurgicaPlacementModifiers.CONFIG_DRIVEN.get();
    }

    public MSandFeatureConfigEntry getEntry() {
        return entry;
    }

    public float getFrequency() {
        if (MetallurgicaConfigs.common().worldGen.disable.get())
            return 0;
        return entry.frequency.getF();
    }

    public int getMinY() {
        return entry.minHeight.get();
    }

    public int getMaxY() {
        return entry.maxHeight.get();
    }
}
