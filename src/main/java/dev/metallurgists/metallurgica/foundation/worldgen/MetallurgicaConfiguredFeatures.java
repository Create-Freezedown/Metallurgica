package dev.metallurgists.metallurgica.foundation.worldgen;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.content.world.deposit.kimberlite.KimberlitePipeConfiguration;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import static net.minecraft.data.worldgen.features.FeatureUtils.register;

public class MetallurgicaConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>>
            MAGNETITE_CONDUIT = key("magnetite_conduit"),
            KIMBERLITE_PIPE = key("kimberlite_pipe");

    private static ResourceKey<ConfiguredFeature<?, ?>> key(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, Metallurgica.asResource(name));
    }

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> ctx) {
        RuleTest stoneOreReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateOreReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        register(ctx, KIMBERLITE_PIPE, MetallurgicaFeatures.KIMBERLITE_PIPE.get(), new KimberlitePipeConfiguration(true, 18, 10));
    }
}
