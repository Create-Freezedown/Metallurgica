package com.freezedown.metallurgica.content.fluids.types.uf_backport.behaviours;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;

public interface IFluidBehavior {
    default boolean tick(Level p_76113_, BlockPos p_76114_, FluidState p_76115_) {
        return false;
    }
    
    
    default boolean beforeDestroyingBlock(LevelAccessor worldIn, BlockPos pos, BlockState state) {
        return false;
    }
    
    
    default void neighborChange(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos thisPos, @NotNull Block block, @NotNull BlockPos neighborPos, boolean idkRandomParameter) {}
    
    
    default int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 0;
    }
    
    default boolean requireBlockBeBehaviorable() {
        return false;
    }
}
