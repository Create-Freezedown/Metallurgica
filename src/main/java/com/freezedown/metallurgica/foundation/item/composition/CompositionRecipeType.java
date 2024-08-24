package com.freezedown.metallurgica.foundation.item.composition;

import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class CompositionRecipeType extends ProcessingRecipe<RecipeWrapper> {
    public CompositionRecipeType(IRecipeTypeInfo typeInfo, ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(typeInfo, params);
    }
    
    @Override
    protected int getMaxInputCount() {
        return 0;
    }
    
    @Override
    protected int getMaxOutputCount() {
        return 0;
    }
    
    @Override
    public boolean matches(RecipeWrapper recipeWrapper, Level level) {
        return false;
    }
}
