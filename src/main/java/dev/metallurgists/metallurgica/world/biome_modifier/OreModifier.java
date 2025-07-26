package dev.metallurgists.metallurgica.world.biome_modifier;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

import java.util.List;

import static dev.metallurgists.metallurgica.Metallurgica.surfaceDeposits_CODEC;

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
        //removeFeatures(
        //        generationSettings.getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES),
        //        OrePlacements.ORE_COAL_LOWER,
        //        OrePlacements.ORE_COAL_UPPER,
        //        OrePlacements.ORE_IRON_MIDDLE,
        //        OrePlacements.ORE_IRON_UPPER,
        //        OrePlacements.ORE_IRON_SMALL,
        //        OrePlacements.ORE_GOLD,
        //        OrePlacements.ORE_GOLD_EXTRA,
        //        OrePlacements.ORE_GOLD_DELTAS,
        //        OrePlacements.ORE_GOLD_LOWER,
        //        OrePlacements.ORE_GOLD_NETHER,
        //        OrePlacements.ORE_LAPIS,
        //        OrePlacements.ORE_LAPIS_BURIED,
        //        OrePlacements.ORE_REDSTONE,
        //        OrePlacements.ORE_REDSTONE_LOWER,
        //        OrePlacements.ORE_EMERALD,
        //        OrePlacements.ORE_DIAMOND_BURIED,
        //        OrePlacements.ORE_DIAMOND_LARGE,
        //        OrePlacements.ORE_DIAMOND,
        //        OrePlacements.ORE_COPPER_LARGE,
        //        OrePlacements.ORE_COPPER
        //);

        //removeFeatures(
        //        generationSettings.getFeatures(GenerationStep.Decoration.UNDERGROUND_DECORATION),
        //        CavePlacements.AMETHYST_GEODE
        //);
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
        return surfaceDeposits_CODEC.get();
    }
}
