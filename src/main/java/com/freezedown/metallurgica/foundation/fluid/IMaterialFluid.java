package com.freezedown.metallurgica.foundation.fluid;

import com.freezedown.metallurgica.infastructure.material.Material;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.FluidFlag;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.interfaces.IFluidRegistry;

public interface IMaterialFluid {
    public Material getMaterial();

    public IFluidRegistry getFlag();
}
