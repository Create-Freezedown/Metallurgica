package com.freezedown.metallurgica.foundation.data.custom.temp.biome;

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

public class BiomeTemperatureManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    public static Map<ResourceLocation, BiomeTemperature> biomeTemperatures = new HashMap<>();
    public static List<ResourceLocation> biomes = new ArrayList<>();
    
    public BiomeTemperatureManager() {
        super(GSON, "metallurgica_utilities/biome_temperatures");
    }
    
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        biomeTemperatures.clear();
        biomes.clear();
        
        for (Map.Entry<ResourceLocation, JsonElement> entry : resourceLocationJsonElementMap.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();
            
            try {
                BiomeTemperature biomeTemperature = BiomeTemperature.CODEC.parse(JsonOps.INSTANCE, entry.getValue()).getOrThrow(true, e -> {
                    throw new IllegalArgumentException("Parsing error loading biome temperatures " + resourceLocation);
                });
                biomeTemperatures.put(biomeTemperature.getLoc(), biomeTemperature);
                biomes.add(biomeTemperature.getLoc());
                Metallurgica.LOGGER.info("Loaded biome temperature for {} with temperature {}", biomeTemperature.getLoc().toString(), biomeTemperature.getSurfaceTemperature());
            } catch (IllegalArgumentException | NullPointerException e) {
                Metallurgica.LOGGER.error("Parsing error loading biome temperatures {}", resourceLocation, e);
            }
            
        }
        Metallurgica.LOGGER.info("Load Complete for {} biomes", biomes.size());
    }
    
    public static double getTemperature(ResourceLocation biome) {
        if (biomeTemperatures.get(biome) == null) {
            return 23.0;
        }
        return biomeTemperatures.get(biome).getSurfaceTemperature();
    }
    
    public static List<ResourceLocation> getBlendingBlacklist(ResourceLocation biome) {
        return biomeTemperatures.get(biome).getTemperatureBlendBlacklist();
    }
}
