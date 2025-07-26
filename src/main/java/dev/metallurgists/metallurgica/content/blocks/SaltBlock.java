package dev.metallurgists.metallurgica.content.blocks;

import dev.metallurgists.metallurgica.registry.MetallurgicaFluids;
import net.minecraft.world.level.material.FlowingFluid;

public class SaltBlock extends AbstractWaterConvertingBlock {

    public SaltBlock(Properties properties) {
        super(properties);
    }

    @Override
    FlowingFluid convertTo() {
        return MetallurgicaFluids.brine.get();
    }
}
