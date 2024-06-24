package com.freezedown.metallurgica.content.world.striated;

import com.simibubi.create.infrastructure.worldgen.LayerPattern;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.world.level.block.Blocks;

public class MetallurgicaLayeredPatterns {
    public static final NonNullSupplier<LayerPattern> AMETHYST = () -> LayerPattern.builder()
            .layer(l -> l.weight(4)
                    .block(Blocks.AIR)
                    .size(2, 5))
            .layer(l -> l.weight(3)
                    .block(Blocks.SMOOTH_BASALT)
                    .block(Blocks.CALCITE)
                    .size(2, 3))
            .layer(l -> l.weight(1)
                    .blocks(Blocks.AMETHYST_BLOCK, Blocks.BUDDING_AMETHYST)
                    .size(0, 1))
            .layer(l -> l.weight(2)
                    .blocks(Blocks.SMOOTH_BASALT, Blocks.CALCITE)
                    .size(2, 2))
            .layer(l -> l.weight(2)
                    .block(Blocks.AMETHYST_BLOCK)
                    .size(1, 2))
            .build();
    
    public MetallurgicaLayeredPatterns() {
    }
}
