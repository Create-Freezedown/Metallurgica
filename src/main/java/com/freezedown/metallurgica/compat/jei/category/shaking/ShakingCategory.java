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
            builder.addSlot(RecipeIngredientRole.INPUT, 15, 29).setBackground(getRenderedSlot(), -1, -1).addIngredients(ForgeTypes.FLUID_STACK, withImprovedVisibility(recipe.getFluidIngredients().get(0).getMatchingFluidStacks()));
        }
        
        for(Iterator<ProcessingOutput> var7 = results.iterator(); var7.hasNext(); ++i) {
            ProcessingOutput output = var7.next();
            int xOffset = i % 2 == 0 ? 0 : 19;
            int yOffset = i / 2 * -19;
            builder.addSlot(RecipeIngredientRole.OUTPUT, single ? 139 : 133 + xOffset, 27 + yOffset).setBackground(getRenderedSlot(output), -1, -1).addItemStack(output.getStack()).addTooltipCallback(addStochasticTooltip(output));
        }
    }
    
    public void draw(ShakingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, PoseStack matrixStack, double mouseX, double mouseY) {
        AllGuiTextures.JEI_ARROW.render(matrixStack, 85, 32);
        AllGuiTextures.JEI_DOWN_ARROW.render(matrixStack, 43, 4);
        AllGuiTextures.JEI_SHADOW.render(matrixStack, 31, 48);
        this.table.draw(matrixStack, 48, 35);
    }
}
