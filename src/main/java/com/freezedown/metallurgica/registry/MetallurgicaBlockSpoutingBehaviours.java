package com.freezedown.metallurgica.registry;

import com.simibubi.create.api.behaviour.spouting.BlockSpoutingBehaviour;
import com.simibubi.create.compat.tconstruct.SpoutCasting;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Predicate;

public class MetallurgicaBlockSpoutingBehaviours {
    public static void registerDefaults() {
        Predicate<Fluid> isWater = fluid -> fluid.isSame(Fluids.WATER);

        BlockSpoutingBehaviour.BY_BLOCK_ENTITY.register(MetallurgicaBlockEntities.ingotCastingMold.get(), SpoutCasting.INSTANCE);
    }
}
