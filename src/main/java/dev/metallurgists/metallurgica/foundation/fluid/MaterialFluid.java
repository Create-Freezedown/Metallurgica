package dev.metallurgists.metallurgica.foundation.fluid;

import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.interfaces.IFluidRegistry;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class MaterialFluid extends ForgeFlowingFluid implements IMaterialFluid{
    public final Material material;
    public final IFluidRegistry fluidFlag;

    public MaterialFluid(Properties properties, Material material, IFluidRegistry fluidFlag) {
        super(properties);
        this.material = material;
        this.fluidFlag = fluidFlag;
    }

    @Override
    public boolean isSource(FluidState fluidState) {
        return true;
    }

    @Override
    public int getAmount(FluidState fluidState) {
        return 8;
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
