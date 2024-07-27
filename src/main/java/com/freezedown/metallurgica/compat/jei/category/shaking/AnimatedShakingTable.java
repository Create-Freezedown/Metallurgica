package com.freezedown.metallurgica.compat.jei.category.shaking;

import com.freezedown.metallurgica.registry.MetallurgicaBlocks;
import com.freezedown.metallurgica.registry.MetallurgicaPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.negodya1.vintageimprovements.content.kinetics.grinder.GrinderBlock;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

public class AnimatedShakingTable extends AnimatedKinetics {
    
    public AnimatedShakingTable() {
        super();
    }
    
    public void draw(PoseStack matrixStack, int xOffset, int yOffset) {
        matrixStack.pushPose();
        matrixStack.translate((double)xOffset, (double)yOffset, 0.0);
        matrixStack.translate(0.0, 0.0, 200.0);
        matrixStack.translate(2.0, 22.0, 0.0);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(-15.5F));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(112.5F));
        int scale = 25;
        this.blockElement(this.shaft(Direction.Axis.Z)).rotateBlock(0.0, 0.0, -getCurrentAngle()).scale(scale).render(matrixStack);
        this.blockElement(MetallurgicaBlocks.shakingTable.getDefaultState().setValue(GrinderBlock.HORIZONTAL_FACING, Direction.WEST)).rotateBlock(0.0, 0.0, 0.0).scale(scale).render(matrixStack);
        float animation = Mth.abs(Mth.sin(AnimationTickHolder.getRenderTime())) * 0.5F / 16.0F;
        this.blockElement(MetallurgicaPartialModels.shakerPlatform).atLocal(-animation, 0.0, 0.0).rotateBlock(0.0, 90.0, 0.0).scale(scale).render(matrixStack);
        matrixStack.popPose();
    }
}
