package com.freezedown.metallurgica.world.biome_modifier;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.data.worldgen.placement.CavePlacements;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

import java.util.ArrayList;
import java.util.List;

import static com.freezedown.metallurgica.Metallurgica.oreGen_CODEC;
import static net.minecraft.data.worldgen.placement.OrePlacements.*;

public enum OreModifier implements BiomeModifier
{
    INSTANCE;
    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder)
    {
        if (phase != Phase.MODIFY)
        {
            return;
        }
        
        BiomeGenerationSettingsBuilder generationSettings = builder.getGenerationSettings();
        removeFeatures(
                generationSettings.getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES),
                ORE_COAL_LOWER,
                ORE_COAL_UPPER,
                ORE_IRON_MIDDLE,
                ORE_IRON_UPPER,
                ORE_IRON_SMALL,
                ORE_GOLD,
                ORE_GOLD_EXTRA,
                ORE_GOLD_DELTAS,
                ORE_GOLD_LOWER,
                ORE_GOLD_NETHER,
                ORE_LAPIS,
                ORE_LAPIS_BURIED,
                ORE_REDSTONE,
                ORE_REDSTONE_LOWER,
                ORE_EMERALD,
                ORE_DIAMOND_BURIED,
                ORE_DIAMOND_LARGE,
                ORE_DIAMOND,
                ORE_COPPER_LARGE,
                ORE_COPPER
        );

        removeFeatures(
                generationSettings.getFeatures(GenerationStep.Decoration.UNDERGROUND_DECORATION),
                CavePlacements.AMETHYST_GEODE
        );
    }

    @SafeVarargs
    private void removeFeatures(List<Holder<PlacedFeature>> allFeatures, Holder<PlacedFeature>... features) {
        for (Holder<PlacedFeature> feature : features) {
            allFeatures.remove(feature);
        }
    }
    
    @Override
    public Codec<? extends BiomeModifier> codec()
    {
        return oreGen_CODEC.get();
    }
}
