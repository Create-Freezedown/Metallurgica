package com.freezedown.metallurgica.foundation.data.runtime.recipe.handler;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.material.recycling.Scrappable;
import com.freezedown.metallurgica.foundation.material.registry.Material;
import com.freezedown.metallurgica.foundation.material.registry.flags.FlagKey;
import com.freezedown.metallurgica.foundation.material.registry.flags.base.BlockFlag;
import com.freezedown.metallurgica.foundation.material.registry.flags.base.ItemFlag;
import com.simibubi.create.content.kinetics.crusher.CrushingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.createmod.catnip.data.Pair;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

public class ScrappingHandler {

    private ScrappingHandler() {}

    public static void run(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        processCrushing(provider, material);
    }

    private static void processCrushing(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        for (FlagKey<?> flagKey : material.getFlags().getFlagKeys()) {
            if (material.getFlag(flagKey) instanceof Scrappable scrappable) {
                if (material.getFlag(flagKey) instanceof ItemFlag itemFlag) {
                    var inputId = itemFlag.getExistingId(material);
                    crush(provider, material, inputId, scrappable);
                }
                if (material.getFlag(flagKey) instanceof BlockFlag blockFlag) {
                    var inputId = blockFlag.getExistingId(material);
                    crush(provider, material, inputId, scrappable);
                }
            }
        }
    }

    private static void crush(@NotNull Consumer<FinishedRecipe> provider, Material material, ResourceLocation inputId, Scrappable scrappable) {
        ProcessingRecipeBuilder<CrushingRecipe> builder = new Builder<>(material.getNamespace(), CrushingRecipe::new, inputId.getPath(), provider);
        builder.require(BuiltInRegistries.ITEM.get(inputId));
        int outputs = 0;
        for (Map.Entry<Material, Integer> scrapsInto : scrappable.scrapsInto(material).entrySet()) {
            for (Map.Entry<Material, Pair<Integer, Float>> discardChance : scrappable.discardChance(material).entrySet()) {
                if (scrapsInto.getKey() == discardChance.getKey()) {
                    float chance = 1.0f - discardChance.getValue().getSecond();
                    int discardedAmount = discardChance.getValue().getFirst();
                    int mainAmount = scrapsInto.getValue() - discardedAmount;
                    if (!scrapsInto.getKey().hasFlag(FlagKey.DUST)) continue;
                    var dustId = scrapsInto.getKey().getFlag(FlagKey.DUST).getExistingId(scrapsInto.getKey());
                    builder.output(BuiltInRegistries.ITEM.get(dustId), mainAmount);
                    builder.output(chance, BuiltInRegistries.ITEM.get(dustId), discardedAmount);
                    outputs++;
                }
            }
        }
        for (Map.Entry<ItemLike, Pair<Integer, Float>> extraOutput : scrappable.extraItems(material).entrySet()) {
            float chance = extraOutput.getValue().getSecond();
            int amount = extraOutput.getValue().getFirst();
            var output = extraOutput.getKey();
            builder.output(chance, output, amount);
            outputs++;
        }
        if (outputs == 0) return;
        builder.averageProcessingDuration();
        builder.build();
    }


    private static class Builder<T extends ProcessingRecipe<?>> extends ProcessingRecipeBuilder<T> {
        Consumer<FinishedRecipe> consumer;
        public Builder(String modid, ProcessingRecipeBuilder.ProcessingRecipeFactory<T> factory, String from, Consumer<FinishedRecipe> consumer) {
            super(factory, Metallurgica.asResource("recycling/" + modid + "/" + from));
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
