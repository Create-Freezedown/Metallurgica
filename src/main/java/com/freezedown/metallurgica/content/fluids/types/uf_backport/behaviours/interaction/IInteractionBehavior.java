package com.freezedown.metallurgica.content.fluids.types.uf_backport.behaviours.interaction;

import com.freezedown.metallurgica.content.fluids.types.uf_backport.behaviours.IFluidBehavior;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public interface IInteractionBehavior extends IFluidBehavior, IInteraction {
    @Override
    default boolean beforeDestroyingBlock(LevelAccessor worldIn, BlockPos pos, BlockState state) {
        interactWithBlockDestroyed(worldIn, pos, state);
        return false;
    }
}
