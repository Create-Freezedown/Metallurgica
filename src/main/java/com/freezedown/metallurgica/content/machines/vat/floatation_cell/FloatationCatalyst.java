package com.freezedown.metallurgica.content.machines.vat.floatation_cell;

import com.freezedown.metallurgica.registry.MetallurgicaRecipeTypes;
import com.google.gson.JsonObject;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.item.SmartInventory;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

public class FloatationCatalyst extends ProcessingRecipe<SmartInventory> {
    public String operationId;

    public FloatationCatalyst(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(MetallurgicaRecipeTypes.floatation_catalyst, params);
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
        this.operationId = json.get("operationId").getAsString();
    }

    public void writeAdditional(JsonObject json) {
        super.writeAdditional(json);
        json.addProperty("operationId", this.operationId);
    }

    @Override
    public boolean matches(SmartInventory smartInventory, Level level) {
        return false;
    }

    public boolean matches(FluidStack fluid) {
        return getFluidIngredients().get(0).test(fluid);
    }
}
