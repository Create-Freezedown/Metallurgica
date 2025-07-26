package dev.metallurgists.metallurgica.foundation.data.recipe.metallurgica;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.foundation.data.recipe.MProcessingRecipeGen;
import dev.metallurgists.metallurgica.registry.MetallurgicaRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;

public class MReverbaratoryGen extends MProcessingRecipeGen {
    
    GeneratedRecipe
    
    iron = create(Metallurgica.asResource("molten_iron"), b -> b
            .require(I.magnetiteLumps())
            //.output(F.moltenIron(), 45)
            .output(F.slag(), 25)
            .duration(1200))
    
    ;
    
    
    public MReverbaratoryGen(DataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return MetallurgicaRecipeTypes.reverbaratory_cooking;
    }
}
