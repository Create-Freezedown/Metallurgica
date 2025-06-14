package com.freezedown.metallurgica.foundation.data.runtime.recipe.handler;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.material.registry.Material;
import com.freezedown.metallurgica.foundation.material.registry.flags.FlagKey;
import com.freezedown.metallurgica.foundation.material.registry.flags.block.CasingFlag;
import com.freezedown.metallurgica.foundation.material.registry.flags.item.IngotFlag;
import com.freezedown.metallurgica.foundation.material.registry.flags.item.SheetFlag;
import com.freezedown.metallurgica.foundation.material.MaterialHelper;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.deployer.ItemApplicationRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.function.Consumer;

public class ItemApplicationHandler {

    private ItemApplicationHandler() {}

    public static void run(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        processApplication(provider, material);
    }

    private static void processApplication(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        if (material.hasFlag(FlagKey.CASING)) {
            CasingFlag casingFlag = material.getFlag(FlagKey.CASING);
            ResourceLocation casingId = casingFlag.getExistingId(material, FlagKey.CASING);
            IngotFlag ingotFlag = material.getFlag(FlagKey.INGOT);
            ResourceLocation ingotId = ingotFlag.getExistingId(material, FlagKey.INGOT);
            SheetFlag sheetFlag = null;
            ResourceLocation sheetId = null;
            if (casingFlag.isUseSheet()) {
                sheetFlag = material.getFlag(FlagKey.SHEET);
                sheetId = sheetFlag.getExistingId(material, FlagKey.SHEET);
            }
            ResourceLocation usedId = casingFlag.isUseSheet() ? sheetId : ingotId;
            Item usedItem = BuiltInRegistries.ITEM.get(usedId);
            for (TagKey<Item> appliesOn : casingFlag.getToApplyOn()) {
                String appliesOnPath = appliesOn.location().getPath().replace("/", "_");
                ProcessingRecipeBuilder<ItemApplicationRecipe> builder = new Builder<>(casingId.getNamespace(), (params) -> new ItemApplicationRecipe(AllRecipeTypes.ITEM_APPLICATION, params), casingId.getPath(), appliesOnPath, provider);
                builder.require(appliesOn);
                builder.require(usedItem);
                builder.output(MaterialHelper.getBlock(material, FlagKey.CASING).asItem());
                builder.build();
            }
        }
    }

    private static void logRecipeSkip(ResourceLocation rubbleId) {
        Metallurgica.LOGGER.info("Skipping item application recipe for {} as it is not in the metallurgica namespace and likely already has one", rubbleId);
    }


    private static class Builder<T extends ProcessingRecipe<?>> extends ProcessingRecipeBuilder<T> {
        Consumer<FinishedRecipe> consumer;
        public Builder(String modid, ProcessingRecipeBuilder.ProcessingRecipeFactory<T> factory, String casing, String tagPath, Consumer<FinishedRecipe> consumer) {
            super(factory, Metallurgica.asResource("runtime_generated/" + modid + "/" + casing + "_from_" + tagPath));
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
