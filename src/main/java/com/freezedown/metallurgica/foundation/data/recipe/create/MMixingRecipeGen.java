package com.freezedown.metallurgica.foundation.data.recipe.create;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.data.recipe.MProcessingRecipeGen;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;

public class MMixingRecipeGen extends MProcessingRecipeGen {
    
    GeneratedRecipe
    
    aluminaSolution = create(Metallurgica.asResource("loosened_bauxite"), b -> b
            .require(I.bauxite())
            .require(F.sodiumHydroxide(), 500)
            .output(I.loosenedBauxite(), 1)),
            
    sulfuricAcid = create(Metallurgica.asResource("sulfuric_acid"), b -> b
            .require(I.sulfurDust())
            .require(I.sulfurDust())
            .require(I.nitrateDust())
            .require(F.water(), 20)
            .output(F.sulfuricAcid(), 20)
            .requiresHeat(HeatCondition.SUPERHEATED)),
    
    sulfuricAcidDecontaminated = create(Metallurgica.asResource("sulfuric_acid_from_decontaminated_water"), b -> b
            .require(I.sulfurDust())
            .require(I.sulfurDust())
            .require(I.nitrateDust())
            .require(F.decontaminatedWater(), 20)
            .output(F.sulfuricAcid(), 40)
            .requiresHeat(HeatCondition.SUPERHEATED)),
    
    chlorineDisposal = create(Metallurgica.asResource("chlorine_disposal"), b -> b
            .require(F.chlorine(), 10)
            .require(F.sodiumHydroxide(), 40)
            .output(F.water(), 10)
            .output(0.15f, I.salt())),
    
    sodiumHypochlorite = create(Metallurgica.asResource("sodium_hypochlorite"), b -> b
            .require(F.chlorine(), 20)
            .require(F.sodiumHydroxide(), 10)
            .output(F.sodiumHypochlorite(), 30)
            .requiresHeat(HeatCondition.SUPERHEATED)),
            
    decontaminatedWater = create(Metallurgica.asResource("decontaminated_water"), b -> b
            .require(F.water(), 10)
            .require(F.sodiumHypochlorite(), 10)
            .output(F.decontaminatedWater(), 10)),
    
    magnetiteLumps = create(Metallurgica.asResource("magnetite_lumps"), b -> b
            .require(F.magnetiteFines(), 5)
            .require(F.decontaminatedWater(), 10)
            .require(I.cokeDust())
            .require(I.cokeDust())
            .output(I.magnetiteLumps(), 1)
            .output(0.25f, I.magnetiteLumps(), 1)
            .requiresHeat(HeatCondition.HEATED))
            
    ;
    
    public MMixingRecipeGen(DataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.MIXING;
    }
}
