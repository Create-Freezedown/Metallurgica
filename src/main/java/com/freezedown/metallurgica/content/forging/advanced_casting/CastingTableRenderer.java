package com.freezedown.metallurgica.content.forging.advanced_casting;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CastingTableRenderer extends SmartBlockEntityRenderer<CastingTableBlockEntity> {
    public CastingTableRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    
    @Override
    protected void renderSafe(CastingTableBlockEntity castingTable, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        SmartFluidTankBehaviour tank = castingTable.tank1;
        if (tank == null)
            return;
        SmartFluidTankBehaviour.TankSegment primaryTank = tank.getPrimaryTank();
        FluidStack fluidStack = primaryTank.getRenderedFluid();
        ItemStack mold = castingTable.moldInventory.getStackInSlot(0);
        ItemStack output = castingTable.outputInventory.getStackInSlot(0);
        
        // if the recipe is in progress, start fading the item away
        int timer = castingTable.getTimer();
        int totalTime = castingTable.getCoolingTime();
        int itemOpacity = 0;
        int fluidOpacity = 0xFF;
        if (timer > 0 && totalTime > 0) {
            int opacity = (4 * 0xFF) * timer / totalTime;
            // fade item in
            itemOpacity = opacity / 4;
            
            // fade fluid and temperature out during last 10%
            if (opacity > 3 * 0xFF) {
                fluidOpacity = (4 * 0xFF) - opacity;
            }
        }
        if (!mold.isEmpty()) {
            ms.scale(0.5F, 0.5F, 0.5F);
            Minecraft.getInstance().getItemRenderer().renderStatic(mold, ItemTransforms.TransformType.FIXED, light, overlay, ms, buffer, 0);
        }
        if (!output.isEmpty() && itemOpacity > 0) {
            ms.scale(0.5F, 0.5F, 0.5F);
            Minecraft.getInstance().getItemRenderer().renderStatic(output, ItemTransforms.TransformType.FIXED, light, itemOpacity, ms, buffer, 0);
        }
        
        
        
        float level = primaryTank.getFluidLevel()
                .getValue(partialTicks);
        if (!fluidStack.isEmpty() && level != 0) {
            level = Math.max(level, 0.175f);
            float minX = 2f / 16f;
            float maxX = 14f / 16f;
            float minY = 12f / 16f;
            float maxY = minY + (15 / 16f) * level;
            float minZ = 2f / 16f;
            float maxZ = 14f / 16f;
            
            ms.pushPose();
            FluidRenderer.renderFluidBox(fluidStack, minX, minY, minZ, maxX, maxY, maxZ, buffer, ms, light, false);
            ms.popPose();
            
        }
    }
}
