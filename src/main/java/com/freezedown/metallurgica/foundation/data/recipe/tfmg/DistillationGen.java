package com.freezedown.metallurgica.foundation.data.recipe.tfmg;

import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.data.recipe.MProcessingRecipeGen;
import com.freezedown.metallurgica.registry.MetallurgicaFluids;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;

public class DistillationGen extends MProcessingRecipeGen {
    
    GeneratedRecipe
    
    titaniumTetrachloride = create(Metallurgica.asResource("titanium_tetrachloride"), b -> b
            .require(MetallurgicaFluids.crudeTitaniumTetrachloride.get(), 420)
            .output(MetallurgicaFluids.ironChloride.get(), 60)
            .output(MetallurgicaFluids.titaniumTetrachloride.get(), 240)
            .output(MetallurgicaFluids.tinTetrachloride.get(), 90)
            .output(MetallurgicaFluids.siliconTetrachloride.get(), 30))
    
    ;
    
    public DistillationGen(DataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return TFMGRecipeTypes.DISTILLATION;
    }
}
