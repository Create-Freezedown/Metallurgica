package dev.metallurgists.metallurgica.foundation.block_entity.renderer;

import dev.metallurgists.metallurgica.foundation.block_entity.behaviour.DisplayStateBehaviour;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

public class DisplayStateRenderer<T extends SmartBlockEntity> extends SmartBlockEntityRenderer<T> {
    public DisplayStateRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    public void renderDisplayState(T blockEntity, DisplayStateBehaviour displayStateBehaviour, PoseStack ms, MultiBufferSource buffer, int overlay) {
        Minecraft mc = Minecraft.getInstance();

        if (displayStateBehaviour == null || !displayStateBehaviour.hasDisplayState()) return;

        BlockState displayState = displayStateBehaviour.getDisplayState();
        BakedModel displayModel = mc.getBlockRenderer().getBlockModel(displayState);

        ModelBlockRenderer modelBlockRenderer = mc.getBlockRenderer().getModelRenderer();
        BlockPos pos = blockEntity.getBlockPos();
        Level level = blockEntity.getLevel();
        if (level == null) return;

        for (RenderType renderType : displayModel.getRenderTypes(displayState, RandomSource.create(displayState.getSeed(pos)), ModelData.EMPTY)) {
            VertexConsumer vertexConsumer = buffer.getBuffer(renderType);
            modelBlockRenderer.tesselateBlock(blockEntity.getLevel(), displayModel, displayState, pos, ms, vertexConsumer, true, RandomSource.create(), displayState.getSeed(pos), overlay, ModelData.EMPTY, renderType);
        }
    }
}
