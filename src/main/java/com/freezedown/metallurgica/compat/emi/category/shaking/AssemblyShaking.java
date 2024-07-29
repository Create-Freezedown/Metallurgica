package com.freezedown.metallurgica.compat.emi.category.shaking;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.sequencedAssembly.SequencedAssemblySubCategory;
import com.simibubi.create.content.processing.sequenced.SequencedRecipe;

public class AssemblyShaking extends SequencedAssemblySubCategory {
    AnimatedShakingTable table = new AnimatedShakingTable();
    
    public AssemblyShaking() {
        super(25);
    }
    
    @Override
    public void draw(SequencedRecipe<?> recipe, PoseStack graphics, double mouseX, double mouseY, int index) {
        graphics.pushPose();
        graphics.translate(0.0, 51.5, 0.0);
        graphics.scale(0.6F, 0.6F, 0.6F);
        this.table.draw(graphics, this.getWidth() / 2, 30);
        graphics.popPose();
    }
}
