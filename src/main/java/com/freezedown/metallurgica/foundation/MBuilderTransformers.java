package com.freezedown.metallurgica.foundation;

import com.freezedown.metallurgica.content.mineral.deposit.MineralDepositBlock;
import com.freezedown.metallurgica.registry.MetallurgicaItems;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.LimitCount;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class MBuilderTransformers {
    
    public static <B extends MineralDepositBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> mineralDeposit() {
        return b -> b.initialProperties(SharedProperties::stone)
                .properties(p -> p.sound(SoundType.GILDED_BLACKSTONE).requiresCorrectToolForDrops())
                .transform(TagGen.pickaxeOnly())
                .blockstate((c, p) -> p.simpleBlock(c.get()))
                .simpleItem();
    }
    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> mineralStone(String name) {
        return b -> b.initialProperties(SharedProperties::stone)
                .properties(p -> p.sound(SoundType.STONE).requiresCorrectToolForDrops())
                .transform(TagGen.pickaxeOnly())
                .blockstate((c, p) -> p.simpleBlock(c.get()))
                .blockstate(BlockStateGen.naturalStoneTypeBlock(name))
                .item()
                .model((c, p) -> p.cubeAll(c.getName(), p.modLoc("block/palettes/stone_types/natural/" + name + "_2")))
                .build();
    }
}
