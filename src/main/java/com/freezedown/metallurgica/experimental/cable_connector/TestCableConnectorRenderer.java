package com.freezedown.metallurgica.experimental.cable_connector;

import com.freezedown.metallurgica.infastructure.conductor.CableConnection;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class TestCableConnectorRenderer extends SafeBlockEntityRenderer<TestCableConnectorBlockEntity> {
    public TestCableConnectorRenderer(BlockEntityRendererProvider.Context context) {
    }

    protected void renderSafe(TestCableConnectorBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        for(CableConnection cableConnection : be.cableConnections) {
            if (cableConnection.shouldRender) {
                cableConnection.renderWire(ms, bufferSource, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, cableConnection.length / 4500.0F);
            }
        }

    }
}
