package com.freezedown.metallurgica.foundation.mixin.jei;

import com.drmangotea.createindustry.recipes.distillation.DistillationRecipe;
import com.drmangotea.createindustry.recipes.jei.AdvancedDistillationCategory;
import com.freezedown.metallurgica.foundation.mixin.plugin.RequiresClass;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(AdvancedDistillationCategory.class)
public class DistillationCategoryMixin extends CreateRecipeCategory<DistillationRecipe> {
    public DistillationCategoryMixin(Info<DistillationRecipe> info) {
        super(info);
    }
    
    /**
     * @author PouffyDev
     * @reason Fix Fluid Amounts
     */
    @Overwrite(remap = false)
    public void setRecipe(IRecipeLayoutBuilder builder, DistillationRecipe recipe, IFocusGroup focuses) {
        FluidIngredient fluidIngredient = recipe.getInputFluid();
        int outputCount = recipe.getOutputCount(recipe);
        int yModifier = 60 - outputCount * 10;
        int y = 147 - yModifier;
        ((IRecipeSlotBuilder)builder.addSlot(RecipeIngredientRole.INPUT, 18, 130 - yModifier).setBackground(getRenderedSlot(), -1, -1).addIngredients(ForgeTypes.FLUID_STACK, withImprovedVisibility(recipe.getInputFluid().getMatchingFluidStacks()))).addTooltipCallback(addFluidTooltip(recipe.getInputFluid().getRequiredAmount()));
        
        for(int i = 0; i < outputCount; ++i) {
            y -= 24;
            ((IRecipeSlotBuilder)builder.addSlot(RecipeIngredientRole.OUTPUT, 105, y).setBackground(getRenderedSlot(), -1, -1).addIngredient(ForgeTypes.FLUID_STACK, withImprovedVisibility(recipe.getFluidResults().get(i)))).addTooltipCallback(addFluidTooltip(recipe.getFluidResults().get(i).getAmount()));
        }
        
    }
}
