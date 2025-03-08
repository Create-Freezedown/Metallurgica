package com.freezedown.metallurgica.registry.misc;

import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.freezedown.metallurgica.foundation.UpwardsBlockPattern;
import com.freezedown.metallurgica.registry.MetallurgicaBlocks;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;

public class MetallurgicaBlockPatterns {
    
    public static final BlockPattern industrialBlastFurnace = UpwardsBlockPattern.of(BlockPatternBuilder.start()
            .aisle("RSSSR", "RSTSR", "RSSSR", "RFFFR", "RFFFR", "RFFFR", "RFFFR", "#FFF#", "#FFF#", "#FFF#")
            .aisle("SFFFS", "SF#FS", "SSSSS", "FFFFF", "FR#RF", "FR#RF", "FR#RF", "FR#RF", "FR#RF", "FR#RF")
            .aisle("SFFFS", "T###T", "SS#SS", "FF#FF", "F###F", "F###F", "F###F", "F###F", "F###F", "F###F")
            .aisle("SFFFS", "SF#FS", "SSSSS", "FFFFF", "FR#RF", "FR#RF", "FR#RF", "FR#RF", "FR#RF", "FR#RF")
            .aisle("RSSSR", "RSHSR", "RSSSR", "RFFFR", "RFFFR", "RFFFR", "RFFFR", "#FFF#", "#FFF#", "#FFF#")
            .where('R', BlockInWorld.hasState(BlockStatePredicate.forBlock(TFMGBlocks.FIREPROOF_BRICK_REINFORCEMENT.get())))
            .where('S', BlockInWorld.hasState(BlockStatePredicate.forBlock(MetallurgicaBlocks.carbonBrick.get())))
            .where('F', BlockInWorld.hasState(BlockStatePredicate.forBlock(TFMGBlocks.FIREPROOF_BRICKS.get())))
            .where('T', BlockInWorld.hasState(BlockStatePredicate.forBlock(MetallurgicaBlocks.tuyere.get())))
            .where('H', BlockInWorld.hasState(BlockStatePredicate.forBlock(MetallurgicaBlocks.hearth.get())))
            .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.AIR)))
    );
}
