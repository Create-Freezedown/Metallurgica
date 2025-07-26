package dev.metallurgists.metallurgica.compat.jei.category.drill;

import dev.metallurgists.metallurgica.registry.MetallurgicaBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.level.block.state.BlockState;

public class AnimatedDrill extends AnimatedKinetics {
    private final BlockState deposit;
    private final int expansionAmount;
    
    public AnimatedDrill(BlockState deposit, int expansionAmount) {
        this.deposit = deposit;
        this.expansionAmount = expansionAmount;
    }
    
    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate((double)xOffset, (double)yOffset, 200.0);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5F));
        matrixStack.mulPose(Axis.YP.rotationDegrees(22.5F));
        int scale = 23;
        
        for (int i = 0; i < expansionAmount; i++) {
            blockElement(MetallurgicaBlocks.drillExpansion.getDefaultState())
                    .atLocal(0, -2 + i, 0)
                    .scale(scale)
                    .render(graphics);
        }
        
        blockElement(deposit)
                .atLocal(0, 0, 0)
                .scale(scale)
                .render(graphics);
        
        blockElement(MetallurgicaBlocks.drillTower.getDefaultState())
                .atLocal(0, -1, 0)
                .scale(scale)
                .render(graphics);
        
        blockElement(cogwheel())
                .atLocal(0, -2, 0)
                .rotateBlock(0, getCurrentAngle() * 2, 0)
                .scale(scale)
                .render(graphics);
        
        blockElement(MetallurgicaBlocks.drillActivator.getDefaultState())
                .atLocal(0, -2, 0)
                .scale(scale)
                .render(graphics);
        
        matrixStack.scale((float)scale, (float)(-scale), (float)scale);
        matrixStack.translate(0.0, -1.8, 0.0);
        matrixStack.popPose();
    }
}
