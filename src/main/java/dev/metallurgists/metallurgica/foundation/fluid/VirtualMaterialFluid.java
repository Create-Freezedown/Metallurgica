package dev.metallurgists.metallurgica.foundation.fluid;

import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.interfaces.IFluidRegistry;
import com.simibubi.create.content.fluids.VirtualFluid;

public class VirtualMaterialFluid extends VirtualFluid implements IMaterialFluid {
    public final Material material;
    public final IFluidRegistry fluidFlag;

    public VirtualMaterialFluid(Properties properties, boolean source, Material material, IFluidRegistry fluidFlag) {
        super(properties, source);
        this.material = material;
        this.fluidFlag = fluidFlag;
    }

    public static VirtualMaterialFluid createSource(Properties properties, Material material, IFluidRegistry fluidFlag) {
        return new VirtualMaterialFluid(properties, true, material, fluidFlag);
    }

    public static VirtualMaterialFluid createFlowing(Properties properties, Material material, IFluidRegistry fluidFlag) {
        return new VirtualMaterialFluid(properties, false, material, fluidFlag);
    }

    @Override
    public Material getMaterial() {
        return this.material;
    }

    @Override
    public IFluidRegistry getFlag() {
        return this.fluidFlag;
    }
}
