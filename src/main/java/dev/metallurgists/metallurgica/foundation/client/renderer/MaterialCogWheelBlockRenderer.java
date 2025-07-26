package dev.metallurgists.metallurgica.foundation.client.renderer;

import dev.metallurgists.metallurgica.foundation.data.runtime.MetallurgicaDynamicResourcePack;
import dev.metallurgists.metallurgica.foundation.material.block.MaterialCogWheelBlock;
import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.block.CogWheelFlag;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.block.LargeCogWheelFlag;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

import static dev.metallurgists.metallurgica.foundation.data.runtime.assets.MetallurgicaModels.*;

public class MaterialCogWheelBlockRenderer {
    private static final Set<MaterialCogWheelBlockRenderer> MODELS = new HashSet<>();

    public static void create(MaterialCogWheelBlock block, Material material, FlagKey<?> flagKey) {
        MODELS.add(new MaterialCogWheelBlockRenderer(block, material, flagKey));
    }

    public static void reinitModels() {
        for (MaterialCogWheelBlockRenderer model : MODELS) {
            ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(model.block);
            Material material = model.material;
            FlagKey<?> flagKey = model.flagKey;
            JsonElement obj = getModel(material, flagKey);
            if (obj == null) return;
            MetallurgicaDynamicResourcePack.addBlockModel(new ResourceLocation(material.getNamespace(), blockId.getPath()), obj);
            MetallurgicaDynamicResourcePack.addBlockState(new ResourceLocation(material.getNamespace(), blockId.getPath()), simpleAxisBlockstate("metallurgica:block/" + blockId));
            MetallurgicaDynamicResourcePack.addItemModel(new ResourceLocation(material.getNamespace(), blockId.getPath()), simpleParentedModel(material.getNamespace() + ":block/" + blockId));
        }
    }

    private final MaterialCogWheelBlock block;
    private final Material material;
    private final FlagKey<?> flagKey;

    protected MaterialCogWheelBlockRenderer(MaterialCogWheelBlock block, Material material, FlagKey<?> flagKey) {
        this.block = block;
        this.material = material;
        this.flagKey = flagKey;
    }

    private static JsonElement getModel(Material material, FlagKey<?> flagKey) {
        if (material.getFlag(flagKey) instanceof LargeCogWheelFlag flag) {
            return modelLarge(material, flag);
        } else if (material.getFlag(flagKey) instanceof CogWheelFlag flag) {
            return modelSmall(material, flag);
        }
        return  null;
    }

    private static JsonElement modelLarge(Material material, LargeCogWheelFlag flag) {
        boolean texturePresent = isDeleteMePresent() && Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(material.getNamespace() + ":textures/block/materials/" + material.getName() + "/large_cogwheel.png")).isPresent();
        String texture = texturePresent ? "metallurgica:block/materials/" + material.getName() + "/large_cogwheel" : "metallurgica:block/materials/null/large_cogwheel";
        JsonObject model = new JsonObject();
        model.addProperty("parent", "metallurgica:block/template/cogwheel/"+flag.getCogWheelModelVariant()+"/large");
        JsonObject textures = new JsonObject();
        textures.addProperty("texture", texture);
        model.add("textures", textures);
        return model;
    }

    private static JsonElement modelSmall(Material material, CogWheelFlag flag) {
        boolean texturePresent = isDeleteMePresent() && Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(material.getNamespace() + ":textures/block/materials/" + material.getName() + "/cogwheel.png")).isPresent();
        String texture = texturePresent ? "metallurgica:block/materials/" + material.getName() + "/cogwheel" : "metallurgica:block/materials/null/cogwheel";
        JsonObject model = new JsonObject();
        model.addProperty("parent", "metallurgica:block/template/cogwheel/"+flag.getCogWheelModelVariant()+"/small");
        JsonObject textures = new JsonObject();
        textures.addProperty("texture", texture);
        model.add("textures", textures);
        return model;
    }
}
