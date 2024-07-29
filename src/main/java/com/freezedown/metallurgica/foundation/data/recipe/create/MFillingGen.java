package com.freezedown.metallurgica.foundation.data.recipe.create;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.data.recipe.MProcessingRecipeGen;
import com.freezedown.metallurgica.registry.MetallurgicaFluids;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

public class MFillingGen extends MProcessingRecipeGen  {
    GeneratedRecipe
            chlorineTank = create(Metallurgica.asResource("chlorine_tank"), b -> b
            .require(F.chlorine(), 1000)
            .require(Items.BUCKET)
            .output(getBucket("chlorine"))),
    
    crudeTitaniumTetrachlorideBarrel = create(Metallurgica.asResource("crude_titanium_tetrachloride_barrel"), b -> b
            .require(MetallurgicaFluids.crudeTitaniumTetrachloride.get(), 1000)
            .require(Items.BUCKET)
            .output(getBucket("crude_titanium_tetrachloride"))),
    
    titaniumTetrachlorideBarrel = create(Metallurgica.asResource("titanium_tetrachloride_barrel"), b -> b
            .require(MetallurgicaFluids.titaniumTetrachloride.get(), 1000)
            .require(Items.BUCKET)
            .output(getBucket("titanium_tetrachloride"))),
    
    siliconTetrachlorideBarrel = create(Metallurgica.asResource("silicon_tetrachloride_barrel"), b -> b
            .require(MetallurgicaFluids.siliconTetrachloride.get(), 1000)
            .require(Items.BUCKET)
            .output(getBucket("silicon_tetrachloride"))),
    
    tinTetrachlorideBarrel = create(Metallurgica.asResource("tin_tetrachloride_barrel"), b -> b
            .require(MetallurgicaFluids.tinTetrachloride.get(), 1000)
            .require(Items.BUCKET)
            .output(getBucket("tin_tetrachloride"))),
    
    ironChlorideBarrel = create(Metallurgica.asResource("iron_chloride_barrel"), b -> b
            .require(MetallurgicaFluids.ironChloride.get(), 1000)
            .require(Items.BUCKET)
            .output(getBucket("iron_chloride")))
    ;
    
    public static Item getBucket(String name) {
        return Metallurgica.registrate.get(name+"_bucket", ForgeRegistries.ITEMS.getRegistryKey()).get();
    }
    
    public MFillingGen(DataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.FILLING;
    }
}
