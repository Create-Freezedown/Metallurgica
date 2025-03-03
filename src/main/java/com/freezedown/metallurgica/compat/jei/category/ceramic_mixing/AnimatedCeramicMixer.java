package com.freezedown.metallurgica.compat.jei.category.ceramic_mixing;

import com.freezedown.metallurgica.registry.MetallurgicaBlocks;
import com.freezedown.metallurgica.registry.MetallurgicaPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

public class AnimatedCeramicMixer extends AnimatedKinetics {
    
    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 200);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Axis.YP.rotationDegrees(22.5f));
        int scale = 23;
        
        blockElement(MetallurgicaBlocks.ceramicMixingPot.getDefaultState())
                .atLocal(0, 1.65, 0)
                .scale(scale)
                .render(graphics);
        
        float animation = ((Mth.sin(AnimationTickHolder.getRenderTime() / 32f) + 0.3f) / 5) + .5f;
        
        blockElement(MetallurgicaPartialModels.ceramicMixerStirrer)
                .rotateBlock(0, getCurrentAngle() * 4, 0)
                .atLocal(0, animation + 1, 0)
                .scale(scale)
                .render(graphics);

        matrixStack.popPose();
    }
}
