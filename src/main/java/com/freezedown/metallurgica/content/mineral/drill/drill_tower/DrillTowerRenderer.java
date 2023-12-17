package com.freezedown.metallurgica.content.mineral.drill.drill_tower;

import com.freezedown.metallurgica.registry.MetallurgicaPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class DrillTowerRenderer extends SafeBlockEntityRenderer<DrillTowerBlockEntity> {
    
    public DrillTowerRenderer(BlockEntityRendererProvider.Context context) {}
    @Override
    protected void renderSafe(DrillTowerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        float speed = ( be.efficiency / 4) * be.efficiency;
        float time = AnimationTickHolder.getRenderTime(be.getLevel());
        float angle = ((time * speed * 6 / 10f) % 360) / 180 * (float) Math.PI;
        BlockState blockState = be.getBlockState();
        
        VertexConsumer vbCutout = bufferSource.getBuffer(RenderType.cutoutMipped());
        SuperByteBuffer drillBit = CachedBufferer.partial(MetallurgicaPartialModels.drillBitCastIron, blockState);
        
        drillBit.rotateCentered(Direction.UP, angle)
                .light(light)
                .renderInto(ms, vbCutout);
    }
}
