package dev.metallurgists.metallurgica.infastructure.material.registry.flags.base;

import dev.metallurgists.metallurgica.foundation.fluid.IMaterialFluid;
import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.interfaces.IFluidRegistry;
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
