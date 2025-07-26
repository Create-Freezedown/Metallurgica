package dev.metallurgists.metallurgica.content.primitive.pit_smelting;

import dev.metallurgists.metallurgica.registry.MetallurgicaRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;

public class PitFuelRecipe extends ProcessingRecipe<Inventory> {

    public PitFuelRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(MetallurgicaRecipeTypes.pit_fuel, params);
    }

    @Override
    protected int getMaxInputCount() {
        return 1;
    }

    @Override
    protected int getMaxOutputCount() {
        return 0;
    }

    protected boolean canSpecifyDuration() {
        return false;
    }

    @Override
    public boolean matches(Inventory inventory, Level level) {
        return inventory.isEmpty() ? false : this.ingredients.get(0).test(inventory.getSelected());
    }
}
