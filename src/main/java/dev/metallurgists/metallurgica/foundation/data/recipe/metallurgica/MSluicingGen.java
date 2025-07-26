package dev.metallurgists.metallurgica.foundation.data.recipe.metallurgica;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.foundation.data.recipe.MProcessingRecipeGen;
import dev.metallurgists.metallurgica.registry.MetallurgicaRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;

public class MSluicingGen extends MProcessingRecipeGen {
    
    GeneratedRecipe
    
    goldSluicing = create(Metallurgica.asResource("gold"), b -> b
            .require(F.riverSand(), 1000)
            .require(F.water(), 1000)
            .output(I.goldNugget(), 1)
            .output(0.25f, I.goldNugget(), 1)
            .output(I.sand(), 1)
            .output(F.water(), 1000)
            .duration(100))
    
    ;
    
    public MSluicingGen(DataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return MetallurgicaRecipeTypes.sluicing;
    }
}
