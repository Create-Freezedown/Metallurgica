package dev.metallurgists.metallurgica.registry.material.init;

import dev.metallurgists.metallurgica.foundation.fluid.IMaterialFluid;
import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey;
import dev.metallurgists.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.interfaces.IFluidRegistry;
import dev.metallurgists.metallurgica.registry.material.MetMaterials;
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
                    if (flag instanceof IFluidRegistry fluidRegistry) {
                        registerMaterialFluid(fluidRegistry, material, flagKey, registrate);
                    }
                }
            }
        }
    }

    private static void registerMaterialFluid(IFluidRegistry fluidRegistry, Material material, FlagKey<?> flagKey, MetallurgicaRegistrate registrate) {
        MATERIAL_FLUIDS_BUILDER.put(flagKey, material, fluidRegistry.registerFluid(material, fluidRegistry, registrate));
    }
}
