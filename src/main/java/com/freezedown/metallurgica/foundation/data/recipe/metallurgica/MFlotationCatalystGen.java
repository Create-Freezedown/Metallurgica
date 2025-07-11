package com.freezedown.metallurgica.foundation.data.recipe.metallurgica;

import com.drmangotea.tfmg.registry.TFMGFluids;
import com.freezedown.metallurgica.content.machines.vat.floatation_cell.FlotationCatalystBuilder;
import com.freezedown.metallurgica.foundation.data.recipe.MProcessingRecipeGen;
import com.freezedown.metallurgica.registry.MetallurgicaRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;

public class MFlotationCatalystGen extends MProcessingRecipeGen {

    GeneratedRecipe
        air = createFloatationCatalyst("air", (b) -> (FlotationCatalystBuilder)b
            .require(TFMGFluids.AIR.get(), 1), efficiency(1f))

    ;

    public MFlotationCatalystGen(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return MetallurgicaRecipeTypes.flotation_catalyst;
    }

    public FlotationCatalystBuilder.FlotationCatalystParams efficiency(float efficiencyMultiplier) {
        FlotationCatalystBuilder.FlotationCatalystParams params = new FlotationCatalystBuilder.FlotationCatalystParams();
        params.efficiencyMultiplier = efficiencyMultiplier;
        return params;
    }
}
