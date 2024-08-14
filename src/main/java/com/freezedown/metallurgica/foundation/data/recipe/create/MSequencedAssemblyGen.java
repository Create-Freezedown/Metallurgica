package com.freezedown.metallurgica.foundation.data.recipe.create;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.data.recipe.MetallurgicaRecipeProvider;
import com.freezedown.metallurgica.registry.MetallurgicaMetals;
import com.freezedown.metallurgica.registry.MetallurgicaTools;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Items;

import java.util.function.UnaryOperator;

import static com.freezedown.metallurgica.registry.MetallurgicaItems.*;

public class MSequencedAssemblyGen extends MetallurgicaRecipeProvider {
    
    GeneratedRecipe

    TiSheet = create("titanium_sheet", (b) -> b
            .require(titaniumIngot.get())
            .transitionTo(semiPressedTitaniumSheet.get())
            .addOutput(titaniumSheet.get(), 120.0F)
            .loops(3)
            .addStep(PressingRecipe::new, (rb) -> rb)),

    TiAlSheet = create("titanium_aluminide_sheet", (b) -> b
            .require(titaniumAluminideIngot.get())
            .transitionTo(semiPressedTitaniumAluminideSheet.get())
            .addOutput(titaniumAluminideSheet.get(), 120.0F)
            .loops(3)
            .addStep(PressingRecipe::new, (rb) -> rb));

    //{
    //    for (MetallurgicaTools pTool : MetallurgicaTools.values()) {
    //        for (MetallurgicaMetals pMetal : MetallurgicaMetals.values()) {
    //            if () {
    //                GeneratedRecipe tool = create(pTool.name().toLowerCase(), (b) -> b
    //                        .require(Items.STICK)
    //                        .transitionTo()
    //                        .addOutput()
    //                        .loops(1)
    //                        .addStep()
    //                );
    //            }
    //        }
    //    }
    //}




    
    public MSequencedAssemblyGen(DataGenerator generator) {
        super(generator);
    }
    
    protected GeneratedRecipe create(String name, UnaryOperator<SequencedAssemblyRecipeBuilder> transform) {
        GeneratedRecipe generatedRecipe = (c) -> {
            transform.apply(new SequencedAssemblyRecipeBuilder(Metallurgica.asResource(name))).build(c);
        };
        this.all.add(generatedRecipe);
        return generatedRecipe;
    }
    
    public String getName() {
        return "Metallurgica's Sequenced Assembly Recipes";
    }
}
