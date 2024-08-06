package com.freezedown.metallurgica.world;

import com.drmangotea.createindustry.registry.TFMGPaletteStoneTypes;
import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.world.striated.MetallurgicaLayeredPatterns;
import com.freezedown.metallurgica.foundation.worldgen.config.MDepositFeatureConfigEntry;
import com.freezedown.metallurgica.foundation.worldgen.config.MOreFeatureConfigEntry;
import com.freezedown.metallurgica.registry.MetallurgicaMaterials;
import com.freezedown.metallurgica.registry.MetallurgicaTags;
import com.simibubi.create.foundation.utility.Couple;
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
    
    public static MOreFeatureConfigEntry create(String name, int clusterSize, float frequency, int minHeight, int maxHeight) {
        ResourceLocation id = Metallurgica.asResource(name);
        return new MOreFeatureConfigEntry(id, clusterSize, frequency, minHeight, maxHeight);
    }
    
    public static MDepositFeatureConfigEntry createDeposit(String name, float frequency, int minHeight, int maxHeight) {
        ResourceLocation id = Metallurgica.asResource(name);
        return new MDepositFeatureConfigEntry(id, frequency, minHeight, maxHeight);
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
    }
    
    public static void init() {}
}
