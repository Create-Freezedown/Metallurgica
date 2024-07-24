package com.freezedown.metallurgica.foundation.data.recipe.create;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.data.recipe.MetallurgicaRecipeProvider;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import net.minecraft.data.DataGenerator;

import java.util.function.UnaryOperator;

import static com.freezedown.metallurgica.registry.MetallurgicaItems.*;

public class MSequencedAssemblyGen extends MetallurgicaRecipeProvider {
    
    GeneratedRecipe TiSheet = create("titanium_sheet", (b) -> {
        return b.require(titaniumIngot.get()).transitionTo(unprocessedTitaniumSheet.get()).addOutput(titaniumSheet.get(), 120.0F).loops(1).addStep(PressingRecipe::new, (rb) -> {
            return rb;
        }).addStep(PressingRecipe::new, (rb) -> {
            return rb;
        }).addStep(PressingRecipe::new, (rb) -> {
            return rb;
        });
    });
    
    GeneratedRecipe TiAlSheet = create("titanium_aluminide_sheet", (b) -> {
        return b.require(titaniumAluminideIngot.get()).transitionTo(unprocessedTitaniumAluminideSheet.get()).addOutput(titaniumAluminideSheet.get(), 120.0F).loops(1).addStep(PressingRecipe::new, (rb) -> {
            return rb;
        }).addStep(PressingRecipe::new, (rb) -> {
            return rb;
        }).addStep(PressingRecipe::new, (rb) -> {
            return rb;
        });
    });
    
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
