package com.freezedown.metallurgica.content.world;

import com.drmangotea.createindustry.registry.TFMGPaletteStoneTypes;
import com.freezedown.metallurgica.registry.MetallurgicaBlocks;
import com.freezedown.metallurgica.registry.MetallurgicaMaterials;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import static com.freezedown.metallurgica.registry.MetallurgicaMaterials.*;

public enum VeinifierHijackerTypes {
    COPPER(NATIVE_COPPER.materialEntry.stone().get().defaultBlockState(), NATIVE_COPPER.materialEntry.depositBlock().get().defaultBlockState(), Blocks.GRANITE.defaultBlockState(), -20, 50),
    IRON(MAGNETITE.materialEntry.stone().get().defaultBlockState(), MAGNETITE.materialEntry.depositBlock().get().defaultBlockState(), Blocks.TUFF.defaultBlockState(), -60, 20),
    BAUXITE(MetallurgicaMaterials.BAUXITE.materialEntry.stone().get().defaultBlockState(), MetallurgicaMaterials.BAUXITE.materialEntry.depositBlock().get().defaultBlockState(), TFMGPaletteStoneTypes.BAUXITE.baseBlock.get().defaultBlockState(), -30, 70),
    GOLD(NATIVE_GOLD.materialEntry.stone().get().defaultBlockState(), NATIVE_GOLD.materialEntry.depositBlock().get().defaultBlockState(), Blocks.DIORITE.defaultBlockState(), -20, 40),
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
