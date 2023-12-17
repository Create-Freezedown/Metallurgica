package com.freezedown.metallurgica.content.fluids.channel.channel_depot;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public class ChannelDepotRenderer extends SmartBlockEntityRenderer<ChannelDepotBlockEntity> {
    public ChannelDepotRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    @Override
    protected void renderSafe(ChannelDepotBlockEntity channelDepot, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        super.renderSafe(channelDepot, partialTicks, ms, buffer, light, overlay);
        
        float fluidLevel = renderFluids(channelDepot, partialTicks, ms, buffer, light, overlay);
        float level = Mth.clamp(fluidLevel - .3f, .125f, .6f);
        
        ms.pushPose();
        
        BlockPos pos = channelDepot.getBlockPos();
        ms.translate(.5, .2f, .5);
        
        RandomSource r = RandomSource.create(pos.hashCode());
        ms.popPose();
        
        BlockState blockState = channelDepot.getBlockState();
        if (!(blockState.getBlock() instanceof ChannelDepotBlock))
            return;
        Direction direction = blockState.getValue(ChannelDepotBlock.FACING);
        if (direction == Direction.DOWN)
            return;
    }
    
    protected float renderFluids(ChannelDepotBlockEntity channelDepot, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                                 int light, int overlay) {
        SmartFluidTankBehaviour inputFluids = channelDepot.getBehaviour(SmartFluidTankBehaviour.INPUT);
        SmartFluidTankBehaviour outputFluids = channelDepot.getBehaviour(SmartFluidTankBehaviour.OUTPUT);
        SmartFluidTankBehaviour[] tanks = { inputFluids, outputFluids };
        float totalUnits = channelDepot.getTotalFluidUnits(partialTicks);
        if (totalUnits < 1)
            return 0;
        
        float fluidLevel = Mth.clamp(totalUnits / 2000, 0, 1);
        
        fluidLevel = 1 - ((1 - fluidLevel) * (1 - fluidLevel));
        
        float xMin = 2 / 16f;
        float xMax = 14 / 16f;
        final float yMin = 2 / 16f;
        final float yMax = yMin + 7 / 16f * fluidLevel;
        final float zMin = 2 / 16f;
        final float zMax = 14 / 16f;
        
        for (SmartFluidTankBehaviour behaviour : tanks) {
            if (behaviour == null)
                continue;
            for (SmartFluidTankBehaviour.TankSegment tankSegment : behaviour.getTanks()) {
                FluidStack renderedFluid = tankSegment.getRenderedFluid();
                if (renderedFluid.isEmpty())
                    continue;
                float units = tankSegment.getTotalUnits(partialTicks);
                if (units < 1)
                    continue;
                
                float partial = Mth.clamp(units / totalUnits, 0, 1);
                xMax += partial * 7 / 16f;
                FluidRenderer.renderFluidBox(renderedFluid, xMin, yMin, zMin, xMax, yMax, zMax, buffer, ms, light, false);
                
                xMin = xMax;
            }
        }
        
        return yMax;
    }
    
    @Override
    public int getViewDistance() {
        return 16;
    }
}
