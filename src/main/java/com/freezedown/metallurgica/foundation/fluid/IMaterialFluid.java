package com.freezedown.metallurgica.foundation.fluid;

import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.FluidFlag;

public interface IMaterialFluid {
    public Material getMaterial();

    public FluidFlag getFlag();
}
