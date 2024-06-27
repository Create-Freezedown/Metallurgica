package com.freezedown.metallurgica.foundation.data.recipe.create;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.data.recipe.MProcessingRecipeGen;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;

public class MEmptyingGen extends MProcessingRecipeGen {
    
    GeneratedRecipe
    
    magnetiteLumps = create(Metallurgica.asResource("magnetite_lumps"), b -> b
            .require(I.magnetite())
            .output(I.magnetiteLumps(), 1)
            .output(F.magnetiteFines(), 15)
            .duration(200)),
    richMagnetiteLumps = create(Metallurgica.asResource("magnetite_lumps_from_rich"), b -> b
            .require(I.richMagnetite())
            .output(I.magnetiteLumps(), 2)
            .output(0.25f, I.magnetiteLumps(), 1)
            .output(F.magnetiteFines(), 5)
            .duration(200))
    
    ;
    
    
    public MEmptyingGen(DataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.EMPTYING;
    }
}
