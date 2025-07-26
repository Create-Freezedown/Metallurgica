package dev.metallurgists.metallurgica.foundation.data.runtime.recipe.handler;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.infastructure.material.MaterialHelper;
import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.ItemFlag;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.block.SheetmetalFlag;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.item.IngotFlag;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.item.NuggetFlag;
import com.simibubi.create.content.kinetics.mixer.CompactingRecipe;
import com.simibubi.create.content.kinetics.saw.CuttingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
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
            Item sheet = MaterialHelper.getItem(material, FlagKey.SHEET);
            Block sheetmetal = MaterialHelper.getBlock(material, FlagKey.SHEETMETAL);
            if (sheetmetalFlag.isRequiresCompacting()) {
                compact9(provider, sheet, sheetmetal, 9, material, "%s_sheetmetal_from_sheets");
                decompact9(provider, sheetmetal, sheet, 9, material, "%s_sheets_from_sheetmetal");
            } else {
                craftCompact9(provider, sheet, sheetmetal, 9, material, "%s_sheetmetal_from_sheets");
                craftDecompact9(provider, sheetmetal, sheet, 9, material, "%s_sheets_from_sheetmetal");
            }
        }
    }

    private static void processNugget(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        List<FlagKey<? extends ItemFlag>> toCheck = List.of(FlagKey.INGOT, FlagKey.GEM);
        if (material.hasFlag(FlagKey.NUGGET)) {
            NuggetFlag nuggetFlag = material.getFlag(FlagKey.NUGGET);
            Item nugget = MaterialHelper.getItem(material, FlagKey.NUGGET);
            ResourceLocation inputId = new ResourceLocation(nuggetFlag.getExistingNamespace(), nuggetFlag.getIdPattern().formatted(material.getName()));
            Item result = null;
            String resultFlag = "";
            for (FlagKey<? extends ItemFlag> flagKey : toCheck) {
                if (material.hasFlag(flagKey)) {
                    result = MaterialHelper.getItem(material, flagKey);
                    resultFlag = flagKey.toString();
                    break;
                }
            }
            if (result == null) {
                Metallurgica.LOGGER.warn("No ingot or gem flag found for material {}. Skipping nugget recipe generation.", material.getName());
                return;
            }
            if (!inputId.getNamespace().equals(Metallurgica.ID)) {
                logRecipeSkip(inputId);
                return;
            }
            if (nuggetFlag.isRequiresCompacting()) {
                compact9(provider, nugget, result, nuggetFlag.getAmountToCraft(), material, "%s_" + resultFlag + "_from_nuggets");
                decompact9(provider, result, nugget, nuggetFlag.getAmountToCraft(), material, "%s_nuggets_from_" + resultFlag);
            } else {
                craftCompact9(provider, nugget, result, nuggetFlag.getAmountToCraft(), material, "%s_" + resultFlag + "_from_nuggets");
                craftDecompact9(provider, result, nugget, nuggetFlag.getAmountToCraft(), material, "%s_nuggets_from_" + resultFlag);
            }
        }
    }

    private static void processIngot(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        if (material.hasFlag(FlagKey.INGOT)) {
            IngotFlag ingotFlag = material.getFlag(FlagKey.INGOT);
            ResourceLocation ingotId = new ResourceLocation(ingotFlag.getExistingNamespace(), ingotFlag.getIdPattern().formatted(material.getName()));
            Item ingot = MaterialHelper.getItem(material, FlagKey.INGOT);
            if (material.hasFlag(FlagKey.STORAGE_BLOCK)) {
                Block block = MaterialHelper.getBlock(material, FlagKey.STORAGE_BLOCK);
                if (!ingotId.getNamespace().equals(Metallurgica.ID)) {
                    logRecipeSkip(ingotId);
                    return;
                }
                if (ingotFlag.isRequiresCompacting()) {
                    compact9(provider, ingot, block, 9, material, "%s_block_from_ingots");
                    decompact9(provider, block, ingot, 9, material, "%s_ingots_from_block");
                } else {
                    craftCompact9(provider, ingot, block, 9, material, "%s_block_from_ingots");
                    craftDecompact9(provider, block, ingot, 9, material, "%s_ingots_from_block");
                }
            }
        }
    }

    private static void logRecipeSkip(ResourceLocation outputId) {
        Metallurgica.LOGGER.info("Skipping storage recipe for {} as it is not in the metallurgica namespace and likely already has one", outputId);
    }

    private static void compact9(@NotNull Consumer<FinishedRecipe> provider, ItemLike input, ItemLike output, int amountIn, Material material, String recipeId) {
        ProcessingRecipeBuilder<CompactingRecipe> builder = new Builder<>(material.getNamespace(), CompactingRecipe::new, recipeId.formatted(material.getName()), provider);
        for (int i = 0; i < amountIn; i++) {
            builder.require(input);
        }
        builder.output(output).build();
    }

    private static void craftCompact9(@NotNull Consumer<FinishedRecipe> provider, ItemLike input, ItemLike output, int amountIn, Material material, String recipeId) {
        if (amountIn == 9) {
            ShapedRecipeBuilder builder = new ShapedRecipeBuilder(RecipeCategory.MISC, output, 1);
            for (int i = 0; i < 3; i++) {
                builder.pattern("###");
            }
            builder.define('#', input)
                    .unlockedBy("has_input", InventoryChangeTrigger.TriggerInstance.hasItems(input))
                    .save(provider, Metallurgica.asResource("runtime_generated/" + material.getNamespace() + "/" + recipeId.formatted(material.getName())));
        } else {
            ShapelessRecipeBuilder builder = new ShapelessRecipeBuilder(RecipeCategory.MISC, output, amountIn);
            for (int i = 0; i < amountIn; i++) {
                builder.requires(input);
            }
            builder.unlockedBy("has_input", InventoryChangeTrigger.TriggerInstance.hasItems(input))
                   .save(provider, Metallurgica.asResource("runtime_generated/" + material.getNamespace() + "/" + recipeId.formatted(material.getName())));
        }
    }

    private static void decompact9(@NotNull Consumer<FinishedRecipe> provider, ItemLike input, ItemLike output, int amountOut, Material material, String recipeId) {
        ProcessingRecipeBuilder<CuttingRecipe> builder = new Builder<>(material.getNamespace(), CuttingRecipe::new, recipeId.formatted(material.getName()), provider);
        builder.require(input);
        builder.output(output, amountOut).build();
    }

    private static void craftDecompact9(@NotNull Consumer<FinishedRecipe> provider, ItemLike input, ItemLike output, int amountOut, Material material, String recipeId) {
        ShapelessRecipeBuilder builder = new ShapelessRecipeBuilder(RecipeCategory.MISC, output, amountOut);
        builder.requires(input)
               .unlockedBy("has_input", InventoryChangeTrigger.TriggerInstance.hasItems(input))
               .save(provider, Metallurgica.asResource("runtime_generated/" + material.getNamespace() + "/" + recipeId.formatted(material.getName())));
    }

    private static class Builder<T extends ProcessingRecipe<?>> extends ProcessingRecipeBuilder<T> {
        Consumer<FinishedRecipe> consumer;
        public Builder(String modid, ProcessingRecipeBuilder.ProcessingRecipeFactory<T> factory, String id, Consumer<FinishedRecipe> consumer) {
            super(factory, Metallurgica.asResource("runtime_generated/" + modid + "/" + id));
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
