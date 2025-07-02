package com.freezedown.metallurgica.foundation.data.recipe.create;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.data.recipe.MProcessingRecipeGen;
import com.freezedown.metallurgica.registry.MetallurgicaItems;
import com.freezedown.metallurgica.registry.MetallurgicaTags;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;

import static com.freezedown.metallurgica.registry.MetallurgicaItems.*;

public class MPressingGen extends MProcessingRecipeGen {
    
    //GeneratedRecipe
    
    //aluminiumSheet = create(Metallurgica.asResource("aluminum_sheet"), b -> b
    //        .require(MetallurgicaTags.forgeItemTag("ingots/aluminum"))
    //        .output(aluminumSheet.get(), 1)),
            
    //bronzeSheet = create(Metallurgica.asResource("bronze_sheet"), b -> b
    //        .require(MetallurgicaTags.forgeItemTag("ingots/bronze"))
    //        .output(MetallurgicaItems.bronzeSheet.get(), 1))
    
    ;
    
    public MPressingGen(DataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.PRESSING;
    }
}
