package dev.metallurgists.metallurgica.content.metalworking.forging.block;

import dev.metallurgists.metallurgica.registry.MetallurgicaPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class WorkedMetalRenderer extends SmartBlockEntityRenderer<WorkedMetalBlockEntity> {
    public WorkedMetalRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(WorkedMetalBlockEntity blockEntity, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(blockEntity, partialTicks, ms, buffer, light, overlay);
        BlockState blockState = blockEntity.getBlockState();
        List<Vec3> positions = blockEntity.getVoxelPositions();
        for (Vec3 position : positions) {
            ms.pushPose();
            double xOff = position.x;
            double yOff = position.y;
            double zOff = position.z;
            ms.translate(xOff, yOff, zOff);
            CachedBuffers.partial(MetallurgicaPartialModels.workedMetal, blockState, () -> ms);
        }
    }
}
