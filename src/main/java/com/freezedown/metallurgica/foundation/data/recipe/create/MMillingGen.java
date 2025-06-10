package com.freezedown.metallurgica.foundation.data.recipe.create;

import com.freezedown.metallurgica.foundation.data.recipe.MProcessingRecipeGen;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;

public class MMillingGen extends MProcessingRecipeGen {
    
    //GeneratedRecipe
    //
    //rutilePowder = create(Metallurgica.asResource("rutile_powder"), b -> b
    //        .require(MetallurgicaOre.RUTILE.ORE.rubble().get())
    //        .output(MetallurgicaItems.rutilePowder.get(), 1)
    //        .output(0.25f, MetallurgicaItems.rutilePowder.get(), 1)
    //        .duration(600))
    ;
    
    
    public MMillingGen(DataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.MILLING;
    }
}
