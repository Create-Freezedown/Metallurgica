package com.freezedown.metallurgica.content.fluids.types;

import com.simibubi.create.AllFluids;
import com.tterrag.registrate.builders.FluidBuilder;
import net.createmod.catnip.theme.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Supplier;

public class TransparentTintedFluidType extends AllFluids.TintedFluidType {
    private Color tintColour;

    public TransparentTintedFluidType(Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture) {
        super(properties, stillTexture, flowingTexture);
    }

    public static FluidBuilder.FluidTypeFactory create(int tintColour) {
        return (p, s, f) -> {
            TransparentTintedFluidType fluidType = new TransparentTintedFluidType(p, s, f);
            fluidType.tintColour = new Color(tintColour, true);
            return fluidType;
        };
    }

    @Override
    protected int getTintColor(FluidStack stack) {
        return tintColour.getRGB();
    }

    @Override
    protected int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        return tintColour.getRGB();
    }
}
