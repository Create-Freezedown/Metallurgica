package dev.metallurgists.metallurgica.foundation.data.custom.composition.tooltip;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.foundation.data.custom.composition.MaterialComposition;
import dev.metallurgists.metallurgica.infastructure.material.Material;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaterialCompositionManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static Map<Material, MaterialComposition> materialCompositions = new HashMap<>();

    @Getter
    public static List<Material> materials = new ArrayList<>();

    public MaterialCompositionManager() {
        super(GSON, "metallurgica_utilities/material_compositions");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        materialCompositions.clear();
        materials.clear();

        for (Map.Entry<ResourceLocation, JsonElement> entry : resourceLocationJsonElementMap.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();

            if (resourceLocation.getPath().startsWith("_")) {
                continue;
            }

            try {
                MaterialComposition materialComposition = MaterialComposition.CODEC.parse(JsonOps.INSTANCE, entry.getValue()).getOrThrow(true, Metallurgica.LOGGER::error);
                if (materialComposition != null) {
                    materialCompositions.put(materialComposition.material(), materialComposition);
                    materials.add(materialComposition.material());
                }
            } catch (IllegalArgumentException | JsonParseException jsonParseException) {
                Metallurgica.LOGGER.error("Parsing error loading material compositions {}", resourceLocation, jsonParseException);
            }
        }
        Metallurgica.LOGGER.info("Load Complete for {} material compositions", materialCompositions.size());
    }


    public static boolean hasComposition(Material material) {
        return materialCompositions.containsKey(material);
    }


    public static MaterialComposition getComposition(Material material) {
        return materialCompositions.get(material);
    }

    public static List<SubComposition> getSubCompositions(Material material) {
        MaterialComposition materialComposition = getComposition(material);
        return materialComposition.compositions();
    }
}
