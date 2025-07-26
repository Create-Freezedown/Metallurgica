package dev.metallurgists.metallurgica.foundation.data.runtime;

import dev.metallurgists.metallurgica.Metallurgica;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.Collections;
import java.util.function.Consumer;

public class RuntimeProcessingRecipeBuilder<T extends ProcessingRecipe<?>> extends ProcessingRecipeBuilder<T> {
    Consumer<FinishedRecipe> consumer;

    public RuntimeProcessingRecipeBuilder(ProcessingRecipeBuilder.ProcessingRecipeFactory<T> factory, Consumer<FinishedRecipe> consumer, String recipePath) {
        super(factory, Metallurgica.asResource("runtime_generated/" + recipePath));
        this.consumer = consumer;
    }

    @Override
    public T build() {
        T t = super.build();
        DataGenResult<T> result = new DataGenResult<>(t, Collections.emptyList());
        consumer.accept(result);
        return t;
    }
}
