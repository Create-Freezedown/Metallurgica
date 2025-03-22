package com.freezedown.metallurgica.content.fluids.types;

import com.simibubi.create.AllFluids;
import com.tterrag.registrate.builders.FluidBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidStack;

public class TintedFluidType extends AllFluids.TintedFluidType {
    private int colour;

    public static FluidBuilder.FluidTypeFactory create(int colour) {
        return (p, s, f) -> {
            TintedFluidType fluidType = new TintedFluidType(p, s, f);
            fluidType.colour = colour;
            return fluidType;
        };
    }

    public TintedFluidType(Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture) {
        super(properties, stillTexture, flowingTexture);
    }



    @Override
    protected int getTintColor(FluidStack stack) {
        return colour | 0xff000000;
    }

    @Override
    protected int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        return NO_TINT;
    }

}
