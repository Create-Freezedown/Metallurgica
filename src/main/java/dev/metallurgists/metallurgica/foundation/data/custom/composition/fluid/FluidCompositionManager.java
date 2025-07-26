package dev.metallurgists.metallurgica.foundation.data.custom.composition.fluid;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.infastructure.element.data.SubComposition;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FluidCompositionManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    
    public static Map<FluidStack, FluidComposition> compositions = new HashMap<>();
    
    @Getter
    public static List<FluidStack> fluids = new ArrayList<>();
    
    public FluidCompositionManager() {
        super(GSON, "metallurgica_utilities/fluid_compositions");
    }
    
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        compositions.clear();
        fluids.clear();
        
        for (Map.Entry<ResourceLocation, JsonElement> entry : resourceLocationJsonElementMap.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();
            
            if (resourceLocation.getPath().startsWith("_")) {
                continue;
            }
            
            try {
                FluidComposition composition = FluidComposition.CODEC.parse(JsonOps.INSTANCE, entry.getValue()).getOrThrow(true, Metallurgica.LOGGER::error);
                if (composition != null) {
                    compositions.put(composition.fluidStack(), composition);
                    fluids.add(composition.fluidStack());
                }
            } catch (IllegalArgumentException | JsonParseException jsonParseException) {
                Metallurgica.LOGGER.error("Parsing error loading fluid compositions {}", resourceLocation, jsonParseException);
            }
        }
        Metallurgica.LOGGER.info("Load Complete for {} fluid compositions", compositions.size());
    }
    
    public static boolean hasComposition(FluidStack fluidStack) {
        boolean hasComposition = false;
        for (FluidStack stack : compositions.keySet()) {
            if (fluidStack.getFluid() == stack.getFluid()) {
                if (stack.hasTag()) {
                    hasComposition = stack.getTag().equals(fluidStack.getTag());
                } else {
                    hasComposition = true;
                }
                break;
            }
        }
        return hasComposition;
    }
    
    public static FluidComposition getComposition(FluidStack fluidStack) {
        return compositions.get(fluidStack);
    }

    public static List<SubComposition> getSubCompositions(FluidStack fluidStack) {
        return compositions.get(fluidStack).subCompositions();
    }
}
