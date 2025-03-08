package com.freezedown.metallurgica.foundation.material;

import com.freezedown.metallurgica.content.mineral.deposit.MineralDepositBlock;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.foundation.item.MetallurgicaItem;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

import static com.freezedown.metallurgica.registry.MetallurgicaTags.NameSpace.MOD;
import static com.freezedown.metallurgica.registry.MetallurgicaTags.optionalTag;

public class MaterialEntry {
    private final BlockEntry<MineralDepositBlock> deposit;
    private final BlockEntry<Block> stone;
    private final TagKey<Block> tag;
    private final String name;
    private final ItemEntry<MetallurgicaItem> raw;
    private final ItemEntry<Item> rubble;
    @Nullable
    private final ItemEntry<MetallurgicaItem> rich;

    public MaterialEntry(MetallurgicaRegistrate reg, String pName, boolean richb) {
        raw = reg.raw(pName);
        rubble = reg.simpleItem(pName + "_rubble", "material_rubble/" + pName, "material_rubble");
        deposit = reg.depositBlock(pName + "_deposit", raw);
        ResourceLocation id = new ResourceLocation(MOD.id, "deposit_replaceable/" + pName);
        tag = MOD.optionalDefault ? optionalTag(ForgeRegistries.BLOCKS, id) : BlockTags.create(id);
        stone = reg.mineralBlock(pName, tag, raw);
        name = pName;
        rich = richb ? reg.metallurgicaItem("rich_magnetite", "enriched_materials/" + pName, "enriched_materials") : null;
    }
    
    public BlockEntry<MineralDepositBlock> depositBlock() { return deposit; }
    public BlockEntry<Block> stone() { return stone; }

    public TagKey<Block> tag() { return tag; }

    public ItemEntry<MetallurgicaItem> raw() {
        return raw;
    }

    public ItemEntry<MetallurgicaItem> rich() {
        return rich;
    }

    public ItemEntry<Item> rubble() {
        return rubble;
    }
}
