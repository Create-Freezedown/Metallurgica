package com.freezedown.metallurgica.content.machines.vat.floatation_cell;

import com.freezedown.metallurgica.registry.MetallurgicaRecipeTypes;
import com.google.gson.JsonObject;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.item.SmartInventory;
import lombok.Getter;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

public class FlotationCatalyst extends ProcessingRecipe<SmartInventory> {
    @Getter
    public float efficiencyMultiplier = 1;

    public FlotationCatalyst(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(MetallurgicaRecipeTypes.flotation_catalyst, params);
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
    protected int getMaxFluidInputCount() {
        return 1;
    }

    public void readAdditional(JsonObject json) {
        super.readAdditional(json);
        this.efficiencyMultiplier = json.get("efficiencyMultiplier").getAsFloat();
    }

    public void writeAdditional(JsonObject json) {
        super.writeAdditional(json);
        json.addProperty("efficiencyMultiplier", this.efficiencyMultiplier);
    }

    @Override
    public boolean matches(SmartInventory smartInventory, Level level) {
        return false;
    }

    public boolean matches(FluidStack fluid) {
        return getFluidIngredients().get(0).test(fluid);
    }
}
