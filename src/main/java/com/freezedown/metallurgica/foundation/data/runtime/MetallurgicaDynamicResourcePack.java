package com.freezedown.metallurgica.foundation.data.runtime;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.config.MetallurgicaConfigs;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

import static com.freezedown.metallurgica.foundation.data.runtime.MetallurgicaDynamicDataPack.writeJson;

@SuppressWarnings("unchecked")
public class MetallurgicaDynamicResourcePack implements PackResources {

    protected static final ObjectSet<String> CLIENT_DOMAINS = new ObjectOpenHashSet<>();
    protected static final MetallurgicaDynamicPackContents CONTENTS = new MetallurgicaDynamicPackContents();

    @ApiStatus.Internal
    public static final ConcurrentMap<ResourceLocation, byte[]> DATA = new ConcurrentHashMap<>();

    private final String name;

    static {
        CLIENT_DOMAINS.addAll(Sets.newHashSet(Metallurgica.ID, "minecraft", "forge", "c"));
    }

    public MetallurgicaDynamicResourcePack(String name) {
        this.name = name;
    }

    public static void clearClient() {
        CONTENTS.clearData();
    }

    public static void addBlockModel(ResourceLocation loc, JsonElement obj) {
        ResourceLocation l = getBlockModelLocation(loc);
        //if (MetallurgicaConfigs.common().dev.dumpRecipes.get()) {
        //    Path parent = Metallurgica.getGameDir().resolve("metallurgica/dumped/assets");
        //    writeJson(l, null, parent, obj);
        //}
        CONTENTS.addToData(l, obj.toString().getBytes(StandardCharsets.UTF_8));
    }

    public static void addBlockModel(ResourceLocation loc, Supplier<JsonElement> obj) {
        addBlockModel(loc, obj.get());
    }

    public static void addItemModel(ResourceLocation loc, JsonElement obj) {
        ResourceLocation l = getItemModelLocation(loc);
        //if (MetallurgicaConfigs.common().dev.dumpRecipes.get()) {
        //    Path parent = Metallurgica.getGameDir().resolve("metallurgica/dumped/assets");
        //    writeJson(l, null, parent, obj);
        //}
        CONTENTS.addToData(l, obj.toString().getBytes(StandardCharsets.UTF_8));
    }

    public static void addItemModel(ResourceLocation loc, Supplier<JsonElement> obj) {
        addItemModel(loc, obj.get());
    }

    public static void addBlockState(ResourceLocation loc, JsonElement stateJson) {
        ResourceLocation l = getBlockStateLocation(loc);
        //if (MetallurgicaConfigs.common().dev.dumpRecipes.get()) {
        //    Path parent = Metallurgica.getGameDir().resolve("metallurgica/dumped/assets");
        //    writeJson(l, null, parent, stateJson);
        //}
        CONTENTS.addToData(l, stateJson.toString().getBytes(StandardCharsets.UTF_8));
    }

    public static void addBlockState(ResourceLocation loc, Supplier<JsonElement> generator) {
        addBlockState(loc, generator.get());
    }

    public static void addPartialModel(ResourceLocation loc, JsonElement obj) {
        ResourceLocation l = getBlockModelLocation(loc);
        CONTENTS.addToData(l, obj.toString().getBytes(StandardCharsets.UTF_8));
    }

    public static void addPartialModel(ResourceLocation loc, Supplier<JsonElement> generator) {
        addPartialModel(loc, generator.get());
    }

    public static void addBlockTexture(ResourceLocation loc, byte[] data) {
        ResourceLocation l = getTextureLocation("block", loc);
        //if (MetallurgicaConfigs.common().dev.dumpRecipes.get()) {
        //    Path parent = Metallurgica.getGameDir().resolve("metallurgica/dumped/assets");
        //    writeByteArray(l, null, parent, data);
        //}
        CONTENTS.addToData(l, data);
    }

    public static void addItemTexture(ResourceLocation loc, byte[] data) {
        ResourceLocation l = getTextureLocation("item", loc);
        //if (MetallurgicaConfigs.common().dev.dumpRecipes.get()) {
        //    Path parent = Metallurgica.getGameDir().resolve("metallurgica/dumped/assets");
        //    writeByteArray(l, null, parent, data);
        //}
        CONTENTS.addToData(l, data);
    }

    @ApiStatus.Internal
    public static void writeByteArray(ResourceLocation id, @Nullable String subdir, Path parent, byte[] data) {
        try {
            Path file;
            if (subdir != null) {
                file = parent.resolve(id.getNamespace()).resolve(subdir).resolve(id.getPath() + ".png"); // assume PNG
            } else {
                file = parent.resolve(id.getNamespace()).resolve(id.getPath()); // assume the file type is also appended
                // if a full path is given.
            }
            Files.createDirectories(file.getParent());
            try (OutputStream output = Files.newOutputStream(file)) {
                output.write(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getRootResource(String... elements) {
        return null;
    }

    @Override
    public IoSupplier<InputStream> getResource(PackType type, ResourceLocation location) {
        if (type == PackType.CLIENT_RESOURCES) {
            return CONTENTS.getResource(location);
        }
        return null;
    }

    @Override
    public void listResources(PackType packType, String namespace, String path, ResourceOutput resourceOutput) {
        if (packType == PackType.CLIENT_RESOURCES) {
            CONTENTS.listResources(namespace, path, resourceOutput);
        }
    }

    @Override
    public Set<String> getNamespaces(PackType type) {
        return type == PackType.CLIENT_RESOURCES ? CLIENT_DOMAINS : Set.of();
    }

    @Nullable
    @Override
    public <T> T getMetadataSection(MetadataSectionSerializer<T> metaReader) {
        if (metaReader == PackMetadataSection.TYPE) {
            return (T) new PackMetadataSection(Component.literal("Metallurgica dynamic assets"),
                    SharedConstants.getCurrentVersion().getPackVersion(PackType.CLIENT_RESOURCES));
        }
        return null;
    }

    @Override
    public String packId() {
        return this.name;
    }

    @Override
    public void close() {
    }

    public static ResourceLocation getBlockStateLocation(ResourceLocation blockId) {
        return new ResourceLocation(blockId.getNamespace(),
                String.join("", "blockstates/", blockId.getPath(), ".json"));
    }

    public static ResourceLocation getModelLocation(ResourceLocation id) {
        return new ResourceLocation(id.getNamespace(), String.join("", "models/", id.getPath(), ".json"));
    }

    public static ResourceLocation getBlockModelLocation(ResourceLocation blockId) {
        return new ResourceLocation(blockId.getNamespace(), String.join("", "models/block/", blockId.getPath(), ".json"));
    }

    public static ResourceLocation getItemModelLocation(ResourceLocation itemId) {
        return new ResourceLocation(itemId.getNamespace(), String.join("", "models/item/", itemId.getPath(), ".json"));
    }

    public static ResourceLocation getTextureLocation(@Nullable String path, ResourceLocation tagId) {
        if (path == null) {
            return new ResourceLocation(tagId.getNamespace(), String.join("", "textures/", tagId.getPath(), ".png"));
        }
        return new ResourceLocation(tagId.getNamespace(),
                String.join("", "textures/", path, "/", tagId.getPath(), ".png"));
    }
}
