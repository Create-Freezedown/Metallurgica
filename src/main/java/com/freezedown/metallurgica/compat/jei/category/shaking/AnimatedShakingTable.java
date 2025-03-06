package com.freezedown.metallurgica.compat.jei.category.shaking;

import com.freezedown.metallurgica.content.machines.shaking_table.ShakingTableBlock;
import com.freezedown.metallurgica.registry.MetallurgicaBlocks;
import com.freezedown.metallurgica.registry.MetallurgicaPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

public class AnimatedShakingTable extends AnimatedKinetics {
    
    public AnimatedShakingTable() {
        super();
    }
    
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate((double)xOffset, (double)yOffset, 0.0);
        matrixStack.translate(0.0, 0.0, 200.0);
        matrixStack.translate(2.0, 22.0, 0.0);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5F));
        matrixStack.mulPose(Axis.YP.rotationDegrees(112.5F));
        int scale = 25;
        this.blockElement(this.shaft(Direction.Axis.Z)).rotateBlock(0.0, 0.0, -getCurrentAngle()).scale(scale).render(graphics);
        this.blockElement(MetallurgicaBlocks.shakingTable.getDefaultState().setValue(ShakingTableBlock.HORIZONTAL_FACING, Direction.WEST)).rotateBlock(0.0, 0.0, 0.0).scale(scale).render(graphics);
        float animation = Mth.abs(Mth.sin(AnimationTickHolder.getRenderTime())) * 0.5F / 16.0F;
        this.blockElement(MetallurgicaPartialModels.shakerPlatform).atLocal(-animation, 0.0, 0.0).rotateBlock(0.0, 90.0, 0.0).scale(scale).render(graphics);
        matrixStack.popPose();
    }
}
