package com.freezedown.metallurgica.content.world;

import com.freezedown.metallurgica.registry.MetallurgicaBlocks;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public enum VeinifierHijackerTypes {
    COPPER(MetallurgicaBlocks.nativeCopperStone.get().defaultBlockState(), MetallurgicaBlocks.nativeCopperDeposit.get().defaultBlockState(), Blocks.GRANITE.defaultBlockState(), -20, 50),
    IRON(MetallurgicaBlocks.magnetiteStone.get().defaultBlockState(), MetallurgicaBlocks.magnetiteDeposit.get().defaultBlockState(), Blocks.TUFF.defaultBlockState(), -60, 20);
    
    public final BlockState ore;
    public final BlockState rawOreBlock;
    public final BlockState filler;
    public final int minY;
    public final int maxY;
    
    private VeinifierHijackerTypes(BlockState ore, BlockState deposit, BlockState filler, int minY, int maxY) {
        this.ore = ore;
        this.rawOreBlock = deposit;
        this.filler = filler;
        this.minY = minY;
        this.maxY = maxY;
    }
}
