package com.freezedown.metallurgica.foundation.mixin;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.infrastructure.worldgen.AllBiomeModifiers;
import com.simibubi.create.infrastructure.worldgen.AllPlacedFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin({AllBiomeModifiers.class})
public class AllBiomeModifiersMixin {
    @Shadow
    public static final ResourceKey<BiomeModifier> STRIATED_ORES_OVERWORLD = key("striated_ores_overworld");
    @Shadow
    public static final ResourceKey<BiomeModifier> STRIATED_ORES_NETHER = key("striated_ores_nether");

    @Shadow
    private static ResourceKey<BiomeModifier> key(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, Create.asResource(name));
    }

    /**
     * @author PouffyDev
     * @reason Remove Zinc Ore from the biome modifier
     */
    @Overwrite(remap = false)
    public static void bootstrap(BootstapContext<BiomeModifier> ctx) {
        HolderGetter<Biome> biomeLookup = ctx.lookup(Registries.BIOME);
        HolderSet<Biome> isOverworld = biomeLookup.getOrThrow(BiomeTags.IS_OVERWORLD);
        HolderSet<Biome> isNether = biomeLookup.getOrThrow(BiomeTags.IS_NETHER);
        HolderGetter<PlacedFeature> featureLookup = ctx.lookup(Registries.PLACED_FEATURE);
        Holder<PlacedFeature> striatedOresOverworld = featureLookup.getOrThrow(AllPlacedFeatures.STRIATED_ORES_OVERWORLD);
        Holder<PlacedFeature> striatedOresNether = featureLookup.getOrThrow(AllPlacedFeatures.STRIATED_ORES_NETHER);
        ctx.register(STRIATED_ORES_OVERWORLD, addOre(isOverworld, striatedOresOverworld));
        ctx.register(STRIATED_ORES_NETHER, addOre(isNether, striatedOresNether));
    }

    @Shadow
    private static ForgeBiomeModifiers.AddFeaturesBiomeModifier addOre(HolderSet<Biome> biomes, Holder<PlacedFeature> feature) {
        return new ForgeBiomeModifiers.AddFeaturesBiomeModifier(biomes, HolderSet.direct(new Holder[]{feature}), GenerationStep.Decoration.UNDERGROUND_ORES);
    }
}
