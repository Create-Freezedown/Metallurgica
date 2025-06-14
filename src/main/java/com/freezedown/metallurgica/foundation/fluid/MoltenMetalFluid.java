package com.freezedown.metallurgica.foundation.fluid;

import com.freezedown.metallurgica.foundation.material.registry.Material;
import com.freezedown.metallurgica.foundation.material.registry.flags.base.FluidFlag;
import lombok.Getter;

public class MoltenMetalFluid extends VirtualMaterialFluid {
    @Getter
    public double meltingPoint = 0;

    public MoltenMetalFluid(Properties properties, boolean source, Material material, FluidFlag fluidFlag) {
        super(properties, source, material, fluidFlag);
    }

    public static MoltenMetalFluid createSource(Properties properties, Material material, FluidFlag fluidFlag) {
        return new MoltenMetalFluid(properties, true, material, fluidFlag);
    }

    public static MoltenMetalFluid createFlowing(Properties properties, Material material, FluidFlag fluidFlag) {
        return new MoltenMetalFluid(properties, false, material, fluidFlag);
    }

    public MoltenMetalFluid meltingPoint(double meltingPoint) {
        this.meltingPoint = meltingPoint;
        return this;
    }
}
