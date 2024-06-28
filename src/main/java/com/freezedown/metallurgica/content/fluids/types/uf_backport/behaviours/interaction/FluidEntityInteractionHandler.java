package com.freezedown.metallurgica.content.fluids.types.uf_backport.behaviours.interaction;

import com.freezedown.metallurgica.content.fluids.types.uf_backport.behaviours.BehaviorableFluid;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;

public class FluidEntityInteractionHandler {
    public static void handleInteraction(Entity entity) {
        AABB aabb = entity.getBoundingBox().deflate(0.001D);
        
        int
                minX = Mth.floor(aabb.minX),
                maxX = Mth.ceil(aabb.maxX),
                minY = Mth.floor(aabb.minY),
                maxY = Mth.ceil(aabb.maxY),
                minZ = Mth.floor(aabb.minZ),
                maxZ = Mth.ceil(aabb.maxZ);
        
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        
        for(int l1 = minX; l1 < maxX; ++l1)
            for(int i2 = minY; i2 < maxY; ++i2)
                for(int j2 = minZ; j2 < maxZ; ++j2) {
                    blockpos$mutableblockpos.set(l1, i2, j2);
                    FluidState fluidstate = entity.level.getFluidState(blockpos$mutableblockpos);
                    if (fluidstate.getType() instanceof BehaviorableFluid behaviorableFluid)
                        behaviorableFluid.getBehavior(IInteractionBehavior.class).forEach(interactionBehavior -> interactionBehavior.interactWithEntity(blockpos$mutableblockpos, entity));
                }
    }
}
