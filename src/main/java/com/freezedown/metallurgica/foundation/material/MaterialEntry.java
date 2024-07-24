package com.freezedown.metallurgica.foundation.material;

import com.freezedown.metallurgica.content.mineral.deposit.MineralDepositBlock;
import com.freezedown.metallurgica.foundation.MetallurgicaRegistrate;
import com.freezedown.metallurgica.foundation.item.MetallurgicaItem;
import com.freezedown.metallurgica.foundation.worldgen.MOreFeatureConfigEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.registries.ForgeRegistries;

import static com.freezedown.metallurgica.registry.MetallurgicaTags.NameSpace.MOD;
import static com.freezedown.metallurgica.registry.MetallurgicaTags.optionalTag;
import static com.freezedown.metallurgica.world.MetallurgicaOreFeatureConfigEntries.create;

public class MaterialEntry {
    private final BlockEntry<MineralDepositBlock> deposit;
    private final BlockEntry<Block> stone;
    private final TagKey<Block> tag;
    private final String name;
    private final ItemEntry<MetallurgicaItem> raw;
    private final ItemEntry<Item> rubble;

    public MaterialEntry(MetallurgicaRegistrate reg, String pName) {
        raw = reg.raw(pName);
        rubble = reg.simpleItem(pName + "_rubble", "material_rubble/" + pName, "material_rubble");
        deposit = reg.depositBlock(pName + "_deposit", raw);
        ResourceLocation id = new ResourceLocation(MOD.id, "deposit_replaceable/" + pName);
        tag = MOD.optionalDefault ? optionalTag(ForgeRegistries.BLOCKS, id) : BlockTags.create(id);
        stone = reg.mineralBlock(pName, tag, raw);
        name = pName;
    }
    public BlockEntry<MineralDepositBlock> depositBlock() { return deposit; }
    public BlockEntry<Block> stone() { return stone; }

    public TagKey<Block> tag() { return tag; }

    public ItemEntry<MetallurgicaItem> raw() {
        return raw;
    }

    public ItemEntry<Item> rubble() {
        return rubble;
    }

    public MOreFeatureConfigEntry deposit(int size, int frequency, int minHeight, int maxHeight) {
        return create(name + "_deposit", size, frequency, minHeight, maxHeight)
                .customStandardDatagenExt()
                .withBlock(stone, new TagMatchTest(tag))
                .biomeTag(BiomeTags.IS_OVERWORLD)
                .parent();
    }
    
    public MOreFeatureConfigEntry cluster(int size, int frequency, int minHeight, int maxHeight) {
        return create(name + "_cluster", size, frequency, minHeight, maxHeight)
                .customStandardDatagenExt()
                .withBlock(stone, OreFeatures.STONE_ORE_REPLACEABLES)
                .biomeTag(BiomeTags.IS_OVERWORLD)
                .parent();
    }
}
