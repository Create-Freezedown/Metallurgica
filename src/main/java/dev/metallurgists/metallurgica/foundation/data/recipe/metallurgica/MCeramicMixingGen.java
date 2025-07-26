package dev.metallurgists.metallurgica.foundation.data.recipe.metallurgica;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.foundation.data.recipe.MProcessingRecipeGen;
import dev.metallurgists.metallurgica.registry.MetallurgicaRecipeTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Items;

public class MCeramicMixingGen extends MProcessingRecipeGen {
    
    GeneratedRecipe
    
    leather = create(Metallurgica.asResource("leather"), b -> b
            .require(Items.ROTTEN_FLESH)
            .require(Items.ROTTEN_FLESH)
            .require(Items.ROTTEN_FLESH)
            .require(Items.ROTTEN_FLESH)
            .require(F.water(), 1000)
            .output(Items.LEATHER, 2)
            .requiresHeat(HeatCondition.HEATED)
            .duration(200))
    
    ;
    
    
    public MCeramicMixingGen(DataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return MetallurgicaRecipeTypes.ceramic_mixing;
    }
}
