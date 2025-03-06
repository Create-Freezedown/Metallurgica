package com.freezedown.metallurgica.content.fluids.types.uf_backport.behaviours.interaction;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public interface IInteraction {
    
    default void interactWithEntity(BlockPos pos, Entity interactant) {
        if (interactant instanceof ItemEntity itemEntity)
            interactWithItem(interactant.level(), pos, itemEntity.getItem());
        
    }
    
    
    default void interactWithBlock(LevelAccessor level, BlockPos pos, BlockState interactant) {
        if (interactant.getFluidState().is(Fluids.EMPTY))
            interactWithFluid(level, pos, interactant.getFluidState().getType());
    }
    
    
    default void interactWithBlockDestroyed(LevelAccessor level, BlockPos pos, BlockState interactant) {
        interactWithBlock(level, pos, interactant);
    }
    
    
    default void interactWithFluid(LevelAccessor level, BlockPos pos, Fluid interactant) {
    
    }
    
    
    default void interactWithItem(LevelAccessor level, BlockPos pos, ItemStack interactant) {
    
    }
}
