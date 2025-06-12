package com.freezedown.metallurgica.foundation.data.runtime.recipe.handler;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey;
import com.freezedown.metallurgica.foundation.item.registry.flags.block.SheetmetalFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.item.IngotFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.item.NuggetFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.block.StorageBlockFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.item.SheetFlag;
import com.simibubi.create.content.kinetics.mixer.CompactingRecipe;
import com.simibubi.create.content.kinetics.saw.CuttingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
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
        processSheet(provider, material);
    }

    private static void processSheet(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        if (material.hasFlag(FlagKey.SHEETMETAL)) {
            SheetmetalFlag sheetmetalFlag = material.getFlag(FlagKey.SHEETMETAL);
            SheetFlag sheetFlag = material.getFlag(FlagKey.SHEET);
            ResourceLocation sheetId = new ResourceLocation(sheetFlag.getExistingNamespace(), sheetFlag.getIdPattern().formatted(material.getName()));
            if (material.noRegister(FlagKey.SHEET)) {
                sheetId = new ResourceLocation(sheetFlag.getExistingNamespace(), sheetFlag.getIdPattern().formatted(material.getName()));
            }
            ResourceLocation sheetmetalId = new ResourceLocation(sheetmetalFlag.getExistingNamespace(), sheetmetalFlag.getIdPattern().formatted(material.getName()));
            if (sheetmetalFlag.isRequiresCompacting()) {
                compact9(provider, sheetId, sheetmetalId);
                decompact9(provider, sheetmetalId, sheetId);
            } else {
                craftCompact9(provider, sheetId, sheetmetalId, BuiltInRegistries.ITEM.get(sheetId), BuiltInRegistries.ITEM.get(sheetmetalId));
                craftDecompact9(provider, sheetmetalId, sheetId, BuiltInRegistries.ITEM.get(sheetmetalId), BuiltInRegistries.ITEM.get(sheetId));
            }
        }
    }

    private static void processNugget(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        if (material.hasFlag(FlagKey.NUGGET)) {
            NuggetFlag nuggetFlag = material.getFlag(FlagKey.NUGGET);
            IngotFlag ingotFlag = material.getFlag(FlagKey.INGOT);
            ResourceLocation inputId = new ResourceLocation(nuggetFlag.getExistingNamespace(), nuggetFlag.getIdPattern().formatted(material.getName()));
            ResourceLocation outputId = new ResourceLocation(ingotFlag.getExistingNamespace(), ingotFlag.getIdPattern().formatted(material.getName()));
            if (material.noRegister(FlagKey.INGOT)) {
                outputId = new ResourceLocation(ingotFlag.getExistingNamespace(), ingotFlag.getIdPattern().formatted(material.getName()));
            }
            if (!inputId.getNamespace().equals(Metallurgica.ID)) {
                logRecipeSkip(inputId);
                return;
            }
            if (nuggetFlag.isRequiresCompacting()) {
                compact9(provider, inputId, outputId);
                decompact9(provider, outputId, inputId);
            } else {
                craftCompact9(provider, inputId, outputId, BuiltInRegistries.ITEM.get(inputId), BuiltInRegistries.ITEM.get(outputId));
                craftDecompact9(provider, outputId, inputId, BuiltInRegistries.ITEM.get(outputId), BuiltInRegistries.ITEM.get(inputId));
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
                decompact9(provider, outputId, inputId);
            } else {
                craftCompact9(provider, inputId, outputId, BuiltInRegistries.ITEM.get(inputId), BuiltInRegistries.ITEM.get(outputId));
                craftDecompact9(provider, outputId, inputId, BuiltInRegistries.ITEM.get(outputId), BuiltInRegistries.ITEM.get(inputId));
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
               .save(provider, Metallurgica.asResource("runtime_generated/" + inputId.getNamespace() + "/" + outputId.getPath() + "_from_" + inputId.getPath()));
    }

    private static void decompact9(@NotNull Consumer<FinishedRecipe> provider, ResourceLocation inputId, ResourceLocation outputId) {
        ProcessingRecipeBuilder<CuttingRecipe> builder = new Builder<>(inputId.getNamespace(), CuttingRecipe::new, outputId.getPath(), inputId.getPath(), provider);
        builder.require(BuiltInRegistries.ITEM.get(inputId));
        builder.output(BuiltInRegistries.ITEM.get(outputId), 9).build();
    }

    private static void craftDecompact9(@NotNull Consumer<FinishedRecipe> provider, ResourceLocation inputId, ResourceLocation outputId, ItemLike input, ItemLike output) {
        ShapelessRecipeBuilder builder = new ShapelessRecipeBuilder(RecipeCategory.MISC, output, 9);
        builder.requires(BuiltInRegistries.ITEM.get(inputId))
               .unlockedBy("has_input", has(input))
               .save(provider, Metallurgica.asResource("runtime_generated/" + inputId.getNamespace() + "/" + outputId.getPath() + "_from_" + inputId.getPath()));
    }

    private static class Builder<T extends ProcessingRecipe<?>> extends ProcessingRecipeBuilder<T> {
        Consumer<FinishedRecipe> consumer;
        public Builder(String modid, ProcessingRecipeBuilder.ProcessingRecipeFactory<T> factory, String from, String to, Consumer<FinishedRecipe> consumer) {
            super(factory, Metallurgica.asResource("runtime_generated/" + modid + "/" + to + "_from_" + from));
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
