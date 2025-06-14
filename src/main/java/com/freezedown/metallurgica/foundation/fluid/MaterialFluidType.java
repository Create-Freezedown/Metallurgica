package com.freezedown.metallurgica.foundation.fluid;

import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.FluidFlag;
import com.tterrag.registrate.builders.FluidBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;

import java.util.function.Consumer;

public class MaterialFluidType extends FluidType {
    public final Material material;
    public final FluidFlag fluidFlag;
    private ResourceLocation stillTexture;
    private ResourceLocation flowingTexture;
    private boolean shouldTint;

    public MaterialFluidType(Properties properties, Material material, FluidFlag flag, ResourceLocation stillTexture, ResourceLocation flowingTexture, boolean shouldTint) {
        super(properties);
        this.material = material;
        this.fluidFlag = flag;
        this.stillTexture = stillTexture;
        this.flowingTexture = flowingTexture;
        this.shouldTint = shouldTint;
    }

    public static FluidBuilder.FluidTypeFactory create(Material material, FluidFlag flag) {
        return (p, s, f) -> new MaterialFluidType(p, material, flag, s, f, true);
    }

    public static FluidBuilder.FluidTypeFactory create(Material material, FluidFlag flag, boolean shouldTint) {
        return (p, s, f) -> new MaterialFluidType(p, material, flag, s, f, shouldTint);
    }

    @Override
    public Component getDescription(FluidStack stack) {
        return fluidFlag.getLocalizedName(material);
    }

    @Override
    public Component getDescription() {
        return fluidFlag.getLocalizedName(material);
    }

    @Override
    public String getDescriptionId() {
        return fluidFlag.getUnlocalizedName(material);
    }

    @Override
    public String getDescriptionId(FluidStack stack) {
        return fluidFlag.getUnlocalizedName(material);
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
