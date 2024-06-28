package com.freezedown.metallurgica.content.fluids.types.uf_backport.behaviours;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class BehaviorableLiquidBlock extends LiquidBlock implements IBehaviorable {
    
    public BehaviorableLiquidBlock(Supplier<? extends FlowingFluid> fluid, Properties properties) {
        super(fluid, properties);
    }
    
    @Override
    public void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos thisPos, @NotNull Block block, @NotNull BlockPos neighborPos, boolean idkRandomParameter) {
        getBehaviors().forEach(behavior -> behavior.neighborChange(state, level, thisPos, block, neighborPos, idkRandomParameter));
        
        super.neighborChanged(state, level, thisPos, block, neighborPos, idkRandomParameter);
    }
    
    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        List<Integer> list = new java.util.ArrayList<>(getBehaviors().stream().map(behavior -> behavior.getFlammability(state, level, pos, direction)).toList());
        list.add(super.getFlammability(state, level, pos, direction));
        return list.stream().max(Integer::compareTo).orElse(0);
    }
    
    public List<IFluidBehavior> getBehaviors() {
        FlowingFluid fluid = this.getFluid();
        if (!(fluid instanceof BehaviorableFluid behaviorableFluid))
            throw new IllegalStateException("Fluid " + fluid + " is not a BehaviorableFluid, the block " + this + " cannot be a BehaviorableLiquidBlock");
        return behaviorableFluid.getBehaviors();
    }
}
