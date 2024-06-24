package com.freezedown.metallurgica.world;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.world.striated.MetallurgicaLayeredPatterns;
import com.freezedown.metallurgica.foundation.worldgen.MOreFeatureConfigEntry;
import com.freezedown.metallurgica.registry.MetallurgicaBlocks;
import com.freezedown.metallurgica.registry.MetallurgicaTags;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.common.ForgeConfigSpec;

public class MetallurgicaOreFeatureConfigEntries {
    public static final RuleTest BAUXITE_CLUSTER_REPLACEABLE = new TagMatchTest(MetallurgicaTags.AllBlockTags.BAUXITE_ORE_REPLACEABLE.tag);
    
    public static final RuleTest BAUXITE_DEPOSIT_REPLACEABLE = new TagMatchTest(MetallurgicaTags.AllBlockTags.DEPOSIT_REPLACEABLE_BAUXITE.tag);
    public static final RuleTest MAGNETITE_DEPOSIT_REPLACEABLE = new TagMatchTest(MetallurgicaTags.AllBlockTags.DEPOSIT_REPLACEABLE_MAGNETITE.tag);
    public static final RuleTest NATIVE_COPPER_DEPOSIT_REPLACEABLE = new TagMatchTest(MetallurgicaTags.AllBlockTags.DEPOSIT_REPLACEABLE_NATIVE_COPPER.tag);
    public static final RuleTest NATIVE_GOLD_DEPOSIT_REPLACEABLE = new TagMatchTest(MetallurgicaTags.AllBlockTags.DEPOSIT_REPLACEABLE_NATIVE_GOLD.tag);
    
    public static final RuleTest BUDDING_AMETHYST_REPLACEABLE = new BlockMatchTest(Blocks.AMETHYST_BLOCK);
    
    public static final RuleTest GRAVEL_REPLACEABLE = new BlockMatchTest(Blocks.GRAVEL);
    
    public static final MOreFeatureConfigEntry BAUXITE_CLUSTER =
            create("bauxite_cluster", 19, 9, -30, 70)
                    .customStandardDatagenExt()
                    .withBlock(MetallurgicaBlocks.bauxiteStone, BAUXITE_CLUSTER_REPLACEABLE)
                    .biomeTag(BiomeTags.IS_OVERWORLD)
                    .parent();
    public static final MOreFeatureConfigEntry MAGNETITE_CLUSTER =
            create("magnetite_cluster", 23, 4, -3, 128)
                    .customStandardDatagenExt()
                    .withBlock(MetallurgicaBlocks.magnetiteStone, OreFeatures.STONE_ORE_REPLACEABLES)
                    .biomeTag(BiomeTags.IS_OVERWORLD)
                    .parent();
    public static final MOreFeatureConfigEntry NATIVE_COPPER_CLUSTER =
            create("native_copper_cluster", 26, 7, -3, 97)
                    .customStandardDatagenExt()
                    .withBlock(MetallurgicaBlocks.nativeCopperStone, OreFeatures.STONE_ORE_REPLACEABLES)
                    .biomeTag(BiomeTags.IS_OVERWORLD)
                    .parent();
    public static final MOreFeatureConfigEntry NATIVE_GOLD_CLUSTER =
            create("native_gold_cluster", 12, 4, -12, 56)
                    .customStandardDatagenExt()
                    .withBlock(MetallurgicaBlocks.nativeGoldStone, OreFeatures.STONE_ORE_REPLACEABLES)
                    .biomeTag(BiomeTags.IS_OVERWORLD)
                    .parent();
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static final MOreFeatureConfigEntry BAUXITE_DEPOSIT =
            create("bauxite_deposit", 1, 10, -30, 70)
                    .customStandardDatagenExt()
                    .withBlock(MetallurgicaBlocks.bauxiteStone, BAUXITE_DEPOSIT_REPLACEABLE)
                    .biomeTag(BiomeTags.IS_OVERWORLD)
                    .parent();
    public static final MOreFeatureConfigEntry MAGNETITE_DEPOSIT =
            create("magnetite_deposit", 1, 5, -3, 128)
                    .customStandardDatagenExt()
                    .withBlock(MetallurgicaBlocks.magnetiteStone, MAGNETITE_DEPOSIT_REPLACEABLE)
                    .biomeTag(BiomeTags.IS_OVERWORLD)
                    .parent();
    public static final MOreFeatureConfigEntry NATIVE_COPPER_DEPOSIT =
            create("native_copper_deposit", 1, 8, -3, 97)
                    .customStandardDatagenExt()
                    .withBlock(MetallurgicaBlocks.nativeCopperStone, NATIVE_COPPER_DEPOSIT_REPLACEABLE)
                    .biomeTag(BiomeTags.IS_OVERWORLD)
                    .parent();
    public static final MOreFeatureConfigEntry NATIVE_GOLD_DEPOSIT =
            create("native_gold_deposit", 1, 5, -12, 56)
                    .customStandardDatagenExt()
                    .withBlock(MetallurgicaBlocks.nativeGoldStone, NATIVE_GOLD_DEPOSIT_REPLACEABLE)
                    .biomeTag(BiomeTags.IS_OVERWORLD)
                    .parent();
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static final MOreFeatureConfigEntry STRIATED_GEODES_OVERWORLD = create("striated_geodes_overworld", 48, 1 / 32f, -50, 70)
            .layeredDatagenExt()
            .withLayerPattern(MetallurgicaLayeredPatterns.AMETHYST)
            .biomeTag(BiomeTags.IS_OVERWORLD)
            .parent();
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static final MOreFeatureConfigEntry BUDDING_AMETHYST = create("budding_amethyst", 1, 1 / 8f, -50, 70)
            .customStandardDatagenExt()
            .withBlock(() -> Blocks.BUDDING_AMETHYST, BUDDING_AMETHYST_REPLACEABLE)
            .biomeTag(BiomeTags.IS_OVERWORLD)
            .parent();
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static final MOreFeatureConfigEntry SAND_IN_RIVERS = create("sand_in_rivers", 79, 25, 15, 61)
            .customStandardDatagenExt()
            .withBlock(() -> Blocks.SAND, GRAVEL_REPLACEABLE)
            .biomeTag(BiomeTags.IS_RIVER)
            .parent();
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    
    private static MOreFeatureConfigEntry create(String name, int clusterSize, float frequency,
                                                 int minHeight, int maxHeight) {
        ResourceLocation id = Metallurgica.asResource(name);
        return new MOreFeatureConfigEntry(id, clusterSize, frequency, minHeight, maxHeight);
    }
    
    public static void fillConfig(ForgeConfigSpec.Builder builder, String namespace) {
        MOreFeatureConfigEntry.ALL
                .forEach((id, entry) -> {
                    if (id.getNamespace().equals(namespace)) {
                        builder.push(entry.getName());
                        entry.addToConfig(builder);
                        builder.pop();
                    }
                });
    }
    
    public static void init() {}
}
