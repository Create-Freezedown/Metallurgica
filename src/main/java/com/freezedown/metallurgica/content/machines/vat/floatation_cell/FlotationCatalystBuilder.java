package com.freezedown.metallurgica.content.machines.vat.floatation_cell;

import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class FlotationCatalystBuilder extends ProcessingRecipeBuilder<FlotationCatalyst> {
    protected FlotationCatalystParams catalystParams;

    public FlotationCatalystBuilder(ProcessingRecipeBuilder.ProcessingRecipeFactory factory, FlotationCatalystParams params, ResourceLocation recipeId) {
        super(factory, recipeId);
        this.catalystParams = params;
    }

    public FlotationCatalyst build() {
        FlotationCatalyst recipe = this.factory.create(this.params);
        recipe.efficiencyMultiplier = this.catalystParams.efficiencyMultiplier;
        return recipe;
    }

    public void build(Consumer<FinishedRecipe> consumer) {
        consumer.accept(new ProcessingRecipeBuilder.DataGenResult<>(this.build(), this.recipeConditions));
    }

    public static class FlotationCatalystParams {
        public float efficiencyMultiplier = 1.0f;

        public FlotationCatalystParams() {

        }
    }
}
