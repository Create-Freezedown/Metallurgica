package com.freezedown.metallurgica.content.world;

import com.drmangotea.tfmg.registry.TFMGPaletteStoneTypes;
import com.freezedown.metallurgica.registry.MetallurgicaOre;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import static com.freezedown.metallurgica.registry.MetallurgicaOre.*;

public enum VeinifierHijackerTypes {
    COPPER(NATIVE_COPPER.MATERIAL.stone().get().defaultBlockState(), NATIVE_COPPER.MATERIAL.depositBlock().get().defaultBlockState(), Blocks.GRANITE.defaultBlockState(), -20, 50),
    IRON(MAGNETITE.MATERIAL.stone().get().defaultBlockState(), MAGNETITE.MATERIAL.depositBlock().get().defaultBlockState(), Blocks.SMOOTH_BASALT.defaultBlockState(), -60, 20),
    BAUXITE(MetallurgicaOre.BAUXITE.MATERIAL.stone().get().defaultBlockState(), MetallurgicaOre.BAUXITE.MATERIAL.depositBlock().get().defaultBlockState(), TFMGPaletteStoneTypes.BAUXITE.baseBlock.get().defaultBlockState(), -30, 70),
    GOLD(NATIVE_GOLD.MATERIAL.stone().get().defaultBlockState(), NATIVE_GOLD.MATERIAL.depositBlock().get().defaultBlockState(), Blocks.DIORITE.defaultBlockState(), -20, 40),
    ;
    
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
