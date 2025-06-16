package com.freezedown.metallurgica.foundation.fluid;

import com.freezedown.metallurgica.infastructure.material.Material;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.FluidFlag;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class MaterialFluid extends ForgeFlowingFluid implements IMaterialFluid{
    public final Material material;
    public final FluidFlag fluidFlag;

    public MaterialFluid(Properties properties, Material material, FluidFlag fluidFlag) {
        super(properties);
        this.material = material;
        this.fluidFlag = fluidFlag;
    }

    @Override
    public boolean isSource(FluidState fluidState) {
        return true;
    }

    @Override
    public int getAmount(FluidState fluidState) {
        return 8;
    }

    @Override
    public Material getMaterial() {
        return this.material;
    }

    @Override
    public FluidFlag getFlag() {
        return this.fluidFlag;
    }
}
