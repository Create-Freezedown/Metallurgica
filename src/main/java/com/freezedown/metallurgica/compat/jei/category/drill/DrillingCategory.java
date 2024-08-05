package com.freezedown.metallurgica.compat.jei.category.drill;

import com.freezedown.metallurgica.content.mineral.deposit.DepositManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DrillingCategory extends CreateRecipeCategory<DrillingRecipe> {
    public static final List<DrillingRecipe> RECIPES = new ArrayList<>();
    
    static {
        DepositManager.depositType.forEach((blockState, deposit) -> {
            RECIPES.add(DrillingRecipe.create(new ItemStack(blockState.getBlock()), deposit.mineralItem(), deposit.minimumEfficiency()));
        });
    }
    
    public DrillingCategory(Info<DrillingRecipe> info) {
        super(info);
    }
    
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, DrillingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 4, 112)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredients(recipe.getIngredients().get(0))
                .addTooltipCallback((recipeSlotView, tooltip) -> {
                    tooltip.add(1, Component.translatable("metallurgica.jei.required_efficiency", recipe.getMinEfficiency()).withStyle(Style.EMPTY.withColor(0x8c4a2d)));
                });
        builder.addSlot(RecipeIngredientRole.OUTPUT, 160, 117).setBackground(getRenderedSlot(), -1, -1).addItemStack(recipe.getRollableResults().get(0).getStack());
    }
    
    public void draw(DrillingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, PoseStack matrixStack, double mouseX, double mouseY) {
        AllGuiTextures.JEI_ARROW.render(matrixStack, 15, 121);
        AllGuiTextures.JEI_DOWN_ARROW.render(matrixStack, 45, 15);
        
        int expansionHeight = (int) ((5 * recipe.getMinEfficiency()) / 1.1) - 1;
        
        Optional<ItemStack> displayedIngredient = iRecipeSlotsView.getSlotViews().get(0)
                .getDisplayedIngredient(VanillaTypes.ITEM_STACK);
        if (displayedIngredient.isEmpty())
            return;
        
        Item item = displayedIngredient.get()
                .getItem();
        if (!(item instanceof BlockItem blockItem))
            return;
        
        BlockState state = blockItem.getBlock().defaultBlockState();
        
        AnimatedDrill animatedDrill = new AnimatedDrill(state, expansionHeight);
        animatedDrill.draw(matrixStack, 60, 135);
    }
}
