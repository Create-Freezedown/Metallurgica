package com.freezedown.metallurgica.compat.jei.category.electrolyzer;

import com.freezedown.metallurgica.registry.MetallurgicaBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;

public class AnimatedElectrolyzer extends AnimatedKinetics {
    
    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 200);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Axis.YP.rotationDegrees(22.5f));
        int scale = 23;
        
        blockElement(shaft(Direction.Axis.Z))
                .rotateBlock(0, 0, getCurrentAngle())
                .scale(scale)
                .render(graphics);
        
        blockElement(MetallurgicaBlocks.electrolyzer.getDefaultState())
                .atLocal(0, 0, 0)
                .scale(scale)
                .render(graphics);
        
        blockElement(AllBlocks.BASIN.getDefaultState())
                .atLocal(0, 1.65, 0)
                .scale(scale)
                .render(graphics);
        
        matrixStack.popPose();
        
    }
}
