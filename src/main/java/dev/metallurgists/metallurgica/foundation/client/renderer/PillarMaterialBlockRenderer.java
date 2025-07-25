package dev.metallurgists.metallurgica.foundation.client.renderer;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.foundation.data.runtime.MetallurgicaDynamicResourcePack;
import dev.metallurgists.metallurgica.foundation.data.runtime.assets.MetallurgicaModels;
import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.model.DelegatedModel;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.HashSet;
import java.util.Set;

public class PillarMaterialBlockRenderer {
    private static final Set<PillarMaterialBlockRenderer> MODELS = new HashSet<>();

    public static void create(Block block, Material material, FlagKey<?> flagKey) {
        MODELS.add(new PillarMaterialBlockRenderer(block, material, flagKey));
    }

    public static void reinitModels() {
        for (PillarMaterialBlockRenderer model : MODELS) {
            ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(model.block);
            String blockName = model.flagKey.toString();
            boolean sidePresent = Minecraft.getInstance().getResourceManager().getResource(Metallurgica.asResource("textures/block/materials/" + model.material.getName() + "/%s_side.png".formatted(blockName))).isPresent();
            boolean endPresent = Minecraft.getInstance().getResourceManager().getResource(Metallurgica.asResource("textures/block/materials/" + model.material.getName() + "/%s_end.png".formatted(blockName))).isPresent();
            String side = sidePresent ? "metallurgica:block/materials/" + model.material.getName() + "/" + blockName + "_side" : "metallurgica:block/materials/null/" + blockName + "_side";
            String end = endPresent ? "metallurgica:block/materials/" + model.material.getName() + "/" + blockName + "_end" : "metallurgica:block/materials/null/" + blockName + "_end";
            ResourceLocation modelId = blockId.withPrefix("block/");
            MetallurgicaDynamicResourcePack.addBlockModel(blockId, MetallurgicaModels.simplePillar(end, side));
            MetallurgicaDynamicResourcePack.addBlockState(blockId, BlockModelGenerators.createSimpleBlock(model.block, modelId));
            MetallurgicaDynamicResourcePack.addItemModel(BuiltInRegistries.ITEM.getKey(model.block.asItem()), new DelegatedModel(ModelLocationUtils.getModelLocation(model.block)));
        }
    }

    private final Block block;
    private final Material material;
    private final FlagKey<?> flagKey;

    protected PillarMaterialBlockRenderer(Block block, Material material, FlagKey<?> flagKey) {
        this.block = block;
        this.material = material;
        this.flagKey = flagKey;
    }
}
