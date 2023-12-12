package com.freezedown.metallurgica.foundation.mixin;

import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.placement.CavePlacements;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import static net.minecraft.data.worldgen.BiomeDefaultFeatures.addAncientDebris;

@Mixin(BiomeDefaultFeatures.class)
public class DefaultOreFeaturesMixin {
    
    /**
     * @author PouffyDev
     * @reason Remove default ores
     */
    @Overwrite
    public static void addDefaultOres(BiomeGenerationSettings.@NotNull Builder pBuilder, boolean pLargeOres) {
        pBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, CavePlacements.UNDERWATER_MAGMA);
    }
    /**
     * @author PouffyDev
     * @reason Remove default ores
     */
    @Overwrite
    public static void addExtraGold(BiomeGenerationSettings.Builder pBuilder) {
    }
    /**
     * @author PouffyDev
     * @reason Remove default ores
     */
    @Overwrite
    public static void addNetherDefaultOres(BiomeGenerationSettings.@NotNull Builder pBuilder) {
        pBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_GRAVEL_NETHER);
        pBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_BLACKSTONE);
        addAncientDebris(pBuilder);
    }
}
