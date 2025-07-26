package dev.metallurgists.metallurgica.content.fluids.types;

import dev.metallurgists.metallurgica.registry.MetallurgicaFluids;
import com.simibubi.create.content.fluids.VirtualFluid;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;

import java.util.function.Consumer;

public class RiverSandFluid extends VirtualFluid {


    public static RiverSandFluid createSource(Properties properties) {
        return new RiverSandFluid(properties, true);
    }

    public static RiverSandFluid createFlowing(Properties properties) {
        return new RiverSandFluid(properties, false);
    }

    public RiverSandFluid(Properties properties, boolean source) {
        super(properties, source);
    }
    
    public static FluidStack of(int amount, String mineral) {
        FluidStack fluidStack = new FluidStack(MetallurgicaFluids.riverSand.get()
                .getSource(), amount);
        addMineralToFluidStack(fluidStack, mineral);
        return fluidStack;
    }
    
    public static FluidStack addMineralToFluidStack(FluidStack fluidStack, String mineral) {
        fluidStack.getOrCreateTag().putString("Mineral", mineral);
        return fluidStack;
    }
    
    public static class RiverSandFluidType extends FluidType {
        public final ResourceLocation still;
        public final ResourceLocation flow;
        
        public RiverSandFluidType(Properties properties, ResourceLocation stillRl, ResourceLocation flowRl) {
            super(properties);
            this.still = stillRl;
            this.flow = flowRl;
        }
        
        @Override
        public String getDescriptionId(FluidStack stack) {
            CompoundTag tag = stack.getOrCreateTag();
            if (tag.contains("Mineral")) {
                return "fluid.metallurgica.river_sand." + tag.getString("Mineral");
            }
            return super.getDescriptionId(stack);
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
