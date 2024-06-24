package com.freezedown.metallurgica.world.biome_modifier;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.data.worldgen.placement.CavePlacements;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

import static com.freezedown.metallurgica.Metallurgica.oreGen_CODEC;

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
        generationSettings.getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).remove(OrePlacements.ORE_COAL_LOWER);
        generationSettings.getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).remove(OrePlacements.ORE_COAL_UPPER);
        generationSettings.getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).remove(OrePlacements.ORE_IRON_MIDDLE);
        generationSettings.getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).remove(OrePlacements.ORE_IRON_UPPER);
        generationSettings.getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).remove(OrePlacements.ORE_IRON_SMALL);
        generationSettings.getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).remove(OrePlacements.ORE_GOLD);
        generationSettings.getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).remove(OrePlacements.ORE_GOLD_EXTRA);
        generationSettings.getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).remove(OrePlacements.ORE_GOLD_DELTAS);
        generationSettings.getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).remove(OrePlacements.ORE_GOLD_LOWER);
        generationSettings.getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).remove(OrePlacements.ORE_GOLD_NETHER);
        generationSettings.getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).remove(OrePlacements.ORE_LAPIS);
        generationSettings.getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).remove(OrePlacements.ORE_LAPIS_BURIED);
        generationSettings.getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).remove(OrePlacements.ORE_REDSTONE);
        generationSettings.getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).remove(OrePlacements.ORE_REDSTONE_LOWER);
        generationSettings.getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).remove(OrePlacements.ORE_EMERALD);
        generationSettings.getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).remove(OrePlacements.ORE_DIAMOND_BURIED);
        generationSettings.getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).remove(OrePlacements.ORE_DIAMOND_LARGE);
        generationSettings.getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).remove(OrePlacements.ORE_DIAMOND);
        generationSettings.getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).remove(OrePlacements.ORE_COPPER_LARGE);
        generationSettings.getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).remove(OrePlacements.ORE_COPPER);
        generationSettings.getFeatures(GenerationStep.Decoration.UNDERGROUND_DECORATION).remove(CavePlacements.AMETHYST_GEODE);
    }
    
    @Override
    public Codec<? extends BiomeModifier> codec()
    {
        return oreGen_CODEC.get();
    }
}
