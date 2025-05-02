package com.freezedown.metallurgica.foundation.data.runtime.recipe;

import com.freezedown.metallurgica.foundation.data.runtime.recipe.handler.PressingRecipeHandler;
import com.freezedown.metallurgica.foundation.data.runtime.recipe.handler.SequencedAssemblyHandler;
import com.freezedown.metallurgica.registry.misc.MetallurgicaMaterials;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;
import java.util.function.Consumer;

public class MetallurgicaRecipes {
    public static final Set<ResourceLocation> RECIPE_FILTERS = new ObjectOpenHashSet<>();

    public static void recipeAddition(Consumer<FinishedRecipe> originalConsumer) {
        Consumer<FinishedRecipe> consumer = recipe -> {
            if (!RECIPE_FILTERS.contains(recipe.getId())) {
                originalConsumer.accept(recipe);
            }
        };

        for (MetallurgicaMaterials material : MetallurgicaMaterials.values()) {
            PressingRecipeHandler.run(consumer, material.getMaterial());
            SequencedAssemblyHandler.run(consumer, material.getMaterial());
        }

    }

    public static void recipeRemoval() {
        RECIPE_FILTERS.clear();

        MetallurgicaRecipeRemoval.init(RECIPE_FILTERS::add);
    }
}
