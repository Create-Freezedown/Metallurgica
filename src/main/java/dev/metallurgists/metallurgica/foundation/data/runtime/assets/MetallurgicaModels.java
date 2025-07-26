package dev.metallurgists.metallurgica.foundation.data.runtime.assets;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.foundation.data.runtime.MetallurgicaDynamicResourcePack;
import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.interfaces.IPartialHolder;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.interfaces.ISpecialAssetGen;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.fluid.MoltenFlag;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.block.StorageBlockFlag;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.ISpecialAssetLocation;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.ItemFlag;
import dev.metallurgists.metallurgica.registry.material.MetMaterials;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class MetallurgicaModels {

    public static void registerMaterialAssets() {
        for (Material material : MetMaterials.registeredMaterials.values()) {
            material.getFlags().getFlagKeys().forEach(flagKey -> {
                generateSpecialAssets(material, flagKey);
                generateItemModels(material, flagKey);
                generateMoltenBucket(material, flagKey);
                generateCubeBlockModel(material, flagKey);
                generatePartialModels(material, flagKey);
            });
        }
    }

    public static boolean isDeleteMePresent() {
        return Minecraft.getInstance().getResourceManager().getResource(Metallurgica.asResource("textures/deleteme.png")).isPresent();
    }

    private static void generateSpecialAssets(Material material, FlagKey<?> flagKey) {
        if (material.getFlag(flagKey) instanceof ISpecialAssetGen specialAssetGen) {
            specialAssetGen.generateAssets(material);
        }
    }

    private static void generateItemModels(Material material, FlagKey<?> flagKey) {
        if (material.getFlag(flagKey) instanceof ItemFlag itemFlag) {
            String textureName = getFlagName(flagKey);
            if (material.getFlag(flagKey) instanceof ISpecialAssetLocation specialAssetLoc) {
                textureName = specialAssetLoc.getAssetName();
            }
            boolean texturePresent = isDeleteMePresent() && Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(material.getNamespace() + ":textures/item/materials/" + material.getName() + "/" + textureName + ".png")).isPresent();
            String texture = texturePresent ? "metallurgica:item/materials/" + material.getName() + "/" + textureName : "metallurgica:item/materials/null/" + textureName;
            MetallurgicaDynamicResourcePack.addItemModel(new ResourceLocation(material.getNamespace(), itemFlag.getIdPattern().formatted(material.getName())), simpleGeneratedModel("minecraft:item/generated", texture));
        }
    }

    private static void generateCubeBlockModel(Material material, FlagKey<?> flagKey) {
        if (material.getFlag(flagKey) instanceof StorageBlockFlag blockFlag) {
            if (blockFlag.isUseColumnModel()) {
                boolean sidePresent = isDeleteMePresent() && Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(material.getNamespace() + ":textures/block/materials/" + material.getName() + "/" + flagKey.toString() + "_side.png")).isPresent();
                boolean endPresent = isDeleteMePresent() && Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(material.getNamespace() + ":textures/block/materials/" + material.getName() + "/" + flagKey.toString() + "_end.png")).isPresent();
                String sideTexture = sidePresent ? material.getNamespace() + ":block/materials/" + material.getName() + "/" + flagKey + "_side" : "metallurgica:block/materials/null/" + flagKey.toString() + "_side";
                String endTexture = endPresent ? material.getNamespace() + ":block/materials/" + material.getName() + "/" + flagKey + "_end" : "metallurgica:block/materials/null/" + flagKey + "_end";
                MetallurgicaDynamicResourcePack.addBlockModel(new ResourceLocation(material.getNamespace(), blockFlag.getIdPattern().formatted(material.getName())), simplePillar(endTexture, sideTexture));
                MetallurgicaDynamicResourcePack.addBlockState(new ResourceLocation(material.getNamespace(), blockFlag.getIdPattern().formatted(material.getName())), simpleAxisBlockstate("metallurgica:block/" + blockFlag.getIdPattern().formatted(material.getName())));
            } else {
                boolean texturePresent = isDeleteMePresent() && Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(material.getNamespace() + ":textures/block/materials/" + material.getName() + "/" + flagKey.toString() + ".png")).isPresent();
                String texture = texturePresent ? material.getNamespace() + ":block/materials/" + material.getName() + "/" + flagKey : "metallurgica:block/materials/null/" + flagKey.toString();
                MetallurgicaDynamicResourcePack.addBlockModel(new ResourceLocation(material.getNamespace(), blockFlag.getIdPattern().formatted(material.getName())), simpleCubeAll(texture));
                MetallurgicaDynamicResourcePack.addBlockState(new ResourceLocation(material.getNamespace(), blockFlag.getIdPattern().formatted(material.getName())), singleVariantBlockstate(material.getNamespace() + ":block/" + blockFlag.getIdPattern().formatted(material.getName())));
            }
            MetallurgicaDynamicResourcePack.addItemModel(new ResourceLocation(material.getNamespace(), blockFlag.getIdPattern().formatted(material.getName())), simpleParentedModel(material.getNamespace() + ":block/" + blockFlag.getIdPattern().formatted(material.getName())));
        }
    }

    private static void generateMoltenBucket(Material material, FlagKey<?> flagKey) {
        if (material.getFlag(flagKey) instanceof MoltenFlag moltenFlag) {
            MetallurgicaDynamicResourcePack.addItemModel(Metallurgica.asResource("molten_%s_bucket".formatted(material.getName())), simpleGeneratedModel("minecraft:item/generated", "metallurgica:item/molten_metal_bucket"));
        }
    }

    private static void generatePartialModels(Material material, FlagKey<?> flagKey) {
        var flag = material.getFlag(flagKey);
        if (flag instanceof IPartialHolder partialHolder) {
            MetallurgicaDynamicResourcePack.addPartialModel(partialHolder.getModelLocation(material), partialHolder.createModel(material));
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

    public static JsonObject simpleParentedModel(String parent) {
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

    public static JsonObject simpleAxisBlockstate(String model) {
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
