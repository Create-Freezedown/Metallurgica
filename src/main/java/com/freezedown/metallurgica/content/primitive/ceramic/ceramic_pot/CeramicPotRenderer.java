package com.freezedown.metallurgica.content.primitive.ceramic.ceramic_pot;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraftforge.fluids.FluidStack;

public class CeramicPotRenderer extends SafeBlockEntityRenderer<CeramicPotBlockEntity> {
    
    
    public CeramicPotRenderer(BlockEntityRendererProvider.Context context) {
    
    }
    
    @Override
    protected void renderSafe(CeramicPotBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        float fluidLevel = renderFluids(be, partialTicks, ms, buffer, light, overlay);
        float level = Mth.clamp(fluidLevel - .3f, .125f, .6f);
    }
    
    protected float renderFluids(CeramicPotBlockEntity pot, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        float totalUnits = pot.tank.getPrimaryTank().getTotalUnits(partialTicks);
        if (totalUnits < 1)
            return 0;
        
        float fluidLevel = Mth.clamp(totalUnits / 2000, 0, 1);
        
        fluidLevel = 1 - ((1 - fluidLevel) * (1 - fluidLevel));
        
        float xMin = 4 / 16f;
        float xMax = 4 / 16f;
        final float yMin = 2 / 16f;
        final float yMax = yMin + 11 / 16f * fluidLevel;
        float zMin = 4 / 16f;
        float zMax = 12 / 16f;
        
        FluidStack renderedFluid = pot.getTankInventory().getFluid();
        if (renderedFluid.isEmpty())
            return 0;
        float units = pot.tank.getPrimaryTank().getTotalUnits(partialTicks);
        if (units < 1)
            return 0;
        
        float partial = Mth.clamp(units / totalUnits, 0, 1);
        xMax += partial * 8 / 16f;
        FluidRenderer.renderFluidBox(renderedFluid, xMin, yMin, zMin, xMax, yMax, zMax, buffer, ms, light,
                false);
        xMin = xMax;
        
        return yMax;
    }
    
    @Override
    public int getViewDistance() {
        return 16;
    }
    
    
}
