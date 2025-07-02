package com.freezedown.metallurgica.content.machines.vat.floatation_cell;

import com.drmangotea.tfmg.datagen.recipes.builder.VatMachineRecipeBuilder;
import com.drmangotea.tfmg.recipes.VatMachineRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FloatationCatalystBuilder extends ProcessingRecipeBuilder<FloatationCatalyst> {
    protected FloatationCatalystParams vatParams;

    public FloatationCatalystBuilder(ProcessingRecipeBuilder.ProcessingRecipeFactory factory, FloatationCatalystParams params, ResourceLocation recipeId) {
        super(factory, recipeId);
        this.vatParams = params;
    }

    public FloatationCatalyst build() {
        FloatationCatalyst recipe = this.factory.create(this.params);
        recipe.operationId = this.vatParams.operationId;
        return recipe;
    }

    public void build(Consumer<FinishedRecipe> consumer) {
        consumer.accept(new ProcessingRecipeBuilder.DataGenResult<>(this.build(), this.recipeConditions));
    }

    public static class FloatationCatalystParams {
        public String operationId = "floatation";

        public FloatationCatalystParams() {

        }
    }
}
