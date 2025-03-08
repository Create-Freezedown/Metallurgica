package com.freezedown.metallurgica.world;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.world.striated.MetallurgicaLayeredPatterns;
import com.freezedown.metallurgica.foundation.worldgen.feature.deposit.DepositCapacity;
import com.freezedown.metallurgica.registry.MetallurgicaTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.common.ForgeConfigSpec;

public class MetallurgicaOreFeatureConfigEntries {
    public static final RuleTest BAUXITE_CLUSTER_REPLACEABLE = new TagMatchTest(MetallurgicaTags.AllBlockTags.BAUXITE_ORE_REPLACEABLE.tag);

    public static final RuleTest BUDDING_AMETHYST_REPLACEABLE = new BlockMatchTest(Blocks.AMETHYST_BLOCK);
    
    public static final RuleTest GRAVEL_REPLACEABLE = new BlockMatchTest(Blocks.GRAVEL);
    
    public static void init() {}
}
