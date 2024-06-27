package com.freezedown.metallurgica.foundation.data.recipe.create;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.data.recipe.MProcessingRecipeGen;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;

public class MMixingRecipeGen extends MProcessingRecipeGen {
    
    GeneratedRecipe
            
            aluminaSolution = create(Metallurgica.asResource("loosened_bauxite"), b -> b
            .require(I.bauxite())
            .require(F.sodiumHydroxide(), 500)
            .output(I.loosenedBauxite(), 1)
            .duration(200)),
            
            sulfuricAcid = create(Metallurgica.asResource("sulfuric_acid"), b -> b
            .require(I.sulfurDust())
            .require(I.sulfurDust())
            .require(F.water(), 800)
            .output(F.sulfuricAcid(), 800)
            .requiresHeat(HeatCondition.HEATED))
    ;
    
    public MMixingRecipeGen(DataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.MIXING;
    }
}
