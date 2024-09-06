package com.freezedown.metallurgica.foundation.data.custom.temp.dimension;

import com.freezedown.metallurgica.Metallurgica;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DimensionTemperatureManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    public static Map<ResourceLocation, DimensionTemperature> dimensionData = new HashMap<>();
    public static List<ResourceLocation> dimensions = new ArrayList<>();
    
    public DimensionTemperatureManager() {
        super(GSON, "metallurgica_utilities/dimension_data");
    }
    
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        dimensionData.clear();
        dimensions.clear();
        
        for (Map.Entry<ResourceLocation, JsonElement> entry : resourceLocationJsonElementMap.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();
            
            try {
                DimensionTemperature dimensionTemperature = DimensionTemperature.CODEC.parse(JsonOps.INSTANCE, entry.getValue()).getOrThrow(true, e -> {
                    throw new IllegalArgumentException("Parsing error loading dimension data " + resourceLocation);
                });
                dimensionData.put(dimensionTemperature.getLoc(), dimensionTemperature);
                dimensions.add(dimensionTemperature.getLoc());
                Metallurgica.LOGGER.info("Loaded dimension data for {} with max height temperature {} and min height temperature {}", dimensionTemperature.getLoc().toString(), dimensionTemperature.getMaxHeightTemperature(), dimensionTemperature.getMinHeightTemperature());
            } catch (IllegalArgumentException | NullPointerException e) {
                Metallurgica.LOGGER.error("Parsing error loading dimension data {}", resourceLocation, e);
            }
            
        }
        Metallurgica.LOGGER.info("Load Complete for {} dimension data", dimensions.size());
    }
    
    public static double getMaxHeightTemperature(ResourceLocation dimension) {
        return dimensionData.get(dimension).getMaxHeightTemperature();
    }
    
    public static double getMinHeightTemperature(ResourceLocation dimension) {
        return dimensionData.get(dimension).getMinHeightTemperature();
    }
    
    public static boolean hasData(ResourceLocation dimension) {
        return dimensionData.containsKey(dimension);
    }
    
    public static List<ResourceLocation> getDimensions() {
        return dimensions;
    }
    
    public static DimensionTemperature getDimensionData(ResourceLocation dimension) {
        return dimensionData.get(dimension);
    }
}
