package com.freezedown.metallurgica.content.machines.reverbaratory;

import com.freezedown.metallurgica.registry.MetallurgicaRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class ReverbaratoryCookingRecipe extends ProcessingRecipe<RecipeWrapper> {
    public ReverbaratoryCookingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(MetallurgicaRecipeTypes.reverbaratory_cooking, params);
    }
    
    protected int getMaxInputCount() {
        return 1;
    }
    
    protected int getMaxOutputCount() {
        return 0;
    }
    
    protected int getMaxFluidOutputCount() {
        return 2;
    }
    
    public boolean matches(RecipeWrapper inv, Level worldIn) {
        return inv.isEmpty() ? false : ((Ingredient)this.ingredients.get(0)).test(inv.getItem(0));
    }
}
