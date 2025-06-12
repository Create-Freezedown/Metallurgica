package com.freezedown.metallurgica.compat.jei;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.items.reaction.FluidReactionRecipe;
import mezz.jei.api.recipe.RecipeType;

public class MetallurgicaJeiRecipeTypes {
    public static final RecipeType<FluidReactionRecipe> FLUID_REACTION = RecipeType.create(Metallurgica.ID, "fluid_reaction", FluidReactionRecipe.class);
}
