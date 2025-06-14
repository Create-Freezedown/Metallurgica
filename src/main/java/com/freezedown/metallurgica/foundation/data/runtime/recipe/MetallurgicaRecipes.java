package com.freezedown.metallurgica.foundation.data.runtime.recipe;

import com.freezedown.metallurgica.foundation.data.runtime.recipe.handler.*;
import com.freezedown.metallurgica.foundation.material.registry.Material;
import com.freezedown.metallurgica.foundation.material.registry.flags.FlagKey;
import com.freezedown.metallurgica.foundation.material.registry.flags.base.IRecipeHandler;
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
            for (FlagKey<?> flagKey : material.getFlags().getFlagKeys()) {
                var flag = material.getFlag(flagKey);
                if (flag instanceof IRecipeHandler handler) {
                    handler.run(consumer, material);
                }
            }
            PressingRecipeHandler.run(consumer, material);
            SequencedAssemblyHandler.run(consumer, material);
            StorageRecipeHandler.run(consumer, material);
            CrushingRecipeHandler.run(consumer, material);
            ItemApplicationHandler.run(consumer, material);

            RecyclingHandler.run(consumer, material);
        }

    }

    public static void recipeRemoval() {
        RECIPE_FILTERS.clear();

        MetallurgicaRecipeRemoval.init(RECIPE_FILTERS::add);
    }
}
