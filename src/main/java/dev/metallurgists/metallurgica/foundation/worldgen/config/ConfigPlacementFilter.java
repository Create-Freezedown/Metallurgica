package dev.metallurgists.metallurgica.foundation.worldgen.config;

import dev.metallurgists.metallurgica.foundation.config.MetallurgicaConfigs;
import dev.metallurgists.metallurgica.foundation.worldgen.MetallurgicaPlacementModifiers;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class ConfigPlacementFilter extends PlacementFilter {
    public static final ConfigPlacementFilter INSTANCE = new ConfigPlacementFilter();
    public static final Codec<ConfigPlacementFilter> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {
        return !MetallurgicaConfigs.common().worldGen.disable.get();
    }

    @Override
    public PlacementModifierType<?> type() {
        return MetallurgicaPlacementModifiers.CONFIG_FILTER.get();
    }
}
