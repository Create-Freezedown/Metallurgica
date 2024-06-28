package com.freezedown.metallurgica.content.fluids.types.uf_backport.behaviours;

import com.freezedown.metallurgica.content.fluids.types.uf_backport.SmartFluid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class BehaviorableFluid extends SmartFluid implements IBehaviorable {
    protected BehaviorableFluid(Properties properties) {
        super(properties);
    }
    
    @Override
    public void tick(@NotNull Level worldIn, @NotNull BlockPos pos, @NotNull FluidState state) {
        if (getBehaviors().stream().anyMatch(behavior -> behavior.tick(worldIn, pos, state)))
            return;
        
        super.tick(worldIn, pos, state);
    }
    
    @Override
    public int getAmount(FluidState pState) {
        return pState.hasProperty(LEVEL) ? pState.getValue(LEVEL) : 8;
    }
    
    @Override
    protected void beforeDestroyingBlock(LevelAccessor worldIn, BlockPos pos, BlockState state) {
        if (getBehaviors().stream().anyMatch(behavior -> behavior.beforeDestroyingBlock(worldIn, pos, state)))
            return;
        
        super.beforeDestroyingBlock(worldIn, pos, state);
    }
    
    protected List<IFluidBehavior> getUncheckedBehaviors() {
        return new ArrayList<>();
    }
    
    public List<IFluidBehavior> getBehaviors() {
        List<IFluidBehavior> uncheckedBehaviors = getUncheckedBehaviors();
        BlockState blockState = this.createLegacyBlock(this.defaultFluidState());
        if (blockState.isAir()) {
            System.out.println("Fluid " + this + " has no block, returning empty list");
            return new ArrayList<>();
        }
        if (uncheckedBehaviors.stream().anyMatch(IFluidBehavior::requireBlockBeBehaviorable) && !(blockState.getBlock() instanceof BehaviorableLiquidBlock))
            throw new IllegalStateException(
                    "Fluid " + this + ", has a behavior that requires the block to be a BehaviorableLiquidBlock, " +
                            "but the block is not a BehaviorableLiquidBlock, the block is " + blockState.getBlock().getClass().getSimpleName()
            );
        
        return uncheckedBehaviors;
    }
}
