package com.freezedown.metallurgica.foundation.material;


import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.item.MetallurgicaItem;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.foundation.util.NonnullBiConsumer;
import com.freezedown.metallurgica.foundation.worldgen.config.MSandFeatureConfigEntry;
import com.freezedown.metallurgica.world.MetallurgicaOreFeatureConfigEntries;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockSource;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SandBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.registries.ForgeRegistries;

import static com.freezedown.metallurgica.registry.MetallurgicaTags.NameSpace.MOD;
import static com.freezedown.metallurgica.registry.MetallurgicaTags.optionalTag;
import static com.freezedown.metallurgica.world.MetallurgicaOreFeatureConfigEntries.createSandDeposit;


public class SandEntry {
    private final BlockEntry<SandBlock> sand;
    private final TagKey<Block> tag;
    private final String name;
    private final Boolean existing;

    public SandEntry(MetallurgicaRegistrate reg, String pName, Boolean pExisting) {
        ResourceLocation id = new ResourceLocation(MOD.id, "deposit_replaceable/" + pName);
        tag = MOD.optionalDefault ? optionalTag(ForgeRegistries.BLOCKS, id) : BlockTags.create(id);
        existing = pExisting;
//        sand = existing ?  : reg.sandBlock(pName,tag);
        sand = reg.sandBlock(pName,tag);
        name = pName;
    }


    public TagKey<Block> tag() { return tag; }
    public BlockEntry<SandBlock> sand() { return sand; }

    public MSandFeatureConfigEntry deposit(int size, int frequency, int minHeight, int maxHeight) {
        return createSandDeposit(name + "_deposit", size, frequency, frequency, minHeight, maxHeight)
                .customStandardDatagenExt()
                .withBlock(sand, new TagMatchTest(tag))
                .biomeTag(BiomeTags.IS_OVERWORLD)
                .parent();
    }

    public MSandFeatureConfigEntry cluster(int size, int frequency, int minHeight, int maxHeight) {
        return createSandDeposit(name + "_cluster", size, frequency, frequency, minHeight, maxHeight)
                .customStandardDatagenExt()
                .withBlock(sand, MetallurgicaOreFeatureConfigEntries.SAND_REPLACEABLE)
                .biomeTag(BiomeTags.IS_OVERWORLD)
                .parent();

    }

}
