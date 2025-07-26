package dev.metallurgists.metallurgica.content.primitive.ceramic;

import dev.metallurgists.metallurgica.registry.MetallurgicaBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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

public class UnfiredCeramicBlockRenderer extends SmartBlockEntityRenderer<UnfiredCeramicBlockEntity> {
    public UnfiredCeramicBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(UnfiredCeramicBlockEntity blockEntity, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        boolean renderFuel = blockEntity.isHasFuel() && !blockEntity.isCooking();
        boolean renderLitFuel = blockEntity.isHasFuel() && blockEntity.isCooking();

        Minecraft mc = Minecraft.getInstance();
        BlockState fuelState = MetallurgicaBlocks.charcoalPile.getDefaultState();
        BlockState litFuelState = MetallurgicaBlocks.charredLogPile.getDefaultState();
        BakedModel fuelModel = mc.getBlockRenderer().getBlockModel(fuelState);
        BakedModel litFuelModel = mc.getBlockRenderer().getBlockModel(MetallurgicaBlocks.charredLogPile.getDefaultState());
        ModelBlockRenderer modelBlockRenderer = mc.getBlockRenderer().getModelRenderer();
        BlockPos pos = blockEntity.getBlockPos();
        Level level = blockEntity.getLevel();
        if (level == null) return;

        if (renderFuel) {
            for (RenderType renderType : fuelModel.getRenderTypes(fuelState, RandomSource.create(fuelState.getSeed(pos)), ModelData.EMPTY)) {
                VertexConsumer vertexConsumer = buffer.getBuffer(renderType);
                modelBlockRenderer.tesselateBlock(blockEntity.getLevel(), fuelModel, fuelState, pos, ms, vertexConsumer, true, RandomSource.create(), fuelState.getSeed(pos), overlay, ModelData.EMPTY, renderType);
            }
        }
        if (renderLitFuel) {
            for (RenderType renderType : litFuelModel.getRenderTypes(litFuelState, RandomSource.create(litFuelState.getSeed(pos)), ModelData.EMPTY)) {
                VertexConsumer vertexConsumer = buffer.getBuffer(renderType);
                modelBlockRenderer.tesselateBlock(blockEntity.getLevel(), litFuelModel, litFuelState, pos, ms, vertexConsumer, true, RandomSource.create(), litFuelState.getSeed(pos), overlay, ModelData.EMPTY, renderType);
            }
        }
    }
}
