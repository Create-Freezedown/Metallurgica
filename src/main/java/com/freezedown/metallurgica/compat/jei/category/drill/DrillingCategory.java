package com.freezedown.metallurgica.compat.jei.category.drill;

import com.drmangotea.createindustry.recipes.jei.DistillationCategory;
import com.freezedown.metallurgica.content.mineral.deposit.DepositManager;
import com.freezedown.metallurgica.registry.MetallurgicaBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.compat.jei.ConversionRecipe;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.compat.jei.category.ItemApplicationCategory;
import com.simibubi.create.compat.jei.category.MixingCategory;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.util.Mth;
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
        builder.addSlot(RecipeIngredientRole.INPUT, 2, 75)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 105, 100).setBackground(getRenderedSlot(), -1, -1).addItemStack(recipe.getRollableResults().get(0).getStack());
    }
    
    public void draw(DrillingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, PoseStack matrixStack, double mouseX, double mouseY) {
        AllGuiTextures.JEI_SHADOW.render(matrixStack, 62, 47);
        AllGuiTextures.JEI_DOWN_ARROW.render(matrixStack, 100, 79);
        
        int expansionHeight = (int) Math.round((recipe.getMinEfficiency() * 5 - 5.5) / 4.5);
        
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
        animatedDrill.draw(matrixStack, 65, 27);
    }
}
