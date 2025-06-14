package com.freezedown.metallurgica.foundation.data.runtime.assets;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.data.runtime.MetallurgicaDynamicResourcePack;
import com.freezedown.metallurgica.foundation.material.registry.Material;
import com.freezedown.metallurgica.foundation.material.registry.flags.fluid.MoltenFlag;
import com.freezedown.metallurgica.foundation.material.registry.flags.other.CableFlag;
import com.freezedown.metallurgica.foundation.material.registry.flags.FlagKey;
import com.freezedown.metallurgica.foundation.material.registry.flags.block.StorageBlockFlag;
import com.freezedown.metallurgica.foundation.material.registry.flags.base.ISpecialAssetLocation;
import com.freezedown.metallurgica.foundation.material.registry.flags.base.ItemFlag;
import com.freezedown.metallurgica.registry.material.MetMaterials;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

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

    private static boolean isDeleteMePresent() {
        return Minecraft.getInstance().getResourceManager().getResource(Metallurgica.asResource("textures/deleteme.png")).isPresent();
    }

    private static void generateItemModels(Material material, FlagKey<?> flagKey) {
        if (material.getFlag(flagKey) instanceof ItemFlag itemFlag) {
            String textureName = getFlagName(flagKey);
            if (material.getFlag(flagKey) instanceof ISpecialAssetLocation specialAssetLoc) {
                textureName = specialAssetLoc.getAssetName();
            }
            boolean texturePresent = isDeleteMePresent() && Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(material.getModid() + ":textures/item/materials/" + material.getName() + "/" + textureName + ".png")).isPresent();
            String texture = texturePresent ? "metallurgica:item/materials/" + material.getName() + "/" + textureName : "metallurgica:item/materials/null/" + textureName;
            MetallurgicaDynamicResourcePack.addItemModel(new ResourceLocation(material.getModid(), itemFlag.getIdPattern().formatted(material.getName())), simpleGeneratedModel("minecraft:item/generated", texture));
        } else if (material.getFlag(flagKey) instanceof CableFlag) {
            String textureName = getFlagName(FlagKey.CABLE);
            boolean texturePresent = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(material.getModid() + ":textures/item/materials/" + material.getName() + "/" + textureName + ".png")).isPresent();
            String texture = texturePresent ? "metallurgica:item/materials/" + material.getName() + "/" + textureName : "metallurgica:item/materials/null/" + textureName;
            MetallurgicaDynamicResourcePack.addItemModel(new ResourceLocation(material.getModid() + ":%s_cable".formatted(material.getName())), simpleGeneratedModel("minecraft:item/generated", texture));
        }
    }

    private static void generateCubeBlockModel(Material material, FlagKey<?> flagKey) {
        if (material.getFlag(flagKey) instanceof StorageBlockFlag blockFlag) {
            if (blockFlag.isUseColumnModel()) {
                boolean sidePresent = isDeleteMePresent() && Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(material.getModid() + ":textures/block/materials/" + material.getName() + "/block_side.png")).isPresent();
                boolean endPresent = isDeleteMePresent() && Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(material.getModid() + ":textures/block/materials/" + material.getName() + "/block_end.png")).isPresent();
                String sideTexture = sidePresent ? material.getModid() + ":block/materials/" + material.getName() + "/block_side" : "metallurgica:block/materials/null/block_side";
                String endTexture = endPresent ? material.getModid() + ":block/materials/" + material.getName() + "/block_end" : "metallurgica:block/materials/null/block_end";
                MetallurgicaDynamicResourcePack.addBlockModel(new ResourceLocation(material.getModid(), blockFlag.getIdPattern().formatted(material.getName())), simplePillar(endTexture, sideTexture));
                MetallurgicaDynamicResourcePack.addBlockState(new ResourceLocation(material.getModid(), blockFlag.getIdPattern().formatted(material.getName())), simpleAxisBlockstate("metallurgica:block/" + blockFlag.getIdPattern().formatted(material.getName())));
            } else {
                boolean texturePresent = isDeleteMePresent() && Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(material.getModid() + ":textures/block/materials/" + material.getName() + "/block.png")).isPresent();
                String texture = texturePresent ? material.getModid() + ":block/materials/" + material.getName() + "/block" : "metallurgica:block/materials/null/block";
                MetallurgicaDynamicResourcePack.addBlockModel(new ResourceLocation(material.getModid(), blockFlag.getIdPattern().formatted(material.getName())), simpleCubeAll(texture));
                MetallurgicaDynamicResourcePack.addBlockState(new ResourceLocation(material.getModid(), blockFlag.getIdPattern().formatted(material.getName())), singleVariantBlockstate(material.getModid() + ":block/" + blockFlag.getIdPattern().formatted(material.getName())));
            }
            MetallurgicaDynamicResourcePack.addItemModel(new ResourceLocation(material.getModid(), blockFlag.getIdPattern().formatted(material.getName())), simpleParentedModel(material.getModid() + ":block/" + blockFlag.getIdPattern().formatted(material.getName())));
        }
    }

    private static void generateMoltenBucket(Material material, FlagKey<?> flagKey) {
        if (material.getFlag(flagKey) instanceof MoltenFlag moltenFlag) {
            MetallurgicaDynamicResourcePack.addItemModel(Metallurgica.asResource("molten_%s_bucket".formatted(material.getName())), simpleGeneratedModel("minecraft:item/generated", "metallurgica:item/molten_metal_bucket"));
        }
    }

    public static String getFlagName(FlagKey<?> flagKey) {
        return flagKey.toString();
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

    public static JsonObject simplePillar(String end, String side) {
        JsonObject model = new JsonObject();
        model.addProperty("parent", "minecraft:block/cube_column");
        JsonObject textures = new JsonObject();
        textures.addProperty("end", end);
        textures.addProperty("side", side);
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

    private static JsonObject simpleAxisBlockstate(String model) {
        JsonObject blockstate = new JsonObject();
        JsonObject variants = new JsonObject();
        JsonObject variantX = new JsonObject();
        JsonObject variantY = new JsonObject();
        JsonObject variantZ = new JsonObject();
        variantX.addProperty("model", model);
        variantX.addProperty("x", 90);
        variantX.addProperty("y", 90);
        variantY.addProperty("model", model);
        variantZ.addProperty("model", model);
        variantZ.addProperty("x", 90);
        variants.add("axis=x", variantX);
        variants.add("axis=y", variantY);
        variants.add("axis=z", variantZ);
        blockstate.add("variants", variants);
        return blockstate;
    }
}
