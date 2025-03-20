package com.freezedown.metallurgica.foundation.data.custom.composition.fluid;

import com.freezedown.metallurgica.foundation.data.custom.composition.Element;
import com.freezedown.metallurgica.foundation.data.custom.composition.FinishedComposition;
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
    private final List<Element> elements;
    
    public FluidCompositionBuilder(FluidStack fluidStack, List<Element> elements) {
        this.fluidStack = fluidStack;
        this.elements = elements;
    }
    
    public static FluidCompositionBuilder create(FluidStack fluidStack, List<Element> elements) {
        return new FluidCompositionBuilder(fluidStack, elements);
    }
    
    static ResourceLocation getDefaultCompositionId(FluidStack fluidStack) {
        return BuiltInRegistries.FLUID.getKey(fluidStack.getFluid());
    }
    
    public void save(Consumer<FinishedComposition> pFinishedCompositionConsumer) {
        this.save(pFinishedCompositionConsumer, getDefaultCompositionId(this.fluidStack));
    }
    
    public void save(Consumer<FinishedComposition> pFinishedCompositionConsumer, ResourceLocation pCompositionId) {
        FluidStack toApply = this.fluidStack;
        List<Element> elementsToApply = this.elements;
        pFinishedCompositionConsumer.accept(new DataGenResult(pCompositionId, toApply, elementsToApply));
    }
    
    public static class DataGenResult implements FinishedComposition {
        private final FluidStack fluidStack;
        private final List<Element> elements;
        private ResourceLocation id;
        
        public DataGenResult(ResourceLocation pId, FluidStack fluidStack, List<Element> elements) {
            this.fluidStack = fluidStack;
            this.elements = elements;
            this.id = pId;
        }
        
        @Override
        public void serializeData(JsonObject json) {
            JsonObject fluid = new JsonObject();
            fluid.addProperty("name", CatnipServices.REGISTRIES.getKeyOrThrow(fluidStack.getFluid()).toString());
            fluid.addProperty("nbt", fluidStack.getOrCreateTag().toString());
            json.add("fluid", fluid);
            JsonArray elementsArray = new JsonArray();
            for (Element element : elements) {
                JsonObject elementObject = new JsonObject();
                elementObject.addProperty("element", element.getName());
                elementObject.addProperty("amount", element.getAmount());
                elementObject.addProperty("groupedAmount", element.getGroupedAmount());
                elementObject.addProperty("numbersUp", element.numbersUp());
                elementObject.addProperty("bracketed", element.bracketed());
                elementObject.addProperty("forceCloseBracket", element.isBracketForceClosed());
                elementObject.addProperty("appendDash", element.hasDash());
                elementsArray.add(elementObject);
            }
            json.add("elements", elementsArray);
        }
        
        @Override
        public ResourceLocation getId() {
            return id;
        }
    }
}
