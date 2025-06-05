package com.freezedown.metallurgica.foundation.data.custom.composition.tooltip;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.data.custom.composition.ItemComposition;
import com.freezedown.metallurgica.infastructure.element.data.SubComposition;
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
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompositionManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    
    public static Map<Item, ItemComposition> compositions = new HashMap<>();
    
    @Getter
    public static List<Item> items = new ArrayList<>();
    
    public CompositionManager() {
        super(GSON, "metallurgica_utilities/compositions");
    }
    
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        compositions.clear();
        items.clear();
        
        for (Map.Entry<ResourceLocation, JsonElement> entry : resourceLocationJsonElementMap.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();
            
            if (resourceLocation.getPath().startsWith("_")) {
                continue;
            }
            
            try {
                ItemComposition itemComposition = ItemComposition.CODEC.parse(JsonOps.INSTANCE, entry.getValue()).getOrThrow(true, Metallurgica.LOGGER::error);
                if (itemComposition != null) {
                    compositions.put(itemComposition.item(), itemComposition);
                    items.add(itemComposition.item());
                }
            } catch (IllegalArgumentException | JsonParseException jsonParseException) {
                Metallurgica.LOGGER.error("Parsing error loading compositions {}", resourceLocation, jsonParseException);
            }
        }
        Metallurgica.LOGGER.info("Load Complete for {} compositions", compositions.size());
    }
    
    public static boolean hasComposition(Item item) {
        return compositions.containsKey(item);
    }
    
    public static ItemComposition getComposition(Item item) {
        return compositions.get(item);
    }

    public static List<SubComposition> getSubCompositions(Item item) {
        ItemComposition itemComposition = getComposition(item);
        return itemComposition.compositions();
    }
}
