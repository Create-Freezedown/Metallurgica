package com.freezedown.metallurgica.foundation.worldgen;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.registry.MetallurgicaBlocks;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.CaveFeatures;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomBooleanFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.VegetationPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class MFeatures {
    public static final RuleTest NATURAL_STONE;
    public static final Holder<ConfiguredFeature<VegetationPatchConfiguration, ?>> CLAY_WITH_DRIPLEAVES;
    public static final Holder<ConfiguredFeature<VegetationPatchConfiguration, ?>> CLAY_POOL_WITH_DRIPLEAVES;
    public static final Holder<ConfiguredFeature<RandomBooleanFeatureConfiguration, ?>> LUSH_CAVES_CLAY;
    
    public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_CLAY;
    public static final Holder<ConfiguredFeature<DiskConfiguration, ?>> DISK_CLAY;
    
    public MFeatures() {
    }
    
    static {
        NATURAL_STONE = new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD);
        CLAY_WITH_DRIPLEAVES = FeatureUtils.register(Metallurgica.ID + ":dirty_clay_with_dripleaves", Feature.VEGETATION_PATCH, new VegetationPatchConfiguration(BlockTags.LUSH_GROUND_REPLACEABLE, BlockStateProvider.simple(MetallurgicaBlocks.dirtyClay.get()), PlacementUtils.inlinePlaced(CaveFeatures.DRIPLEAF), CaveSurface.FLOOR, ConstantInt.of(3), 0.8F, 2, 0.05F, UniformInt.of(4, 7), 0.7F));
        CLAY_POOL_WITH_DRIPLEAVES = FeatureUtils.register(Metallurgica.ID + ":dirty_clay_pool_with_dripleaves", Feature.WATERLOGGED_VEGETATION_PATCH, new VegetationPatchConfiguration(BlockTags.LUSH_GROUND_REPLACEABLE, BlockStateProvider.simple(MetallurgicaBlocks.dirtyClay.get()), PlacementUtils.inlinePlaced(CaveFeatures.DRIPLEAF), CaveSurface.FLOOR, ConstantInt.of(3), 0.8F, 5, 0.1F, UniformInt.of(4, 7), 0.7F));
        LUSH_CAVES_CLAY = FeatureUtils.register(Metallurgica.ID + ":lush_caves_dirty_clay", Feature.RANDOM_BOOLEAN_SELECTOR, new RandomBooleanFeatureConfiguration(PlacementUtils.inlinePlaced(CLAY_WITH_DRIPLEAVES), PlacementUtils.inlinePlaced(CLAY_POOL_WITH_DRIPLEAVES)));
        ORE_CLAY = FeatureUtils.register(Metallurgica.ID + ":ore_dirty_clay", Feature.ORE, new OreConfiguration(NATURAL_STONE, MetallurgicaBlocks.dirtyClay.get().defaultBlockState(), 33));
        DISK_CLAY = FeatureUtils.register(Metallurgica.ID + ":disk_dirty_clay", Feature.DISK, new DiskConfiguration(RuleBasedBlockStateProvider.simple(MetallurgicaBlocks.dirtyClay.get()), BlockPredicate.matchesBlocks(List.of(Blocks.DIRT, MetallurgicaBlocks.dirtyClay.get())), UniformInt.of(2, 3), 1));
    }
}
