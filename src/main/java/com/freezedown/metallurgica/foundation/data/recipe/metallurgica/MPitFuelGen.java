package com.freezedown.metallurgica.foundation.data.recipe.metallurgica;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.data.recipe.MProcessingRecipeGen;
import com.freezedown.metallurgica.registry.MetallurgicaRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Items;

public class MPitFuelGen extends MProcessingRecipeGen {

    GeneratedRecipe

    charcoal = create(Metallurgica.asResource("charcoal"), b -> b
            .require(Items.CHARCOAL));

    public MPitFuelGen(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return MetallurgicaRecipeTypes.pit_fuel;
    }
}
