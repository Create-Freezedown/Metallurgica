package com.freezedown.metallurgica.foundation;

import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

public class MBuilderTransformers {
    
    //public static <B extends MineralDepositBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> mineralDeposit() {
    //    return b -> b.initialProperties(SharedProperties::stone)
    //            .properties(p -> p.sound(SoundType.GILDED_BLACKSTONE).requiresCorrectToolForDrops())
    //            .transform(TagGen.pickaxeOnly())
    //            .blockstate((c, p) -> p.simpleBlock(c.get()))
    //            .simpleItem();
    //}
    //public static <B extends MineralDepositBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> mineralDepositSideTop() {
    //    return b -> b.initialProperties(SharedProperties::stone)
    //            .properties(p -> p.sound(SoundType.GILDED_BLACKSTONE).requiresCorrectToolForDrops())
    //            .transform(TagGen.pickaxeOnly())
    //            .blockstate((c, p) -> p.models().cubeColumn(c.getName(), p.modLoc("block/" + c.getName()), p.modLoc("block/" + c.getName() + "_top")))
    //            .simpleItem();
    //}
    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> stone(String name) {
        return b -> b.initialProperties(SharedProperties::stone)
                .properties(p -> p.sound(SoundType.STONE).requiresCorrectToolForDrops())
                .transform(TagGen.pickaxeOnly())
                .blockstate((c, p) -> p.simpleBlock(c.get()))
                .blockstate(BlockStateGen.naturalStoneTypeBlock(name))
                .item()
                .model((c, p) -> p.cubeAll(c.getName(), p.modLoc("block/palettes/stone_types/natural/" + name + "_2")))
                .build();
    }
    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> mineralStone(String name) {
        return b -> b.initialProperties(SharedProperties::stone)
                .properties(p -> p.sound(SoundType.STONE).requiresCorrectToolForDrops())
                .transform(TagGen.pickaxeOnly())
                .blockstate((c, p) -> p.simpleBlock(c.get()))
                .blockstate(MBlockStateGen.naturalMineralTypeBlock(name))
                .item()
                .model((c, p) -> p.cubeAll(c.getName(), p.modLoc("block/palettes/stone_types/mineral/" + name + "_2")))
                .build();
    }
}
