package com.freezedown.metallurgica.foundation.data.runtime.assets;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.data.runtime.MetallurgicaDynamicResourcePack;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.CableFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey;
import com.freezedown.metallurgica.foundation.item.registry.flags.FluidFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.StorageBlockFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.ItemFlag;
import com.freezedown.metallurgica.registry.material.MetMaterials;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;

public class MetallurgicaModels {

    public static void registerMaterialAssets() {
        for (Material material : MetMaterials.registeredMaterials.values()) {
            material.getFlags().getFlagKeys().forEach(flagKey -> {
                generateItemModels(material, flagKey);
                generateMoltenBucket(material, flagKey);
                generateCubeBlockModel(material, flagKey);
            });
        }
    }

    private static void generateItemModels(Material material, FlagKey<?> flagKey) {
        if (material.getFlag(flagKey) instanceof ItemFlag itemFlag) {
            String textureName = getFlagName(itemFlag.getIdPattern());
            boolean texturePresent = Minecraft.getInstance().getResourceManager().getResource(Metallurgica.asResource("textures/item/materials/" + material.getName() + "/" + textureName + ".png")).isPresent();
            String texture = texturePresent ? "metallurgica:item/materials/" + material.getName() + "/" + textureName : "metallurgica:item/materials/null/" + textureName;
            MetallurgicaDynamicResourcePack.addItemModel(Metallurgica.asResource(itemFlag.getIdPattern().formatted(material.getName())), simpleGeneratedModel("minecraft:item/generated", texture));
        } else if (material.getFlag(flagKey) instanceof CableFlag) {
            String textureName = getFlagName("%s_cable");
            boolean texturePresent = Minecraft.getInstance().getResourceManager().getResource(Metallurgica.asResource("textures/item/materials/" + material.getName() + "/" + textureName + ".png")).isPresent();
            String texture = texturePresent ? "metallurgica:item/materials/" + material.getName() + "/" + textureName : "metallurgica:item/materials/null/" + textureName;
            MetallurgicaDynamicResourcePack.addItemModel(Metallurgica.asResource("%s_cable".formatted(material.getName())), simpleGeneratedModel("minecraft:item/generated", texture));
        }
    }

    private static void generateCubeBlockModel(Material material, FlagKey<?> flagKey) {
        if (material.getFlag(flagKey) instanceof StorageBlockFlag blockFlag) {
            boolean texturePresent = Minecraft.getInstance().getResourceManager().getResource(Metallurgica.asResource("textures/block/materials/" + material.getName() + "/block.png")).isPresent();
            String texture = texturePresent ? "metallurgica:block/materials/" + material.getName() + "/block" : "metallurgica:block/materials/null/block";
            MetallurgicaDynamicResourcePack.addBlockModel(Metallurgica.asResource(blockFlag.getIdPattern().formatted(material.getName())), simpleCubeAll(texture));
            MetallurgicaDynamicResourcePack.addBlockState(Metallurgica.asResource(blockFlag.getIdPattern().formatted(material.getName())), singleVariantBlockstate("metallurgica:block/" + blockFlag.getIdPattern().formatted(material.getName())));
            MetallurgicaDynamicResourcePack.addItemModel(Metallurgica.asResource(blockFlag.getIdPattern().formatted(material.getName())), simpleParentedModel("metallurgica:block/" + blockFlag.getIdPattern().formatted(material.getName())));
        }
    }

    private static void generateMoltenBucket(Material material, FlagKey<?> flagKey) {
        if (material.getFlag(flagKey) instanceof FluidFlag fluidFlag) {
            if (material.hasFlag(FlagKey.INGOT)) {
                MetallurgicaDynamicResourcePack.addItemModel(Metallurgica.asResource("molten_%s_bucket".formatted(material.getName())), simpleGeneratedModel("minecraft:item/generated", "metallurgica:item/molten_metal_bucket"));
            }
        }
    }

    public static String getFlagName(String idPattern) {
        String textureName = idPattern.formatted("");
        if (textureName.contains("__")) textureName = textureName.replace("__", "_");
        if (textureName.startsWith("_")) textureName = textureName.substring(1);
        if (textureName.endsWith("_")) textureName = textureName.substring(0, textureName.length() - 1);
        return textureName;
    }

    private static JsonObject simpleGeneratedModel(String parent, String texture) {
        JsonObject model = new JsonObject();
        model.addProperty("parent", parent);
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", texture);
        model.add("textures", textures);
        return model;
    }

    private static JsonObject simpleParentedModel(String parent) {
        JsonObject model = new JsonObject();
        model.addProperty("parent", parent);
        return model;
    }

    public static JsonObject simpleCubeAll(String texture) {
        JsonObject model = new JsonObject();
        model.addProperty("parent", "minecraft:block/cube_all");
        JsonObject textures = new JsonObject();
        textures.addProperty("all", texture);
        model.add("textures", textures);
        return model;
    }

    private static JsonObject singleVariantBlockstate(String model) {
        JsonObject blockstate = new JsonObject();
        JsonObject variants = new JsonObject();
        JsonObject variant = new JsonObject();
        variant.addProperty("model", model);
        variants.add("", variant);
        blockstate.add("variants", variants);
        return blockstate;
    }
}
