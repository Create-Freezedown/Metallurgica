package dev.metallurgists.metallurgica.foundation.util.recipe.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TagPreferenceManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    
    public static List<String> tagPreferences = new ArrayList<>();
    
    
    public TagPreferenceManager() {
        super(GSON, "metallurgica_utilities");
    }
    
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        tagPreferences.clear();
        
        for (Map.Entry<ResourceLocation, JsonElement> entry : resourceLocationJsonElementMap.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();
            
            if (resourceLocation.getPath().startsWith("_") || !canContinue(entry)) {
                continue;
            }
            
            try {
                TagPref tagPreference = TagPref.CODEC.parse(JsonOps.INSTANCE, entry.getValue()).getOrThrow(true, e -> {
                    throw new IllegalArgumentException("Parsing error loading tag preferences " + resourceLocation);
                });
                tagPreferences.addAll(tagPreference.tagPreferences());
            } catch (IllegalArgumentException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static List<String> getTagPreferences() {
        return tagPreferences;
    }
    
    //The file MUST be named "tag_preferences.json"
    public static boolean canContinue(Map.Entry<ResourceLocation, JsonElement> entry) {
        return removeExtension(entry.getKey()).equals("tag_preferences");
    }
    
    public static String removeExtension(ResourceLocation resourceLocation) {
        String path = resourceLocation.getPath(); // Get the full path from ResourceLocation
        String[] pathElements = path.split("/");
        return pathElements[pathElements.length - 1];
    }
}
