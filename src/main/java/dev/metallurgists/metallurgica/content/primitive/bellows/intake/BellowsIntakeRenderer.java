package dev.metallurgists.metallurgica.content.primitive.bellows.intake;

import dev.metallurgists.metallurgica.foundation.block_entity.behaviour.DisplayStateBehaviour;
import dev.metallurgists.metallurgica.foundation.block_entity.renderer.DisplayStateRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class BellowsIntakeRenderer extends DisplayStateRenderer<BellowsIntakeBlockEntity> {
    public BellowsIntakeRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(BellowsIntakeBlockEntity blockEntity, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        DisplayStateBehaviour displayStateBehaviour = blockEntity.getDisplayStateBehaviour();
        renderDisplayState(blockEntity, displayStateBehaviour, ms, buffer, overlay);
    }
}
