package com.freezedown.metallurgica.compat.jei.category.drill;

import com.freezedown.metallurgica.registry.MetallurgicaBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import net.minecraft.world.level.block.state.BlockState;

public class AnimatedDrill extends AnimatedKinetics {
    private final BlockState deposit;
    private final int expansionAmount;
    
    public AnimatedDrill(BlockState deposit, int expansionAmount) {
        this.deposit = deposit;
        this.expansionAmount = expansionAmount;
    }
    
    @Override
    public void draw(PoseStack matrixStack, int xOffset, int yOffset) {
        matrixStack.pushPose();
        matrixStack.translate((double)xOffset, (double)yOffset, 200.0);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(-15.5F));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(22.5F));
        int scale = 23;
        
        for (int i = 0; i < expansionAmount; i++) {
            blockElement(MetallurgicaBlocks.drillExpansion.getDefaultState())
                    .scale(scale)
                    .render(matrixStack);
        }
        
        blockElement(deposit)
                .atLocal(0, 3, 0)
                .scale(scale)
                .render(matrixStack);
        
        blockElement(MetallurgicaBlocks.drillTower.getDefaultState())
                .atLocal(0, 2, 0)
                .scale(scale)
                .render(matrixStack);
        
        blockElement(cogwheel())
                .atLocal(0, 1, 0)
                .rotateBlock(0, getCurrentAngle() * 2, 0)
                .scale(scale)
                .render(matrixStack);
        
        blockElement(MetallurgicaBlocks.drillActivator.getDefaultState())
                .atLocal(0, 1, 0)
                .scale(scale)
                .render(matrixStack);
    }
}
