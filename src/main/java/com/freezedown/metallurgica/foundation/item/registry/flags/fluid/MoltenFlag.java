package com.freezedown.metallurgica.foundation.item.registry.flags.fluid;

import com.freezedown.metallurgica.content.fluids.types.MaterialFluid;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.FluidFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.MaterialFlags;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.registry.material.MetMaterials;
import com.tterrag.registrate.util.entry.FluidEntry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MoltenFlag extends FluidFlag {
    public final double meltingPoint;

    public MoltenFlag(double meltingPoint) {
        super("molten_%s", "metallurgica");
        this.meltingPoint = meltingPoint;
    }

    public MoltenFlag(String existingNamespace) {
        super("molten_%s", existingNamespace);
        this.meltingPoint = 100; // Default melting point, can be overridden
    }

    @Override
    public FlagKey<?> getKey() {
        return FlagKey.MOLTEN;
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

    @Override
    public FluidEntry<? extends MaterialFluid> registerFluid(@NotNull Material material, FluidFlag flag, @NotNull MetallurgicaRegistrate registrate) {
        return null;
    }
}
