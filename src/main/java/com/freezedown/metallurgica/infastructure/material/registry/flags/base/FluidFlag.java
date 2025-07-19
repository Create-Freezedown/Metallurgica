package com.freezedown.metallurgica.infastructure.material.registry.flags.base;

import com.freezedown.metallurgica.foundation.fluid.IMaterialFluid;
import com.freezedown.metallurgica.infastructure.material.Material;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.interfaces.IFluidRegistry;
import com.tterrag.registrate.util.entry.FluidEntry;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class FluidFlag implements IMaterialFlag, IFluidRegistry {
    private final String idPattern;
    private String existingNamespace = "metallurgica";

    public FluidFlag(String idPattern) {
        this.idPattern = idPattern;
    }

    public FluidFlag(String idPattern, String existingNamespace) {
        this.idPattern = idPattern;
        this.existingNamespace = existingNamespace;
    }

    public abstract FluidEntry<? extends IMaterialFluid> registerFluid(@NotNull Material material, IFluidRegistry flag, @NotNull MetallurgicaRegistrate registrate);
}
