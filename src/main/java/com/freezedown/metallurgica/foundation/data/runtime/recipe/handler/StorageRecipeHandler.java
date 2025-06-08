package com.freezedown.metallurgica.foundation.data.runtime.recipe.handler;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey;
import com.freezedown.metallurgica.foundation.item.registry.flags.IngotFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.NuggetFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.StorageBlockFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.IMaterialFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.ItemFlag;
import com.freezedown.metallurgica.foundation.material.MaterialHelper;
import com.simibubi.create.content.kinetics.mixer.CompactingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.function.Consumer;

import static com.tterrag.registrate.providers.RegistrateRecipeProvider.has;

public class StorageRecipeHandler {

    private StorageRecipeHandler() {}

    public static void run(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        processNugget(provider, material);
        processIngot(provider, material);
    }

    private static void processNugget(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        if (material.hasFlag(FlagKey.NUGGET)) {
            NuggetFlag nuggetFlag = material.getFlag(FlagKey.NUGGET);
            IngotFlag ingotFlag = material.getFlag(FlagKey.INGOT);
            ResourceLocation inputId = new ResourceLocation(nuggetFlag.getExistingNamespace(), nuggetFlag.getIdPattern().formatted(material.getName()));
            ResourceLocation outputId = new ResourceLocation(ingotFlag.getExistingNamespace(), ingotFlag.getIdPattern().formatted(material.getName()));
            if (!outputId.getNamespace().equals(Metallurgica.ID)) {
                logRecipeSkip(outputId);
                return;
            }
            if (nuggetFlag.isRequiresCompacting()) {
                compact9(provider, inputId, outputId);
            } else {
                craftCompact9(provider, inputId, outputId, MaterialHelper.getItem(material, FlagKey.NUGGET), MaterialHelper.getItem(material, FlagKey.INGOT));
            }
        }
    }

    private static void processIngot(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        if (material.hasFlag(FlagKey.INGOT)) {
            IngotFlag ingotFlag = material.getFlag(FlagKey.INGOT);
            StorageBlockFlag storageBlockFlag = material.getFlag(FlagKey.STORAGE_BLOCK);
            ResourceLocation inputId = new ResourceLocation(ingotFlag.getExistingNamespace(), ingotFlag.getIdPattern().formatted(material.getName()));
            ResourceLocation outputId = new ResourceLocation(storageBlockFlag.getExistingNamespace(), storageBlockFlag.getIdPattern().formatted(material.getName()));
            if (!outputId.getNamespace().equals(Metallurgica.ID)) {
                logRecipeSkip(outputId);
                return;
            }
            if (ingotFlag.isRequiresCompacting()) {
                compact9(provider, inputId, outputId);
            } else {
                craftCompact9(provider, inputId, outputId, MaterialHelper.getItem(material, FlagKey.INGOT), MaterialHelper.getBlock(material, FlagKey.STORAGE_BLOCK));
            }

        }
    }

    private static void logRecipeSkip(ResourceLocation outputId) {
        Metallurgica.LOGGER.info("Skipping storage recipe for {} as it is not in the metallurgica namespace and likely already has one", outputId);
    }

    private static void compact9(@NotNull Consumer<FinishedRecipe> provider, ResourceLocation inputId, ResourceLocation outputId) {
        ProcessingRecipeBuilder<CompactingRecipe> builder = new Builder<>(inputId.getNamespace(), CompactingRecipe::new, inputId.getPath(), outputId.getPath(), provider);
        for (int i = 0; i < 9; i++) {
            builder.require(BuiltInRegistries.ITEM.get(inputId));
        }
        builder.output(BuiltInRegistries.ITEM.get(outputId)).build();
    }

    private static void craftCompact9(@NotNull Consumer<FinishedRecipe> provider, ResourceLocation inputId, ResourceLocation outputId, ItemLike input, ItemLike output) {
        ShapedRecipeBuilder builder = new ShapedRecipeBuilder(RecipeCategory.MISC, output, 1);
        for (int i = 0; i < 3; i++) {
            builder.pattern("###");
        }
        builder.define('#', input)
               .unlockedBy("has_input", has(input))
               .save(provider, Metallurgica.asResource("runtime_generated/" + inputId.getNamespace() + "/crafting/" + inputId.getPath() + "_to_" + outputId.getPath()));
    }

    private static class Builder<T extends ProcessingRecipe<?>> extends ProcessingRecipeBuilder<T> {
        Consumer<FinishedRecipe> consumer;
        public Builder(String modid, ProcessingRecipeBuilder.ProcessingRecipeFactory<T> factory, String from, String to, Consumer<FinishedRecipe> consumer) {
            super(factory, Metallurgica.asResource("runtime_generated/" + modid + "/compacting/" + from + "_to_" + to));
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
