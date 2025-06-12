package com.freezedown.metallurgica.foundation.worldgen;

import com.freezedown.metallurgica.Metallurgica;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

public class MetallurgicaConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>>
            MAGNETITE_CONDUIT = key("magnetite_conduit");

    private static ResourceKey<ConfiguredFeature<?, ?>> key(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, Metallurgica.asResource(name));
    }

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> ctx) {
        RuleTest stoneOreReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateOreReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        //register(ctx, MAGNETITE_CONDUIT, MetallurgicaFeatures.MAGMA_CONDUIT.get(), new MagmaConduitFeatureConfig(Blocks.SMOOTH_BASALT.defaultBlockState(), List.of(MetallurgicaOre.MAGNETITE.ORE.stone().getDefaultState()), List.of(MetallurgicaOre.MAGNETITE.ORE.depositBlock().getDefaultState()), 123, -23));
    }
}
