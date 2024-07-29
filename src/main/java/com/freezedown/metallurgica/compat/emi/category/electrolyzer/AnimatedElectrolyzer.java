package com.freezedown.metallurgica.compat.emi.category.electrolyzer;

import com.freezedown.metallurgica.registry.MetallurgicaBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import net.minecraft.core.Direction;

public class AnimatedElectrolyzer extends AnimatedKinetics {
    
    @Override
    public void draw(PoseStack matrixStack, int xOffset, int yOffset) {
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 200);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(22.5f));
        int scale = 23;
        
        blockElement(shaft(Direction.Axis.Z))
                .rotateBlock(0, 0, getCurrentAngle())
                .scale(scale)
                .render(matrixStack);
        
        blockElement(MetallurgicaBlocks.electrolyzer.getDefaultState())
                .atLocal(0, 0, 0)
                .scale(scale)
                .render(matrixStack);
        
        blockElement(AllBlocks.BASIN.getDefaultState())
                .atLocal(0, 1.65, 0)
                .scale(scale)
                .render(matrixStack);
        
        matrixStack.popPose();
        
    }
}
