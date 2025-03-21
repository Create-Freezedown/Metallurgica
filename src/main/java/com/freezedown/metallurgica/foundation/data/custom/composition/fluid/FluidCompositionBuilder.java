package com.freezedown.metallurgica.foundation.data.custom.composition.fluid;

import com.freezedown.metallurgica.foundation.data.custom.composition.data.Element;
import com.freezedown.metallurgica.foundation.data.custom.composition.FinishedComposition;
import com.freezedown.metallurgica.foundation.data.custom.composition.data.SubComposition;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("deprecation")
public class FluidCompositionBuilder {
    private final FluidStack fluidStack;
    private final List<SubComposition> subCompositions;
    
    public FluidCompositionBuilder(FluidStack fluidStack, List<SubComposition> subCompositions) {
        this.fluidStack = fluidStack;
        this.subCompositions = subCompositions;
    }
    
    public static FluidCompositionBuilder create(FluidStack fluidStack, List<SubComposition> subCompositions) {
        return new FluidCompositionBuilder(fluidStack, subCompositions);
    }
    
    static ResourceLocation getDefaultCompositionId(FluidStack fluidStack) {
        return BuiltInRegistries.FLUID.getKey(fluidStack.getFluid());
    }
    
    public void save(Consumer<FinishedComposition> pFinishedCompositionConsumer) {
        this.save(pFinishedCompositionConsumer, getDefaultCompositionId(this.fluidStack));
    }
    
    public void save(Consumer<FinishedComposition> pFinishedCompositionConsumer, ResourceLocation pCompositionId) {
        FluidStack toApply = this.fluidStack;
        List<SubComposition> subCompositionsToApply = this.subCompositions;
        pFinishedCompositionConsumer.accept(new DataGenResult(pCompositionId, toApply, subCompositionsToApply));
    }
    
    public static class DataGenResult implements FinishedComposition {
        private final FluidStack fluidStack;
        private final List<SubComposition> subCompositions;
        private ResourceLocation id;
        
        public DataGenResult(ResourceLocation pId, FluidStack fluidStack, List<SubComposition> subCompositions) {
            this.fluidStack = fluidStack;
            this.subCompositions = subCompositions;
            this.id = pId;
        }
        
        @Override
        public void serializeData(JsonObject json) {
            JsonObject fluid = new JsonObject();
            fluid.addProperty("name", CatnipServices.REGISTRIES.getKeyOrThrow(fluidStack.getFluid()).toString());
            fluid.addProperty("nbt", fluidStack.getOrCreateTag().toString());
            json.add("fluid", fluid);
            JsonArray compositionsArray = new JsonArray();
            for (SubComposition subComposition : subCompositions) {
                compositionsArray.add(subComposition.toJson());
            }
            json.add("compositions", compositionsArray);
        }
        
        @Override
        public ResourceLocation getId() {
            return id;
        }
    }
}
