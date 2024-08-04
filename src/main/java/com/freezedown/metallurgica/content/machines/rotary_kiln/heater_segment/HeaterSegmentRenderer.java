package com.freezedown.metallurgica.content.machines.rotary_kiln.heater_segment;

import com.drmangotea.createindustry.CreateTFMG;
import com.freezedown.metallurgica.content.machines.shaking_table.ShakingTableBlockEntity;
import com.jozufozu.flywheel.core.StitchedSprite;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.waterwheel.LargeWaterWheelBlock;
import com.simibubi.create.content.kinetics.waterwheel.WaterWheelModelKey;
import com.simibubi.create.foundation.model.BakedModelHelper;
import com.simibubi.create.foundation.render.BakedModelRenderHelper;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.render.SuperByteBufferCache;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

import java.util.List;
import java.util.Map;

public class HeaterSegmentRenderer extends KineticBlockEntityRenderer<HeaterSegmentBlockEntity> {
    public static final SuperByteBufferCache.Compartment<HeaterSegmentModelKey> HEATER_SEGMENT = new SuperByteBufferCache.Compartment<>();
    
    public static final StitchedSprite CONNECTED_CASING_TEMPLATE = new StitchedSprite(new ResourceLocation(CreateTFMG.MOD_ID, "block/heavy_machinery_casing_connected"));
    
    private static final String[] CASING_SUFFIXES = new String[] {"_casing"};
    
    public HeaterSegmentRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    
    @Override
    protected void renderSafe(HeaterSegmentBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
        renderFrame(be, partialTicks, ms, buffer, light);
    }
    
    protected void renderFrame(HeaterSegmentBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light) {
        BlockState state = be.getBlockState();
        SuperByteBuffer model = getFrameModel(be, state);
        model.light(light)
                .renderInto(ms, buffer.getBuffer(RenderType.solid()));
    }
    
    protected SuperByteBuffer getFrameModel(HeaterSegmentBlockEntity be, BlockState state) {
        HeaterSegmentModelKey key = new HeaterSegmentModelKey(state, be.material);
        return CreateClient.BUFFER_CACHE.get(HEATER_SEGMENT, key, () -> {
            BakedModel model = generateModel(key);
            BlockState state1 = key.state();
            Direction dir = state1.getValue(HeaterSegmentBlock.HORIZONTAL_FACING);
            PoseStack transform = CachedBufferer.rotateToFaceVertical(dir).get();
            return BakedModelRenderHelper.standardModelRender(model, Blocks.AIR.defaultBlockState(), transform);
        });
    }
    
    public static BakedModel generateModel(HeaterSegmentModelKey key) {
        BakedModel template = AllPartialModels.WATER_WHEEL.get();
        return generateModel(template, key.material());
    }
    
    public static BakedModel generateModel(BakedModel template, BlockState casingBlockState) {
        Block casingBlock = casingBlockState.getBlock();
        ResourceLocation id = RegisteredObjects.getKeyOrThrow(casingBlock);
        String path = id.getPath();
        
        if (path.endsWith("_casing")) {
            Map<TextureAtlasSprite, TextureAtlasSprite> map = new Reference2ReferenceOpenHashMap<>();
            map.put(CONNECTED_CASING_TEMPLATE.get(), getSpriteOnSide(casingBlockState, Direction.UP));
            return BakedModelHelper.generateModel(template, map::get);
        }
        
        return BakedModelHelper.generateModel(template, sprite -> null);
    }
    
    private static TextureAtlasSprite getSpriteOnSide(BlockState state, Direction side) {
        BakedModel model = Minecraft.getInstance()
                .getBlockRenderer()
                .getBlockModel(state);
        if (model == null)
            return null;
        RandomSource random = RandomSource.create();
        random.setSeed(42L);
        List<BakedQuad> quads = model.getQuads(state, side, random, ModelData.EMPTY, null);
        if (!quads.isEmpty()) {
            TextureAtlasSprite sprite = quads.get(0).getSprite();
            TextureAtlas atlas = sprite.atlas();
            return atlas.getSprite(new ResourceLocation(sprite.getName().getPath().replace("_casing", "_casing_connected")));
        }
        random.setSeed(42L);
        quads = model.getQuads(state, null, random, ModelData.EMPTY, null);
        if (!quads.isEmpty()) {
            for (BakedQuad quad : quads) {
                if (quad.getDirection() == side) {
                    TextureAtlasSprite sprite = quad.getSprite();
                    TextureAtlas atlas = sprite.atlas();
                    return atlas.getSprite(new ResourceLocation(sprite.getName().getPath().replace("_casing", "_casing_connected")));
                }
            }
        }
        return model.getParticleIcon(ModelData.EMPTY);
    }
}
