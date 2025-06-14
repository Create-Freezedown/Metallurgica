package com.freezedown.metallurgica.registry.material.init;

import com.freezedown.metallurgica.foundation.fluid.IMaterialFluid;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.FluidFlag;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.registry.material.MetMaterials;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.tterrag.registrate.util.entry.FluidEntry;

public class MetMaterialFluids {
    public static ImmutableTable.Builder<FlagKey<?>, Material, FluidEntry<? extends IMaterialFluid>> MATERIAL_FLUIDS_BUILDER = ImmutableTable.builder();

    public static Table<FlagKey<?>, Material, FluidEntry<? extends IMaterialFluid>> MATERIAL_FLUIDS;

    public static void generateMaterialFluids(MetallurgicaRegistrate registrate) {
        for (Material material : MetMaterials.registeredMaterials.values()) {
            for (FlagKey<?> flagKey : FlagKey.getAllFlags()) {
                var flag = material.getFlag(flagKey);
                if (!material.noRegister(flagKey)) {
                    if (flag instanceof FluidFlag fluidFlag) {
                        registerMaterialFluid(fluidFlag, material, flagKey, registrate);
                    }
                }
            }
        }
    }

    private static void registerMaterialFluid(FluidFlag flag, Material material, FlagKey<?> flagKey, MetallurgicaRegistrate registrate) {
        MATERIAL_FLUIDS_BUILDER.put(flagKey, material, flag.registerFluid(material, flag, registrate));
    }
}
