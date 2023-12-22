package com.freezedown.metallurgica.content.blast_furnace;

import com.freezedown.metallurgica.registry.MetallurgicaRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class HeavyBlastingRecipe extends ProcessingRecipe<RecipeWrapper> {
    public HeavyBlastingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(MetallurgicaRecipeTypes.heavyBlasting, params);
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
        return inv.isEmpty() ? false : this.ingredients.get(0).test(inv.getItem(0));
    }
}
