package com.freezedown.metallurgica.content.forging.casting.ingot;

import com.freezedown.metallurgica.content.forging.casting.AbstractCastingMoldBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.waterwheel.WaterWheelRenderer;
import com.simibubi.create.foundation.model.BakedModelHelper;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.createmod.catnip.platform.CatnipServices;
import net.createmod.catnip.render.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;
import java.util.Map;

public class IngotCastingMoldRenderer extends KineticBlockEntityRenderer<IngotCastingMoldBlockEntity> {
    
    protected static final RenderType[] REVERSED_CHUNK_BUFFER_LAYERS = RenderType.chunkBufferLayers().toArray(RenderType[]::new);
    static {
        ArrayUtils.reverse(REVERSED_CHUNK_BUFFER_LAYERS);
    }
    
    public IngotCastingMoldRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        
    }
    
    public static final SuperByteBufferCache.Compartment<IngotModelKey> INGOT = new SuperByteBufferCache.Compartment<>();
    public static final StitchedSprite COPPER_BLOCK_TEMPLATE = new StitchedSprite(new ResourceLocation("block/copper_block"));
    
    private static final String[] BLOCK_SUFFIXES = new String[]{"_block", "_storage_block"};
    private static final String[] BLOCK_PREFIXES = new String[]{"block_", "storage_block_", ""};
    
    protected SuperByteBuffer getRotatedModel(IngotCastingMoldBlockEntity be, BlockState state) {
        IngotModelKey key = new IngotModelKey(state, be.getOutput());
        return SuperByteBufferCache.getInstance().get(INGOT, key, () -> {
            BakedModel model = generateModel(key);
            BlockState state1 = key.state();
            Direction dir = state1.getValue(AbstractCastingMoldBlock.FACING);
            PoseStack transform = CachedBuffers.rotateToFaceVertical(dir).get();
            return SuperBufferFactory.getInstance().createForBlock(model, Blocks.AIR.defaultBlockState(), transform);
        });
    }
    
    public static BakedModel generateModel(IngotModelKey key) {
        BakedModel template = AllPartialModels.WATER_WHEEL.get();
        return generateModel(template, key.item());
    }
    
    public static BakedModel generateModel(BakedModel template, ItemStack itemStack) {
        Item ingot = itemStack.getItem();
        ResourceLocation id = CatnipServices.REGISTRIES.getKeyOrThrow(ingot);
        ResourceLocation blockId = id.getPath().startsWith("ingot_") ? new ResourceLocation(id.getNamespace(), id.getPath().replace("ingot_", "block_")) : id.getPath().endsWith("_ingot") ? new ResourceLocation(id.getNamespace(), id.getPath().replace("_ingot", "_block")) : new ResourceLocation("copper_block");
        String path = blockId.getPath();
        
        BlockState blockState = Blocks.COPPER_BLOCK.defaultBlockState();
        
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockId.getNamespace(), path));
        if (block != null)
            blockState = block.defaultBlockState();
        
        Map<TextureAtlasSprite, TextureAtlasSprite> map = new Reference2ReferenceOpenHashMap<>();
        map.put(COPPER_BLOCK_TEMPLATE.get(), getSpriteOnSide(blockState, Direction.UP));
        
        return BakedModelHelper.generateModel(template, map::get);
    }
    
    private static TextureAtlasSprite getSpriteOnSide(BlockState state, Direction side) {
        BakedModel model = Minecraft.getInstance()
                .getBlockRenderer()
                .getBlockModel(state);
        RandomSource random = RandomSource.create();
        random.setSeed(42L);
        List<BakedQuad> quads = model.getQuads(state, side, random, ModelData.EMPTY, null);
        if (!quads.isEmpty()) {
            return quads.get(0)
                    .getSprite();
        }
        random.setSeed(42L);
        quads = model.getQuads(state, null, random, ModelData.EMPTY, null);
        if (!quads.isEmpty()) {
            for (BakedQuad quad : quads) {
                if (quad.getDirection() == side) {
                    return quad.getSprite();
                }
            }
        }
        return model.getParticleIcon(ModelData.EMPTY);
    }
}
