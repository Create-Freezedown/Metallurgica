package dev.metallurgists.metallurgica.foundation.data.recipe.metallurgica;

import com.drmangotea.tfmg.registry.TFMGFluids;
import dev.metallurgists.metallurgica.content.machines.vat.floatation_cell.FlotationCatalystBuilder;
import dev.metallurgists.metallurgica.foundation.data.recipe.MProcessingRecipeGen;
import dev.metallurgists.metallurgica.registry.MetallurgicaRecipeTypes;
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
