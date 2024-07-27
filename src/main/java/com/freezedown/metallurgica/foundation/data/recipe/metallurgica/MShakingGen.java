package com.freezedown.metallurgica.foundation.data.recipe.metallurgica;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.data.recipe.MProcessingRecipeGen;
import com.freezedown.metallurgica.registry.MetallurgicaItems;
import com.freezedown.metallurgica.registry.MetallurgicaMaterials;
import com.freezedown.metallurgica.registry.MetallurgicaRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Items;

public class MShakingGen extends MProcessingRecipeGen {
    
    GeneratedRecipe
    
    cassiteriteShaking = create(Metallurgica.asResource("cassiterite"), b -> b
            .require(MetallurgicaMaterials.CASSITERITE.MATERIAL.raw().get())
            .output(MetallurgicaMaterials.CASSITERITE.MATERIAL.rubble().get(), 1)
            .output(0.25f, MetallurgicaMaterials.CASSITERITE.MATERIAL.rubble().get(), 1)
            .output(0.25f, I.sand(), 1)
            .output(0.5f, Items.GRANITE, 1)
            .duration(100))
            
            ;
    
    public MShakingGen(DataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return MetallurgicaRecipeTypes.shaking;
    }
}

