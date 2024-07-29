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

public class MEmptyingGen extends MProcessingRecipeGen {
    
    GeneratedRecipe
    
    magnetiteLumps = create(Metallurgica.asResource("magnetite_lumps"), b -> b
            .require(I.magnetite())
            .output(I.magnetiteLumps(), 1)
            .output(F.magnetiteFines(), 15)
            .duration(200)),
    
    richMagnetiteLumps = create(Metallurgica.asResource("magnetite_lumps_from_rich"), b -> b
            .require(I.richMagnetite())
            .output(1.25f, I.magnetiteLumps(), 1)
            .output(F.magnetiteFines(), 5)
            .duration(200)),
    
    chlorineTank = create(Metallurgica.asResource("chlorine_tank"), b -> b
            .output(F.chlorine(), 1000)
            .output(Items.BUCKET)
            .require(getBucket("chlorine"))),
    
    crudeTitaniumTetrachlorideBarrel = create(Metallurgica.asResource("crude_titanium_tetrachloride_barrel"), b -> b
            .output(MetallurgicaFluids.crudeTitaniumTetrachloride.get(), 1000)
            .output(Items.BUCKET)
            .require(getBucket("crude_titanium_tetrachloride"))),
    
    titaniumTetrachlorideBarrel = create(Metallurgica.asResource("titanium_tetrachloride_barrel"), b -> b
            .output(MetallurgicaFluids.titaniumTetrachloride.get(), 1000)
            .output(Items.BUCKET)
            .require(getBucket("titanium_tetrachloride"))),
    
    siliconTetrachlorideBarrel = create(Metallurgica.asResource("silicon_tetrachloride_barrel"), b -> b
            .output(MetallurgicaFluids.siliconTetrachloride.get(), 1000)
            .output(Items.BUCKET)
            .require(getBucket("silicon_tetrachloride"))),
    
    tinTetrachlorideBarrel = create(Metallurgica.asResource("tin_tetrachloride_barrel"), b -> b
            .output(MetallurgicaFluids.tinTetrachloride.get(), 1000)
            .output(Items.BUCKET)
            .require(getBucket("tin_tetrachloride"))),
    
    ironChlorideBarrel = create(Metallurgica.asResource("iron_chloride_barrel"), b -> b
            .output(MetallurgicaFluids.ironChloride.get(), 1000)
            .output(Items.BUCKET)
            .require(getBucket("iron_chloride")))
    ;
    
    public static Item getBucket(String name) {
        return Metallurgica.registrate.get(name+"_bucket", ForgeRegistries.ITEMS.getRegistryKey()).get();
    }
    
    
    public MEmptyingGen(DataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.EMPTYING;
    }
}
