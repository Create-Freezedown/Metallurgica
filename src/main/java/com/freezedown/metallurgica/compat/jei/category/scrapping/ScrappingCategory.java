package com.freezedown.metallurgica.compat.jei.category.scrapping;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.primitive.ceramic.ceramic_mixing_pot.CeramicMixingRecipe;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.compat.jei.category.CrushingCategory;
import com.simibubi.create.compat.jei.category.animations.AnimatedCrushingWheels;
import com.simibubi.create.content.kinetics.crusher.AbstractCrushingRecipe;
import com.simibubi.create.content.kinetics.crusher.CrushingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.registration.IRecipeRegistration;
import net.createmod.catnip.layout.LayoutHelper;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ScrappingCategory extends CreateRecipeCategory<CrushingRecipe> {
    private final AnimatedCrushingWheels crushingWheels = new AnimatedCrushingWheels();

    private final Supplier<List<CrushingRecipe>> recipes;

    public ScrappingCategory(Info<CrushingRecipe> info) {
        super(info);
        recipes = info.recipes();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CrushingRecipe recipe, IFocusGroup focuses) {
        builder
                .addSlot(RecipeIngredientRole.INPUT, 51, 3)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredients(recipe.getIngredients().get(0));

        int xOffset = getBackground().getWidth() / 2;
        int yOffset = 86;

        layoutOutput(recipe).forEach(layoutEntry -> builder
                .addSlot(RecipeIngredientRole.OUTPUT, (xOffset) + layoutEntry.posX() + 1, yOffset + layoutEntry.posY() + 1)
                .setBackground(getRenderedSlot(layoutEntry.output()), -1, -1)
                .addItemStack(layoutEntry.output().getStack())
                .addRichTooltipCallback(addStochasticTooltip(layoutEntry.output()))
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<CrushingRecipe> filteredRecipes = recipes.get().stream().filter(cr -> cr.getId().getNamespace().equals(Metallurgica.ID)).filter(cr -> cr.getId().getPath().startsWith("crushing/scrapping/")).toList();
        registration.addRecipes(type, filteredRecipes);
    }

    private List<LayoutEntry> layoutOutput(ProcessingRecipe<?> recipe) {
        int size = recipe.getRollableResults().size();
        List<LayoutEntry> positions = new ArrayList<>(size);

        LayoutHelper layout = LayoutHelper.centeredHorizontal(size, 1, 18, 18, 1);
        for (ProcessingOutput result : recipe.getRollableResults()) {
            positions.add(new LayoutEntry(result, layout.getX(), layout.getY()));
            layout.next();
        }

        return positions;
    }

    private record LayoutEntry(
            ProcessingOutput output,
            int posX,
            int posY
    ) {}

    @Override
    public void draw(CrushingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 72, 7);

        crushingWheels.draw(graphics, 62, 59);
    }
}
