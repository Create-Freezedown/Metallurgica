package com.freezedown.metallurgica.content.blocks;

import com.freezedown.metallurgica.registry.MetallurgicaFluids;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;

public class SaltBlock extends AbstractWaterConvertingBlock {

    public SaltBlock(Properties properties) {
        super(properties);
    }

    @Override
    FlowingFluid convertTo() {
        return MetallurgicaFluids.brine.get();
    }
}
