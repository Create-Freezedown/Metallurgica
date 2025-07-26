package dev.metallurgists.metallurgica.compat.jei;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.content.items.reaction.FluidReactionRecipe;
import mezz.jei.api.recipe.RecipeType;

public class MetallurgicaJeiRecipeTypes {
    public static final RecipeType<FluidReactionRecipe> FLUID_REACTION = RecipeType.create(Metallurgica.ID, "fluid_reaction", FluidReactionRecipe.class);
}
