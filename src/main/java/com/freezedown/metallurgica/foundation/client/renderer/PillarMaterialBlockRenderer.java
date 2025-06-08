package com.freezedown.metallurgica.foundation.client.renderer;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.data.runtime.MetallurgicaDynamicResourcePack;
import com.freezedown.metallurgica.foundation.data.runtime.assets.MetallurgicaModels;
import com.freezedown.metallurgica.foundation.item.registry.Material;
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

    public static void create(Block block, Material material) {
        MODELS.add(new PillarMaterialBlockRenderer(block, material));
    }

    public static void reinitModels() {
        for (PillarMaterialBlockRenderer model : MODELS) {
            ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(model.block);
            boolean sidePresent = Minecraft.getInstance().getResourceManager().getResource(Metallurgica.asResource("textures/block/materials/" + model.material.getName() + "/block_side.png")).isPresent();
            boolean endPresent = Minecraft.getInstance().getResourceManager().getResource(Metallurgica.asResource("textures/block/materials/" + model.material.getName() + "/block_end.png")).isPresent();
            String side = sidePresent ? "metallurgica:block/materials/" + model.material.getName() + "/block_side" : "metallurgica:block/materials/null/block_side";
            String end = endPresent ? "metallurgica:block/materials/" + model.material.getName() + "/block_end" : "metallurgica:block/materials/null/block_end";
            ResourceLocation modelId = blockId.withPrefix("block/");
            MetallurgicaDynamicResourcePack.addBlockModel(modelId, MetallurgicaModels.simplePillar(end, side));
            MetallurgicaDynamicResourcePack.addBlockState(blockId, BlockModelGenerators.createSimpleBlock(model.block, modelId));
            MetallurgicaDynamicResourcePack.addItemModel(BuiltInRegistries.ITEM.getKey(model.block.asItem()), new DelegatedModel(ModelLocationUtils.getModelLocation(model.block)));
        }
    }

    private final Block block;
    private final Material material;

    protected PillarMaterialBlockRenderer(Block block, Material material) {
        this.block = block;
        this.material = material;
    }
}
