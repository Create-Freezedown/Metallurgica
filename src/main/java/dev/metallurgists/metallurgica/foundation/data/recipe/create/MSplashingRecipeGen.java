package dev.metallurgists.metallurgica.foundation.data.recipe.create;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.foundation.data.recipe.MProcessingRecipeGen;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public class MSplashingRecipeGen extends MProcessingRecipeGen {
    
    GeneratedRecipe
    
    washedAlumina = create(Metallurgica.asResource("washed_alumina"), b -> b
            .require(I.loosenedBauxite())
            .require(F.water(), 500)
            .output(I.washedAlumina(), 1)
            .output(0.35f, I.redSand(), 1))
    
    ;
    
    
    public static Item getBucket(String name) {
        return Metallurgica.registrate.get(name+"_bucket", ForgeRegistries.ITEMS.getRegistryKey()).get();
    }
    
    public MSplashingRecipeGen(DataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.SPLASHING;
    }
}
