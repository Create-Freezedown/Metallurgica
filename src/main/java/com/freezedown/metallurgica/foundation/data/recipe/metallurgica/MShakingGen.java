package com.freezedown.metallurgica.foundation.data.recipe.metallurgica;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.data.recipe.MProcessingRecipeGen;
import com.freezedown.metallurgica.registry.MetallurgicaOre;
import com.freezedown.metallurgica.registry.MetallurgicaRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Items;

public class MShakingGen extends MProcessingRecipeGen {
    
    GeneratedRecipe
    
    cassiteriteShaking = create(Metallurgica.asResource("cassiterite"), b -> b
            .require(MetallurgicaOre.CASSITERITE.ORE.raw().get())
            .output(MetallurgicaOre.CASSITERITE.ORE.rubble().get(), 1)
            .output(0.25f, MetallurgicaOre.CASSITERITE.ORE.rubble().get(), 1)
            .output(0.25f, I.sand(), 1)
            .output(0.5f, Items.GRANITE, 1)
            .duration(600)),
    
    goldNuggetFromRedSand = create(Metallurgica.asResource("gold_nugget_from_red_sand"), b -> b
            .require(Items.RED_SAND)
            .require(F.water(), 250)
            .output(0.23f, Items.GOLD_NUGGET, 3)
            .output(0.42f, Items.GOLD_NUGGET, 1)
            .output(0.1f, Items.DEAD_BUSH, 1)
            .duration(200))
            
            ;
    
    public MShakingGen(DataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return MetallurgicaRecipeTypes.shaking;
    }
}

