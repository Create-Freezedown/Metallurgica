package com.freezedown.metallurgica.content.fluids.types.open_ended_pipe;

import com.simibubi.create.api.effect.OpenPipeEffectHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class MoltenMetalHandler implements OpenPipeEffectHandler {

    @Override
    public void apply(Level level, AABB area, FluidStack fluid) {
        if (level.getGameTime() % 5 != 0)
            return;
        List<Entity> entities = level.getEntities((Entity) null, area, entity -> !entity.fireImmune());
        for (Entity entity : entities)
            entity.setSecondsOnFire(6);
    }
}
