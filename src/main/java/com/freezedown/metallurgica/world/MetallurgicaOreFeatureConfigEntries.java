package com.freezedown.metallurgica.world;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.world.striated.MetallurgicaLayeredPatterns;
import com.freezedown.metallurgica.foundation.worldgen.config.MDepositFeatureConfigEntry;
import com.freezedown.metallurgica.foundation.worldgen.config.MOreFeatureConfigEntry;
import com.freezedown.metallurgica.foundation.worldgen.config.MSandFeatureConfigEntry;
import com.freezedown.metallurgica.foundation.worldgen.config.MTypedDepositFeatureConfigEntry;
import com.freezedown.metallurgica.foundation.worldgen.feature.deposit.DepositCapacity;
import com.freezedown.metallurgica.registry.MetallurgicaTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.common.ForgeConfigSpec;

public class MetallurgicaOreFeatureConfigEntries {
    public static final RuleTest BAUXITE_CLUSTER_REPLACEABLE = new TagMatchTest(MetallurgicaTags.AllBlockTags.BAUXITE_ORE_REPLACEABLE.tag);

    public static final RuleTest BUDDING_AMETHYST_REPLACEABLE = new BlockMatchTest(Blocks.AMETHYST_BLOCK);
    
    public static final RuleTest GRAVEL_REPLACEABLE = new BlockMatchTest(Blocks.GRAVEL);

    public static final RuleTest SAND_REPLACEABLE = new BlockMatchTest(Blocks.SAND);
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static final MOreFeatureConfigEntry STRIATED_GEODES_OVERWORLD = create("striated_geodes_overworld", 48, 1 / 32f, 24, -50, 70)
            .layeredDatagenExt()
            .withLayerPattern(MetallurgicaLayeredPatterns.AMETHYST)
            .biomeTag(BiomeTags.IS_OVERWORLD)
            .parent();
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static final MOreFeatureConfigEntry BUDDING_AMETHYST = create("budding_amethyst", 1, 1 / 8f, 24, -50, 70)
            .customStandardDatagenExt()
            .withBlock(() -> Blocks.BUDDING_AMETHYST, BUDDING_AMETHYST_REPLACEABLE)
            .biomeTag(BiomeTags.IS_OVERWORLD)
            .parent();
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static final MOreFeatureConfigEntry SAND_IN_RIVERS = create("sand_in_rivers", 79, 25, 1, 15, 61)
            .customStandardDatagenExt()
            .withBlock(() -> Blocks.SAND, GRAVEL_REPLACEABLE)
            .biomeTag(BiomeTags.IS_RIVER)
            .parent();
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    
    public static MOreFeatureConfigEntry create(String name, int clusterSize, float frequency, int chance, int minHeight, int maxHeight) {
        ResourceLocation id = Metallurgica.asResource(name);
        return new MOreFeatureConfigEntry(id, clusterSize, frequency, chance, minHeight, maxHeight);
    }

    public static MSandFeatureConfigEntry createSandDeposit(String name, int clusterSize, float frequency, int chance, int minHeight, int maxHeight) {
        ResourceLocation id = Metallurgica.asResource(name);
        return new MSandFeatureConfigEntry(id, clusterSize, frequency, chance, minHeight, maxHeight);
    }

    public static MDepositFeatureConfigEntry createDeposit(String name, float frequency, int chance, int minHeight, int maxHeight) {
        ResourceLocation id = Metallurgica.asResource(name);
        return new MDepositFeatureConfigEntry(id, frequency, chance, minHeight, maxHeight);
    }
    
    public static MTypedDepositFeatureConfigEntry createTypedDeposit(String name, int maxWidth, int minWidth, int maxDepth, int minDepth, float depositBlockChance, DepositCapacity capacity, float frequency, int chance, int minHeight, int maxHeight) {
        ResourceLocation id = Metallurgica.asResource(name);
        return new MTypedDepositFeatureConfigEntry(id, maxWidth, minWidth, maxDepth, minDepth, depositBlockChance, capacity, frequency, chance, minHeight, maxHeight);
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
        MDepositFeatureConfigEntry.ALL
                .forEach((id, entry) -> {
                    if (id.getNamespace().equals(namespace)) {
                        builder.push(entry.getName());
                        entry.addToConfig(builder);
                        builder.pop();
                    }
                });
        MTypedDepositFeatureConfigEntry.ALL
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
