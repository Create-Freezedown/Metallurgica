package com.freezedown.metallurgica.foundation.fluid;

import com.freezedown.metallurgica.infastructure.material.Material;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.FluidFlag;
import com.simibubi.create.content.fluids.VirtualFluid;

public class VirtualMaterialFluid extends VirtualFluid implements IMaterialFluid {
    public final Material material;
    public final FluidFlag fluidFlag;

    public VirtualMaterialFluid(Properties properties, boolean source, Material material, FluidFlag fluidFlag) {
        super(properties, source);
        this.material = material;
        this.fluidFlag = fluidFlag;
    }

    public static VirtualMaterialFluid createSource(Properties properties, Material material, FluidFlag fluidFlag) {
        return new VirtualMaterialFluid(properties, true, material, fluidFlag);
    }

    public static VirtualMaterialFluid createFlowing(Properties properties, Material material, FluidFlag fluidFlag) {
        return new VirtualMaterialFluid(properties, false, material, fluidFlag);
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
