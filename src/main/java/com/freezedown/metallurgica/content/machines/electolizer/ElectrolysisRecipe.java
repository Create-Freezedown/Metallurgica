package com.freezedown.metallurgica.content.machines.electolizer;

import com.freezedown.metallurgica.registry.MetallurgicaRecipeTypes;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;

public class ElectrolysisRecipe extends BasinRecipe {
    public ElectrolysisRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(MetallurgicaRecipeTypes.electrolysis, params);
    }
}
