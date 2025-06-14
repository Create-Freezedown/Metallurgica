package com.freezedown.metallurgica.foundation.fluid;

import com.freezedown.metallurgica.content.fluids.types.TransparentTintedFluidType;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.simibubi.create.AllFluids;
import com.tterrag.registrate.builders.FluidBuilder;
import net.createmod.catnip.theme.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;

import java.util.function.Consumer;

public class MaterialFluidType extends FluidType {
    public final Material material;
    private ResourceLocation stillTexture;
    private ResourceLocation flowingTexture;
    private boolean shouldTint;

    public MaterialFluidType(Properties properties, Material material, ResourceLocation stillTexture, ResourceLocation flowingTexture, boolean shouldTint) {
        super(properties);
        this.material = material;
        this.stillTexture = stillTexture;
        this.flowingTexture = flowingTexture;
        this.shouldTint = shouldTint;
    }

    public static FluidBuilder.FluidTypeFactory create(Material material) {
        return (p, s, f) -> new MaterialFluidType(p, material, s, f, true);
    }

    public static FluidBuilder.FluidTypeFactory create(Material material, boolean shouldTint) {
        return (p, s, f) -> new MaterialFluidType(p, material, s, f, shouldTint);
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return stillTexture;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return flowingTexture;
            }

            @Override
            public int getTintColor(FluidStack stack) {
                return MaterialFluidType.this.getTintColor(stack);
            }

            @Override
            public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                return MaterialFluidType.this.getTintColor(state, getter, pos);
            }
        });
    }

    protected int getTintColor(FluidStack stack) {
        return shouldTint ? material.materialInfo().colour() : -1;
    }

    protected int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        return shouldTint ? material.materialInfo().colour() : -1;
    }
}
