package com.freezedown.metallurgica.foundation.data.runtime.recipe.handler;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.infastructure.material.Material;
import com.freezedown.metallurgica.infastructure.material.registry.flags.FlagKey;
import com.simibubi.create.content.kinetics.crusher.CrushingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
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
        if (material.hasFlag(FlagKey.RUBBLE)) {
            var rubbleFlag = material.getFlag(FlagKey.RUBBLE);
            var mineralFlag = material.getFlag(FlagKey.MINERAL);
            ResourceLocation rubbleId = new ResourceLocation(rubbleFlag.getExistingNamespace(), rubbleFlag.getIdPattern().formatted(material.getName()));
            ResourceLocation mineralId = new ResourceLocation(mineralFlag.getExistingNamespace(), mineralFlag.getIdPattern().formatted(material.getName()));
            if (!rubbleId.getNamespace().equals(Metallurgica.ID)) {
                logRecipeSkip(rubbleId);
            }
            if (rubbleFlag.isCrushing()) {
                ProcessingRecipeBuilder<CrushingRecipe> builder = new Builder<>(mineralId.getNamespace(), CrushingRecipe::new, mineralId.getPath(), rubbleId.getPath(), provider);
                builder.require(BuiltInRegistries.ITEM.get(mineralId));
                builder.output(BuiltInRegistries.ITEM.get(rubbleId));
                if (rubbleFlag.getBonusChance() > 0) {
                    builder.output(rubbleFlag.getBonusChance(), BuiltInRegistries.ITEM.get(rubbleId));
                }
                builder.averageProcessingDuration();
                builder.build();
            }
        }
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
