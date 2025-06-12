package com.freezedown.metallurgica.foundation.data.runtime.recipe;

import com.freezedown.metallurgica.foundation.data.runtime.recipe.handler.*;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.registry.material.MetMaterials;
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

        for (Material material : MetMaterials.registeredMaterials.values()) {
            PressingRecipeHandler.run(consumer, material);
            SequencedAssemblyHandler.run(consumer, material);
            StorageRecipeHandler.run(consumer, material);
            CrushingRecipeHandler.run(consumer, material);
            ItemApplicationHandler.run(consumer, material);
        }

    }

    public static void recipeRemoval() {
        RECIPE_FILTERS.clear();

        MetallurgicaRecipeRemoval.init(RECIPE_FILTERS::add);
    }
}
