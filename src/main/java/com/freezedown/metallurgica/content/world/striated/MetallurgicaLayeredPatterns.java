package com.freezedown.metallurgica.content.world.striated;

import com.drmangotea.createindustry.registry.TFMGPaletteStoneTypes;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.infrastructure.worldgen.LayerPattern;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.world.level.block.Blocks;

public class MetallurgicaLayeredPatterns {
    public static final NonNullSupplier<LayerPattern> AMETHYST = () -> {
        return LayerPattern.builder().layer((l) -> {
            l.weight(1).passiveBlock();
        }).layer((l) -> {
            l.weight(8).block(Blocks.AIR).size(2, 5);
        }).layer((l) -> {
            l.weight(4).block(Blocks.SMOOTH_BASALT).block(Blocks.CALCITE).size(1, 3);
        }).layer((l) -> {
            l.weight(1).block(Blocks.AMETHYST_BLOCK).size(0, 1);
        }).build();
    };
    
    public MetallurgicaLayeredPatterns() {
    }
}
