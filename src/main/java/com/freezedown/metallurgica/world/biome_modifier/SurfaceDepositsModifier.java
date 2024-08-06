package com.freezedown.metallurgica.world.biome_modifier;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

import java.util.Arrays;
import java.util.List;

import static com.freezedown.metallurgica.Metallurgica.surfaceDeposits_CODEC;

public enum SurfaceDepositsModifier implements BiomeModifier {
    
    INSTANCE;
    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase != Phase.MODIFY) {
            return;
        }
        
        BiomeGenerationSettingsBuilder generationSettings = builder.getGenerationSettings();
        //addFeatures(
        //        generationSettings.getFeatures(GenerationStep.Decoration.SURFACE_STRUCTURES),
        //        MFeatures.NATIVE_COPPER_DEPOSIT_PLACED.get()
        //);
    }
    
    @SafeVarargs
    private void removeFeatures(List<Holder<PlacedFeature>> allFeatures, Holder<PlacedFeature>... features) {
        for (Holder<PlacedFeature> feature : features) {
            allFeatures.remove(feature);
        }
    }
    
    @SafeVarargs
    private void addFeatures(List<Holder<PlacedFeature>> allFeatures, Holder<PlacedFeature>... features) {
        allFeatures.addAll(Arrays.asList(features));
    }
    
    @Override
    public Codec<? extends BiomeModifier> codec()
    {
        return surfaceDeposits_CODEC.get();
    }
}
