package com.freezedown.metallurgica.content.machines.sluice_belt;

import com.freezedown.metallurgica.registry.MetallurgicaRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.item.SmartInventory;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class SluicingRecipe extends ProcessingRecipe<SmartInventory> {

    public SluicingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(MetallurgicaRecipeTypes.sluicing, params);
    }

    protected int getMaxInputCount() { return 0; }

    protected int getMaxFluidInputCount() { return 1; }

    protected int getMaxOutputCount() { return 4; }

    protected int getMaxFluidOutputCount() { return 1; }
    
    protected boolean canSpecifyDuration() {
        return true;
    }
    
    public boolean matches(CombinedTankWrapper inv, Level worldIn) {
        if (inv.getFluidInTank(0).getAmount()==0)
            return false;
        return fluidIngredients.get(0).test(inv.getFluidInTank(0));
    }
    
    @Override
    public boolean matches(SmartInventory smartInventory, Level level) {
        return false;
    }
}
