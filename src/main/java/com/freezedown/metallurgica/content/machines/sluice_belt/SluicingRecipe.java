package com.freezedown.metallurgica.content.machines.sluice_belt;

import com.freezedown.metallurgica.registry.MetallurgicaRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class SluicingRecipe extends ProcessingRecipe<RecipeWrapper> {

    public SluicingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(MetallurgicaRecipeTypes.sluicing, params);
    }

    protected int getMaxInputCount() { return 0; }

    protected int getMaxFluidInputCount() { return 0; }

    protected int getMaxOutputCount() { return 0; }

    protected int getMaxFluidOutputCount() { return 0; }

    @Override
    public boolean matches(RecipeWrapper inv, Level level) {
        return inv.isEmpty() ? false : this.ingredients.get(0).test(inv.getItem(0)); //I basically just copied the HeavyBlasting recipe code for this
    }
}
