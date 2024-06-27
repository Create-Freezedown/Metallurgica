package com.freezedown.metallurgica.foundation.data.recipe.create;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.data.recipe.MProcessingRecipeGen;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;

public class MSplashingRecipeGen extends MProcessingRecipeGen {
    
    GeneratedRecipe
    
    washedAlumina = create(Metallurgica.asResource("washed_alumina"), b -> b
            .require(I.loosenedBauxite())
            .require(F.water(), 500)
            .output(I.washedAlumina(), 1)
            .output(0.35f, I.redSand(), 1))
    
    ;
    
    
    
    public MSplashingRecipeGen(DataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.SPLASHING;
    }
}
