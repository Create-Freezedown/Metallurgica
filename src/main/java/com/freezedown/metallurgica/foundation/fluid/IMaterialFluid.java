package com.freezedown.metallurgica.foundation.fluid;

import com.freezedown.metallurgica.foundation.material.registry.Material;
import com.freezedown.metallurgica.foundation.material.registry.flags.base.FluidFlag;

public interface IMaterialFluid {
    public Material getMaterial();

    public FluidFlag getFlag();
}
