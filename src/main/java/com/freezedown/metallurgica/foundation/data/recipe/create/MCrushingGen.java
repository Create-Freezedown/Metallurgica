package com.freezedown.metallurgica.foundation.data.recipe.create;

import com.freezedown.metallurgica.foundation.data.recipe.MProcessingRecipeGen;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;

public class MCrushingGen extends MProcessingRecipeGen {
    
    //GeneratedRecipe
    
    //rutileRubble = create(Metallurgica.asResource("rutile_rubble"), b -> b
    //        .require(MetallurgicaOre.RUTILE.ORE.raw().get())
    //        .output(MetallurgicaOre.RUTILE.ORE.rubble().get(), 1)
    //        .output(0.05f, MetallurgicaOre.RUTILE.ORE.rubble().get(), 1)
    //        .duration(600))
    
    ;
    
    
    public MCrushingGen(DataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.CRUSHING;
    }
}
