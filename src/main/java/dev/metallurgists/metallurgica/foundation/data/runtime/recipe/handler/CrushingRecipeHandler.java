package dev.metallurgists.metallurgica.foundation.data.runtime.recipe.handler;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.infastructure.material.Material;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.function.Consumer;

public class CrushingRecipeHandler {

    private CrushingRecipeHandler() {}

    public static void run(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        processCrushing(provider, material);
    }

    private static void processCrushing(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {

    }

    private static void logRecipeSkip(ResourceLocation rubbleId) {
        Metallurgica.LOGGER.info("Skipping crushing recipe for {} as it is not in the metallurgica namespace and likely already has one", rubbleId);
    }

    private static class Builder<T extends ProcessingRecipe<?>> extends ProcessingRecipeBuilder<T> {
        Consumer<FinishedRecipe> consumer;
        public Builder(String modid, ProcessingRecipeBuilder.ProcessingRecipeFactory<T> factory, String from, String to, Consumer<FinishedRecipe> consumer) {
            super(factory, Metallurgica.asResource("runtime_generated/" + modid + "/" + from + "_to_" + to));
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
}
