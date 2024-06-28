package com.freezedown.metallurgica.content.fluids.types.uf_backport;

import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import org.jetbrains.annotations.NotNull;

public abstract class SmartFluid extends ForgeFlowingFluid {
    protected SmartFluid(Properties properties) {
        super(properties);
        
        if (this.isSource(this.defaultFluidState())) {
            this.registerDefaultState(this.getStateDefinition().any()
            );
        } else {
            this.registerDefaultState(this.getStateDefinition().any()
                    .setValue(LEVEL, 7)
            );
        }
    }
    
    protected void createFluidStateDefinition(StateDefinition.@NotNull Builder<Fluid, FluidState> builder) {
        super.createFluidStateDefinition(builder);
        if (!this.isSource(this.defaultFluidState()))
            builder.add(LEVEL);
    }
    
    protected abstract boolean isSource();
    
    @Override
    public final boolean isSource(@NotNull FluidState p_76140_) {
        return isSource();
    }
}
