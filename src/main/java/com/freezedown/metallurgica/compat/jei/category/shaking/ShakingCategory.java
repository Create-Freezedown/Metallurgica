package com.freezedown.metallurgica.compat.jei.category.shaking;

import com.freezedown.metallurgica.content.machines.shaking_table.ShakingRecipe;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Iterator;
import java.util.List;

public class ShakingCategory extends CreateRecipeCategory<ShakingRecipe> {
    private final AnimatedShakingTable table = new AnimatedShakingTable();
    
    public ShakingCategory(Info<ShakingRecipe> info) {
        super(info);
    }
    
    public void setRecipe(IRecipeLayoutBuilder builder, ShakingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 15, 9).setBackground(getRenderedSlot(), -1, -1).addIngredients((Ingredient)recipe.getIngredients().get(0));
        List<ProcessingOutput> results = recipe.getRollableResults();
        boolean single = results.size() == 1;
        int i = 0;
        
        if (!recipe.getFluidIngredients().isEmpty()) {
            addFluidSlot(builder, 15, 29, recipe.getFluidIngredients().get(0)).setBackground(getRenderedSlot(), -1, -1);
        }
        
        for(Iterator<ProcessingOutput> var7 = results.iterator(); var7.hasNext(); ++i) {
            ProcessingOutput output = var7.next();
            int xOffset = i % 2 == 0 ? 0 : 19;
            int yOffset = i / 2 * -19;
            builder.addSlot(RecipeIngredientRole.OUTPUT, single ? 139 : 133 + xOffset, 27 + yOffset).setBackground(getRenderedSlot(output), -1, -1).addItemStack(output.getStack()).addRichTooltipCallback(addStochasticTooltip(output));
        }
    }
    
    public void draw(ShakingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        PoseStack matrixStack = graphics.pose();
        AllGuiTextures.JEI_ARROW.render(graphics, 85, 32);
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 43, 4);
        AllGuiTextures.JEI_SHADOW.render(graphics, 31, 48);
        this.table.draw(graphics, 48, 35);
    }
}
