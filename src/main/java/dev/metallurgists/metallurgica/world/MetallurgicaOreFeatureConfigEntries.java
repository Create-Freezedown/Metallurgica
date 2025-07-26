package dev.metallurgists.metallurgica.world;

import dev.metallurgists.metallurgica.registry.MetallurgicaTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

public class MetallurgicaOreFeatureConfigEntries {
    public static final RuleTest BAUXITE_CLUSTER_REPLACEABLE = new TagMatchTest(MetallurgicaTags.AllBlockTags.BAUXITE_ORE_REPLACEABLE.tag);

    public static final RuleTest BUDDING_AMETHYST_REPLACEABLE = new BlockMatchTest(Blocks.AMETHYST_BLOCK);
    
    public static final RuleTest GRAVEL_REPLACEABLE = new BlockMatchTest(Blocks.GRAVEL);
    
    public static void init() {}
}
