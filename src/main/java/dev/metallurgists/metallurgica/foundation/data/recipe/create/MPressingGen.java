package dev.metallurgists.metallurgica.foundation.data.recipe.create;

import dev.metallurgists.metallurgica.foundation.data.recipe.MProcessingRecipeGen;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;

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
