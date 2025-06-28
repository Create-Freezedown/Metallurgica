package com.freezedown.metallurgica.foundation.data.recipe.metallurgica;

import com.drmangotea.tfmg.registry.TFMGFluids;
import com.freezedown.metallurgica.content.machines.vat.floatation_cell.FloatationCatalystBuilder;
import com.freezedown.metallurgica.foundation.data.recipe.MProcessingRecipeGen;
import com.freezedown.metallurgica.registry.MetallurgicaRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;

public class MFlotationCatalystGen extends MProcessingRecipeGen {

    GeneratedRecipe
        air = createFloatationCatalyst("air", (b) -> (FloatationCatalystBuilder)b
            .require(TFMGFluids.AIR.get(), 1), named("metallurgica:air_floatation"))

    ;

    public MFlotationCatalystGen(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return MetallurgicaRecipeTypes.floatation_catalyst;
    }

    public FloatationCatalystBuilder.FloatationCatalystParams named(String operationId) {
        FloatationCatalystBuilder.FloatationCatalystParams params = new FloatationCatalystBuilder.FloatationCatalystParams();
        params.operationId = operationId;
        return params;
    }
}
