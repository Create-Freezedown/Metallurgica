package dev.metallurgists.metallurgica.foundation.fluid;

import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.interfaces.IFluidRegistry;

public interface IMaterialFluid {
    public Material getMaterial();

    public IFluidRegistry getFlag();
}
