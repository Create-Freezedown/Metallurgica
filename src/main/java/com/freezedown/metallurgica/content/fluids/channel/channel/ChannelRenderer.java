package com.freezedown.metallurgica.content.fluids.channel.channel;

import com.freezedown.metallurgica.registry.MetallurgicaPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import dev.engine_room.flywheel.lib.transform.PoseTransformStack;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class ChannelRenderer extends SafeBlockEntityRenderer<ChannelBlockEntity> {
    public ChannelRenderer(BlockEntityRendererProvider.Context context) {
    }
    @Override
    protected void renderSafe(ChannelBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        this.renderFluid(be, partialTicks, ms, buffer, light, overlay);
        BlockState blockState = be.getBlockState();
        VertexConsumer vb = buffer.getBuffer(RenderType.solid());
        ms.pushPose();
        TransformStack<PoseTransformStack> msr = TransformStack.of(ms);
        msr.translate(0.5, 0.5, 0.5);
        if (be.north) {
            CachedBuffers.partial(MetallurgicaPartialModels.channelSide, blockState).uncenter().light(light).renderInto(ms, vb);
        }
        if (be.east) {
            CachedBuffers.partial(MetallurgicaPartialModels.channelSide, blockState).rotateY(270.0f).uncenter().light(light).renderInto(ms, vb);
        }
        if (be.south) {
            CachedBuffers.partial(MetallurgicaPartialModels.channelSide, blockState).rotateY(180.0f).uncenter().light(light).renderInto(ms, vb);
        }
        if (be.west) {
            CachedBuffers.partial(MetallurgicaPartialModels.channelSide, blockState).rotateY(90.0f).uncenter().light(light).renderInto(ms, vb);
        }
        
        ms.popPose();
    }
    
    protected void renderFluid(ChannelBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        FluidTank tank = be.tankInventory;
        float fluidLevel = Mth.clamp(be.getFluidLevel().getValue(partialTicks) / 1000.0F, 0, 1);
        FluidStack fluidStack = tank.getFluid();
        
        
        fluidLevel = 1 - ((1 - fluidLevel) * (1 - fluidLevel));
        float xMin = 2 / 16f;
        float xMax = 2 / 16f;
        final float yMin = 2 / 16f;
        final float yMax = yMin + 7 / 16f * fluidLevel;
        final float zMin = 2 / 16f;
        final float zMax = 14 / 16f;
        //North
        final float xMinN = 2 / 16f;
        final float xMaxN = 14 / 16f;
        final float zMinN = 0 / 16f;
        final float zMaxN = 2 / 16f;
        final float yMaxN = be.north ? yMax : yMin;
        //East
        final float xMinE = 14 / 16f;
        final float xMaxE = 16 / 16f;
        final float zMinE = 2 / 16f;
        final float zMaxE = 14 / 16f;
        final float yMaxE = be.east ? yMax : yMin;
        //South
        final float xMinS = 2 / 16f;
        final float xMaxS = 14 / 16f;
        final float zMinS = 14 / 16f;
        final float zMaxS = 16 / 16f;
        final float yMaxS = be.south ? yMax : yMin;
        //West
        final float xMinW = 0 / 16f;
        final float xMaxW = 2 / 16f;
        final float zMinW = 2 / 16f;
        final float zMaxW = 14 / 16f;
        final float yMaxW = be.west ? yMax : yMin;
        
        
        
        if (!fluidStack.isEmpty()) {
            ms.pushPose();
            FluidRenderer.renderFluidBox(fluidStack.getFluid(), fluidStack.getAmount(), xMin, yMin, zMin, xMax, yMax, zMax, buffer, ms, light, false, false);
            FluidRenderer.renderFluidBox(fluidStack.getFluid(), fluidStack.getAmount(), xMinN, yMin, zMinN, xMaxN, yMaxN, zMaxN, buffer, ms, light, false, false);
            FluidRenderer.renderFluidBox(fluidStack.getFluid(), fluidStack.getAmount(), xMinE, yMin, zMinE, xMaxE, yMaxE, zMaxE, buffer, ms, light, false, false);
            FluidRenderer.renderFluidBox(fluidStack.getFluid(), fluidStack.getAmount(), xMinS, yMin, zMinS, xMaxS, yMaxS, zMaxS, buffer, ms, light, false, false);
            FluidRenderer.renderFluidBox(fluidStack.getFluid(), fluidStack.getAmount(), xMinW, yMin, zMinW, xMaxW, yMaxW, zMaxW, buffer, ms, light, false, false);
            
            ms.popPose();
        }
    }
}
