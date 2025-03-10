package com.freezedown.metallurgica.content.fluids.types;

import com.freezedown.metallurgica.registry.MetallurgicaFluids;
import com.simibubi.create.content.fluids.VirtualFluid;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;

import java.util.function.Consumer;

public class MoltenMetal extends VirtualFluid {
    public double moltenTemperature = 0;

    public static MoltenMetal createSource(Properties properties) {
        return new MoltenMetal(properties, true);
    }

    public static MoltenMetal createFlowing(Properties properties) {
        return new MoltenMetal(properties, false);
    }
    
    public MoltenMetal(Properties properties, boolean source) {
        super(properties, source);
    }
    
    public MoltenMetal moltenTemperature(double moltenTemperature) {
        this.moltenTemperature = moltenTemperature;
        return this;
    }
    
    public double getTemperature() {
        return moltenTemperature;
    }
    
    public static FluidStack of(int amount, float impurity) {
        FluidStack fluidStack = new FluidStack(MetallurgicaFluids.riverSand.get().getSource(), amount);
        addImpurity(fluidStack, impurity);
        return fluidStack;
    }
    
    public static FluidStack addImpurity(FluidStack fluidStack, float amount) {
        fluidStack.getOrCreateTag().putFloat("Impurity", amount);
        return fluidStack;
    }
    
    public static float getImpurity(FluidStack fluidStack) {
        return fluidStack.getOrCreateTag().getFloat("Impurity");
    }

    public static class MoltenMetalFluidType extends FluidType {
        public final ResourceLocation still;
        public final ResourceLocation flow;
        
        public MoltenMetalFluidType(Properties properties, ResourceLocation stillRl, ResourceLocation flowRl) {
            super(properties);
            this.still = stillRl;
            this.flow = flowRl;
        }

        @Override
        public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
            consumer.accept(new IClientFluidTypeExtensions() {

                @Override
                public ResourceLocation getStillTexture() {
                    return still;
                }

                @Override
                public ResourceLocation getFlowingTexture() {
                    return flow;
                }
            });
        }
    }
}
