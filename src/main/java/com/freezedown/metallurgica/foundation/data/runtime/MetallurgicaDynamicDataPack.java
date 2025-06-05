package com.freezedown.metallurgica.foundation.data.runtime;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.config.MetallurgicaConfigs;
import com.freezedown.metallurgica.foundation.data.custom.composition.FinishedComposition;
import com.freezedown.metallurgica.foundation.data.runtime.recipe.MetallurgicaRecipes;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.SharedConstants;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@SuppressWarnings("unchecked")
@ParametersAreNonnullByDefault
public class MetallurgicaDynamicDataPack implements PackResources {
    protected static final ObjectSet<String> SERVER_DOMAINS = new ObjectOpenHashSet<>();
    protected static final Map<ResourceLocation, byte[]> DATA = new HashMap<>();

    private final String name;

    static {
        SERVER_DOMAINS.addAll(Sets.newHashSet(Metallurgica.ID, "minecraft", "forge", "c"));
    }

    public MetallurgicaDynamicDataPack(String name) {
        this.name = name;
    }

    public static void clearServer() {
        DATA.clear();
    }

    @Override
    public @Nullable IoSupplier<InputStream> getRootResource(String... strings) {
        return null;
    }

    @Override
    public @Nullable IoSupplier<InputStream> getResource(PackType packType, ResourceLocation resourceLocation) {
        if (packType == PackType.SERVER_DATA) {
            var byteArray = DATA.get(resourceLocation);
            if (byteArray != null)
                return () -> new ByteArrayInputStream(byteArray);
            else return null;
        } else {
            return null;
        }
    }

    @Override
    public void listResources(PackType packType, String namespace, String path, ResourceOutput resourceOutput) {
        if (packType == PackType.SERVER_DATA) {
            if (!path.endsWith("/")) path += "/";
            final String finalPath = path;
            DATA.keySet().stream().filter(Objects::nonNull).filter(loc -> loc.getPath().startsWith(finalPath))
                    .forEach((id) -> {
                        IoSupplier<InputStream> resource = this.getResource(packType, id);
                        if (resource != null) {
                            resourceOutput.accept(id, resource);
                        }
                    });
        }
    }

    @Override
    public @NotNull Set<String> getNamespaces(PackType packType) {
        return packType == PackType.SERVER_DATA ? SERVER_DOMAINS : Set.of();
    }

    @Override
    public @Nullable <T> T getMetadataSection(MetadataSectionSerializer<T> metadataSectionSerializer) throws IOException {
        if (metadataSectionSerializer == PackMetadataSection.TYPE) {
            return (T) new PackMetadataSection(Component.literal("Metallurgica dynamic data"),
                    SharedConstants.getCurrentVersion().getPackVersion(PackType.SERVER_DATA));
        } else if (metadataSectionSerializer.getMetadataSectionName().equals("filter")) {
            JsonObject filter = new JsonObject();
            JsonArray block = new JsonArray();
            MetallurgicaRecipes.RECIPE_FILTERS.forEach((id) -> { // Collect removed recipes in here, in the pack filter section.
                JsonObject entry = new JsonObject();
                entry.addProperty("namespace", "^" + id.getNamespace().replaceAll("[\\W]", "\\\\$0") + "$");
                entry.addProperty("path", "^recipes/" + id.getPath().replaceAll("[\\W]", "\\\\$0") + "\\.json" + "$");
                block.add(entry);
            });
            filter.add("block", block);
            return metadataSectionSerializer.fromJson(filter);
        }
        return null;
    }

    @Override
    public @NotNull String packId() {
        return this.name;
    }

    @Override
    public void close() {

    }

    @ApiStatus.Internal
    public static void writeJson(ResourceLocation id, @Nullable String subdir, Path parent, JsonElement json) {
        try {
            Path file;
            if (subdir != null) {
                file = parent.resolve(id.getNamespace()).resolve(subdir).resolve(id.getPath() + ".json"); // assume JSON
            } else {
                file = parent.resolve(id.getNamespace()).resolve(id.getPath()); // assume the file type is also appended
                // if a full path is given.
            }
            Files.createDirectories(file.getParent());
            try (OutputStream output = Files.newOutputStream(file)) {
                output.write(json.toString().getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addRecipe(FinishedRecipe recipe) {
        JsonObject recipeJson = recipe.serializeRecipe();
        ResourceLocation recipeId = recipe.getId();
        Path parent = Metallurgica.getGameDir().resolve("metallurgica/dumped/data");
        if (MetallurgicaConfigs.common().dev.dumpRecipes.get()) {
            writeJson(recipeId, "recipes", parent, recipeJson);
        }
        DATA.put(getRecipeLocation(recipeId), recipeJson.toString().getBytes(StandardCharsets.UTF_8));
        if (recipe.serializeAdvancement() != null) {
            JsonObject advancement = recipe.serializeAdvancement();
            DATA.put(getAdvancementLocation(Objects.requireNonNull(recipe.getAdvancementId())), advancement.toString().getBytes(StandardCharsets.UTF_8));
        }
    }

    public static void addAdvancement(ResourceLocation loc, JsonObject obj) {
        ResourceLocation l = getAdvancementLocation(loc);
        synchronized (DATA) {
            DATA.put(l, obj.toString().getBytes(StandardCharsets.UTF_8));
        }
    }

    public static void addComposition(FinishedComposition composition) {
        JsonObject compositionJson = composition.serializeComposition();
        ResourceLocation compositionId = composition.getId();
        Path parent = Metallurgica.getGameDir().resolve("metallurgica/dumped/data");
        if (MetallurgicaConfigs.common().dev.dumpCompositions.get()) {
            writeJson(compositionId, "compositions", parent, compositionJson);
        }
        DATA.put(getCompositionLocation(compositionId), compositionJson.toString().getBytes(StandardCharsets.UTF_8));
    }

    public static ResourceLocation getRecipeLocation(ResourceLocation recipeId) {
        return new ResourceLocation(recipeId.getNamespace(), String.join("", "recipes/", recipeId.getPath(), ".json"));
    }

    public static ResourceLocation getAdvancementLocation(ResourceLocation advancementId) {
        return new ResourceLocation(advancementId.getNamespace(),
                String.join("", "advancements/", advancementId.getPath(), ".json"));
    }

    public static ResourceLocation getTagLocation(String identifier, ResourceLocation tagId) {
        return new ResourceLocation(tagId.getNamespace(),
                String.join("", "tags/", identifier, "/", tagId.getPath(), ".json"));
    }

    public static ResourceLocation getCompositionLocation(ResourceLocation compId) {
        return new ResourceLocation(compId.getNamespace(), String.join("", "metallurgica_utilities/compositions/", compId.getPath(), ".json"));
    }
}
