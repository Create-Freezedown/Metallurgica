package com.freezedown.metallurgica.foundation.material;

import com.freezedown.metallurgica.content.mineral.deposit.MineralDepositBlock;
import com.freezedown.metallurgica.foundation.MetallurgicaRegistrate;
import com.freezedown.metallurgica.foundation.item.MetallurgicaItem;
import com.freezedown.metallurgica.foundation.worldgen.MOreFeatureConfigEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.registries.ForgeRegistries;

import static com.freezedown.metallurgica.registry.MetallurgicaTags.NameSpace.MOD;
import static com.freezedown.metallurgica.registry.MetallurgicaTags.optionalTag;
import static com.freezedown.metallurgica.world.MetallurgicaOreFeatureConfigEntries.create;

public class GemEntry extends MaterialEntry {
    private final BlockEntry<MineralDepositBlock> deposit;
    private final BlockEntry<Block> stone;
    private final TagKey<Block> tag;
    private final String name;
    private final ItemEntry<MetallurgicaItem> cluster;
    private final ItemEntry<MetallurgicaItem> powder;
    
    public GemEntry(MetallurgicaRegistrate reg, String pName) {
        super(reg, pName);
        cluster = reg.cluster(pName + "_cluster");
        powder = reg.powder(pName + "_powder");
        deposit = reg.depositBlock(pName + "_deposit", cluster);
        ResourceLocation id = new ResourceLocation(MOD.id, "deposit_replaceable/" + pName);
        tag = MOD.optionalDefault ? optionalTag(ForgeRegistries.BLOCKS, id) : BlockTags.create(id);
        stone = reg.mineralBlock(pName, tag, cluster);
        name = pName;
    }
    @Override
    public BlockEntry<MineralDepositBlock> depositBlock() {
        return deposit;
    }
    @Override
    public BlockEntry<Block> stone() {
        return stone;
    }
    
    public TagKey<Block> tag() {
        return tag;
    }
    
    public ItemEntry<MetallurgicaItem> cluster() {
        return cluster;
    }
    
    public ItemEntry<MetallurgicaItem> powder() {
        return powder;
    }
    
    @Override
    public MOreFeatureConfigEntry deposit(int size, int frequency, int minHeight, int maxHeight) {
        return create(name + "_deposit", size, frequency, minHeight, maxHeight)
                .customStandardDatagenExt()
                .withBlock(stone, new TagMatchTest(tag))
                .biomeTag(BiomeTags.IS_OVERWORLD)
                .parent();
    }
    
    @Override
    public MOreFeatureConfigEntry cluster(int size, int frequency, int minHeight, int maxHeight) {
        return create(name + "_cluster", size, frequency, minHeight, maxHeight)
                .customStandardDatagenExt()
                .withBlock(stone, new TagMatchTest(tag))
                .biomeTag(BiomeTags.IS_OVERWORLD)
                .parent();
    }
}
