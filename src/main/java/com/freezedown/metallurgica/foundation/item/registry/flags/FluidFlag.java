package com.freezedown.metallurgica.foundation.item.registry.flags;

import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.IMaterialFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.MaterialFlags;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.registry.material.MetMaterials;
import com.tterrag.registrate.util.entry.FluidEntry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FluidFlag implements IMaterialFlag {
    public final double meltingPoint;

    public FluidFlag(double meltingPoint) {
        this.meltingPoint = meltingPoint;
    }

    @Override
    public FlagKey<?> getKey() {
        return FlagKey.FLUID;
    }

    @ApiStatus.Internal
    public void registerFluids(@NotNull Material material, @NotNull MetallurgicaRegistrate registrate) {
        List<FluidEntry<?>> fluidEntries = new ArrayList<>();
        if (material.hasFlag(FlagKey.INGOT)) {
            FluidEntry<?> fluid = registrate.moltenMetal(material, meltingPoint);
            fluidEntries.add(fluid);
        }
        if (!fluidEntries.isEmpty())
            MetMaterials.materialFluids.put(material, fluidEntries);
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }
}
