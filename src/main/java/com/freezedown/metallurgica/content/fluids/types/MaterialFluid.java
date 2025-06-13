package com.freezedown.metallurgica.content.fluids.types;

import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.FluidFlag;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class MaterialFluid extends ForgeFlowingFluid {
    public final Material material;
    public final FluidFlag fluidFlag;

    public MaterialFluid(Properties properties, Material material, FluidFlag fluidFlag) {
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


    public class MaterialFluidType extends FluidType {
        public MaterialFluidType(Properties properties) {
            super(properties);
        }

        @Override
        public Component getDescription() {
            return fluidFlag.getLocalizedName(material);
        }

        @Override
        public String getDescriptionId() {
            return fluidFlag.getUnlocalizedName(material);
        }
    }
}
