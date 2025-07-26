package dev.metallurgists.metallurgica.foundation.data.recipe.create;

import dev.metallurgists.metallurgica.foundation.data.recipe.MProcessingRecipeGen;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;

public class MCompactingGen extends MProcessingRecipeGen {
    
    //GeneratedRecipe
    
    //TiIngot = create(Metallurgica.asResource("titanium_ingot"), b -> b
    //        .require(MetallurgicaTags.forgeItemTag("nuggets/titanium"))
    //        .require(MetallurgicaTags.forgeItemTag("nuggets/titanium"))
    //        .require(MetallurgicaTags.forgeItemTag("nuggets/titanium"))
    //        .require(MetallurgicaTags.forgeItemTag("nuggets/titanium"))
    //        .require(MetallurgicaTags.forgeItemTag("nuggets/titanium"))
    //        .require(MetallurgicaTags.forgeItemTag("nuggets/titanium"))
    //        .require(MetallurgicaTags.forgeItemTag("nuggets/titanium"))
    //        .require(MetallurgicaTags.forgeItemTag("nuggets/titanium"))
    //        .require(MetallurgicaTags.forgeItemTag("nuggets/titanium"))
    //        .output(MetallurgicaItems.titaniumIngot.get(), 1)),
    
    //TiAlIngot = create(Metallurgica.asResource("titanium_aluminide_ingot"), b -> b
    //        .require(MetallurgicaTags.forgeItemTag("nuggets/titanium_aluminide"))
    //        .require(MetallurgicaTags.forgeItemTag("nuggets/titanium_aluminide"))
    //        .require(MetallurgicaTags.forgeItemTag("nuggets/titanium_aluminide"))
    //        .require(MetallurgicaTags.forgeItemTag("nuggets/titanium_aluminide"))
    //        .require(MetallurgicaTags.forgeItemTag("nuggets/titanium_aluminide"))
    //        .require(MetallurgicaTags.forgeItemTag("nuggets/titanium_aluminide"))
    //        .require(MetallurgicaTags.forgeItemTag("nuggets/titanium_aluminide"))
    //        .require(MetallurgicaTags.forgeItemTag("nuggets/titanium_aluminide"))
    //        .require(MetallurgicaTags.forgeItemTag("nuggets/titanium_aluminide"))
    //        .output(MetallurgicaItems.titaniumAluminideIngot.get(), 1))
    
    ;
    
    public MCompactingGen(DataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.COMPACTING;
    }
}
