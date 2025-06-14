package com.freezedown.metallurgica.foundation.data.runtime.recipe.handler;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.material.MaterialHelper;
import com.freezedown.metallurgica.foundation.material.registry.Material;
import com.freezedown.metallurgica.foundation.material.registry.flags.FlagKey;
import com.freezedown.metallurgica.foundation.material.registry.flags.item.IngotFlag;
import com.freezedown.metallurgica.foundation.material.registry.flags.item.SheetFlag;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.function.Consumer;

public class PressingRecipeHandler {

    private PressingRecipeHandler() {}

    public static void run(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        processPressing(provider, material);
    }

    private static void processPressing(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        if (material.hasFlag(FlagKey.SHEET) && !material.hasFlag(FlagKey.SEMI_PRESSED_SHEET)) {
            if (material.noRegister(FlagKey.SHEET)) {
                Metallurgica.LOGGER.info("Skipping pressing recipe for {} as it is not in the metallurgica namespace and likely already has one", material.getName() + "_sheet");
                return;
            }
            Item ingot = MaterialHelper.getCompatibleItem(material, FlagKey.INGOT);
            Item sheet = MaterialHelper.getCompatibleItem(material, FlagKey.SHEET);
            ProcessingRecipeBuilder<PressingRecipe> builder = new Builder<>(material.getNamespace(), PressingRecipe::new, material.getName() + "_sheet", provider);
            builder.require(ingot).output(sheet).build();
        }
    }

    private static class Builder<T extends ProcessingRecipe<?>> extends ProcessingRecipeBuilder<T> {
        Consumer<FinishedRecipe> consumer;
        public Builder(String modid, ProcessingRecipeBuilder.ProcessingRecipeFactory<T> factory, String to, Consumer<FinishedRecipe> consumer) {
            super(factory, Metallurgica.asResource("runtime_generated/" + modid + "/" + to));
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
