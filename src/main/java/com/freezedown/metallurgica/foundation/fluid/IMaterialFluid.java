package com.freezedown.metallurgica.foundation.fluid;

import com.freezedown.metallurgica.infastructure.material.Material;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.FluidFlag;

public interface IMaterialFluid {
    public Material getMaterial();

    public FluidFlag getFlag();
}
