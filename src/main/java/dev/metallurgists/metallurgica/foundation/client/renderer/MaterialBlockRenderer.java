package dev.metallurgists.metallurgica.foundation.client.renderer;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.foundation.material.block.AxisMaterialBlock;
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

public class MaterialBlockRenderer {

    private static final Set<MaterialBlockRenderer> MODELS = new HashSet<>();

    public static void create(Block block, Material material, FlagKey<?> flagKey) {
        if (block instanceof AxisMaterialBlock) {
            PillarMaterialBlockRenderer.create(block, material, flagKey);
            return;
        }
        MODELS.add(new MaterialBlockRenderer(block, material, flagKey));
    }

    public static void reinitModels() {
        for (MaterialBlockRenderer model : MODELS) {
            ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(model.block);
            String blockName = model.flagKey.toString();
            boolean texturePresent = Minecraft.getInstance().getResourceManager().getResource(Metallurgica.asResource("textures/block/materials/" + model.material.getName() + "/%s.png".formatted(blockName))).isPresent();
            String texture = texturePresent ? "metallurgica:block/materials/" + model.material.getName() + "/" + blockName : "metallurgica:block/materials/null/" + blockName;
            ResourceLocation modelId = blockId.withPrefix("block/");
            MetallurgicaDynamicResourcePack.addBlockModel(blockId, MetallurgicaModels.simpleCubeAll(texture));
            MetallurgicaDynamicResourcePack.addBlockState(blockId, BlockModelGenerators.createSimpleBlock(model.block, modelId));
            MetallurgicaDynamicResourcePack.addItemModel(BuiltInRegistries.ITEM.getKey(model.block.asItem()), new DelegatedModel(ModelLocationUtils.getModelLocation(model.block)));
        }
    }

    private final Block block;
    private final Material material;
    private final FlagKey<?> flagKey;

    protected MaterialBlockRenderer(Block block, Material material, FlagKey<?> flagKey) {
        this.block = block;
        this.material = material;
        this.flagKey = flagKey;
    }
}
