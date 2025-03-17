package com.freezedown.metallurgica.foundation.data.recipe.metallurgica;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.fluids.types.MoltenMetal;
import com.freezedown.metallurgica.foundation.data.recipe.MProcessingRecipeGen;
import com.freezedown.metallurgica.registry.MetallurgicaFluids;
import com.freezedown.metallurgica.registry.MetallurgicaRecipeTypes;
import com.freezedown.metallurgica.registry.misc.MetallurgicaMaterials;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;

public class MElectrolysisGen extends MProcessingRecipeGen {
    
    GeneratedRecipe
    
    sodiumHydroxide = create(Metallurgica.asResource("sodium_hydroxide"), b -> b
            .require(I.salt())
            .require(F.water(), 500)
            .output(F.sodiumHydroxide(), 300)
            .output(F.chlorine(), 200)
            .requiresHeat(HeatCondition.HEATED)
            .duration(200)),
    
    sodiumHydroxideDecontaminated = create(Metallurgica.asResource("sodium_hydroxide_from_decontaminated_water"), b -> b
            .require(I.salt())
            .require(F.decontaminatedWater(), 500)
            .output(F.sodiumHydroxide(), 400)
            .output(F.chlorine(), 100)
            .requiresHeat(HeatCondition.HEATED)
            .duration(200)),
    
    magnesiumChloride = create(Metallurgica.asResource("magnesium_chloride"), b -> b
            .require(MetallurgicaFluids.magnesiumChloride.get(), 500)
            .output(MetallurgicaMaterials.MAGNESIUM.getMaterialEntry().getMaterial().getFluid(MoltenMetal.class), 300)
            .output(F.chlorine(), 200)
            .requiresHeat(HeatCondition.HEATED)
            .duration(600))
    
    ;
    
    
    public MElectrolysisGen(DataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return MetallurgicaRecipeTypes.electrolysis;
    }
}
