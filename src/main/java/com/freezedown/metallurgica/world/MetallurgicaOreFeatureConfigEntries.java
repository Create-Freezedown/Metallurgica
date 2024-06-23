package com.freezedown.metallurgica.world;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.worldgen.MOreFeatureConfigEntry;
import com.freezedown.metallurgica.registry.MetallurgicaBlocks;
import com.freezedown.metallurgica.registry.MetallurgicaTags;
import com.simibubi.create.infrastructure.worldgen.OreFeatureConfigEntry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.common.ForgeConfigSpec;

public class MetallurgicaOreFeatureConfigEntries {
    public static final RuleTest BAUXITE_CLUSTER_REPLACEABLE = new TagMatchTest(MetallurgicaTags.AllBlockTags.BAUXITE_ORE_REPLACEABLE.tag);
    
    public static final RuleTest BAUXITE_DEPOSIT_REPLACEABLE = new TagMatchTest(MetallurgicaTags.AllBlockTags.DEPOSIT_REPLACEABLE_BAUXITE.tag);
    public static final RuleTest MAGNETITE_DEPOSIT_REPLACEABLE = new TagMatchTest(MetallurgicaTags.AllBlockTags.DEPOSIT_REPLACEABLE_MAGNETITE.tag);
    public static final RuleTest NATIVE_COPPER_DEPOSIT_REPLACEABLE = new TagMatchTest(MetallurgicaTags.AllBlockTags.DEPOSIT_REPLACEABLE_NATIVE_COPPER.tag);
    public static final RuleTest NATIVE_GOLD_DEPOSIT_REPLACEABLE = new TagMatchTest(MetallurgicaTags.AllBlockTags.DEPOSIT_REPLACEABLE_NATIVE_GOLD.tag);
    
    public static final MOreFeatureConfigEntry BAUXITE_CLUSTER =
            create("bauxite_cluster", 7, 5, -30, 70)
                    .customStandardDatagenExt()
                    .withBlock(MetallurgicaBlocks.bauxiteStone, BAUXITE_CLUSTER_REPLACEABLE)
                    .biomeTag(BiomeTags.IS_OVERWORLD)
                    .parent();
    public static final MOreFeatureConfigEntry MAGNETITE_CLUSTER =
            create("magnetite_cluster", 6, 2, -3, 128)
                    .customStandardDatagenExt()
                    .withBlock(MetallurgicaBlocks.magnetiteStone, OreFeatures.STONE_ORE_REPLACEABLES)
                    .biomeTag(BiomeTags.IS_OVERWORLD)
                    .parent();
    public static final MOreFeatureConfigEntry NATIVE_COPPER_CLUSTER =
            create("native_copper_cluster", 12, 6, -3, 97)
                    .customStandardDatagenExt()
                    .withBlock(MetallurgicaBlocks.nativeCopperStone, OreFeatures.STONE_ORE_REPLACEABLES)
                    .biomeTag(BiomeTags.IS_OVERWORLD)
                    .parent();
    public static final MOreFeatureConfigEntry NATIVE_GOLD_CLUSTER =
            create("native_gold_cluster", 4, 2, -12, 56)
                    .customStandardDatagenExt()
                    .withBlock(MetallurgicaBlocks.nativeGoldStone, OreFeatures.STONE_ORE_REPLACEABLES)
                    .biomeTag(BiomeTags.IS_OVERWORLD)
                    .parent();
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static final MOreFeatureConfigEntry BAUXITE_DEPOSIT =
            create("bauxite_deposit", 1, 1, -30, 70)
                    .customStandardDatagenExt()
                    .withBlock(MetallurgicaBlocks.bauxiteStone, BAUXITE_DEPOSIT_REPLACEABLE)
                    .biomeTag(BiomeTags.IS_OVERWORLD)
                    .parent();
    public static final MOreFeatureConfigEntry MAGNETITE_DEPOSIT =
            create("magnetite_deposit", 1, 1, -3, 128)
                    .customStandardDatagenExt()
                    .withBlock(MetallurgicaBlocks.magnetiteStone, MAGNETITE_DEPOSIT_REPLACEABLE)
                    .biomeTag(BiomeTags.IS_OVERWORLD)
                    .parent();
    public static final MOreFeatureConfigEntry NATIVE_COPPER_DEPOSIT =
            create("native_copper_deposit", 1, 1, -3, 97)
                    .customStandardDatagenExt()
                    .withBlock(MetallurgicaBlocks.nativeCopperStone, NATIVE_COPPER_DEPOSIT_REPLACEABLE)
                    .biomeTag(BiomeTags.IS_OVERWORLD)
                    .parent();
    public static final MOreFeatureConfigEntry NATIVE_GOLD_DEPOSIT =
            create("native_gold_deposit", 1, 1, -12, 56)
                    .customStandardDatagenExt()
                    .withBlock(MetallurgicaBlocks.nativeGoldStone, NATIVE_GOLD_DEPOSIT_REPLACEABLE)
                    .biomeTag(BiomeTags.IS_OVERWORLD)
                    .parent();
    
    
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
