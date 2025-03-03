package com.freezedown.metallurgica.content.fluids.types.open_ended_pipe;

import com.freezedown.metallurgica.content.fluids.types.MoltenMetal;
import com.freezedown.metallurgica.registry.MetallurgicaTags;
import com.simibubi.create.content.fluids.OpenEndedPipe;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class MoltenMetalEffect implements OpenEndedPipe.IEffectHandler {
    @Override
    public boolean canApplyEffects(OpenEndedPipe pipe, FluidStack fluid) {
        return fluid.getFluid().is(MetallurgicaTags.AllFluidTags.MOLTEN_METAL.tag);
    }
    
    @Override
    public void applyEffects(OpenEndedPipe pipe, FluidStack fluid) {
        Level world = pipe.getWorld();
        if (world.getGameTime() % 5 != 0)
            return;
        List<Entity> entities = world.getEntities((Entity) null, pipe.getAOE(), entity -> !entity.fireImmune());
        for (Entity entity : entities)
            entity.setSecondsOnFire(6);
    }
}
