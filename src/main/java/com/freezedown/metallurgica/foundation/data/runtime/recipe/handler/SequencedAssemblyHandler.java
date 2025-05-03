package com.freezedown.metallurgica.foundation.data.runtime.recipe.handler;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey;
import com.freezedown.metallurgica.foundation.item.registry.flags.IngotFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.SemiPressedSheetFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.SheetFlag;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class SequencedAssemblyHandler {

    private SequencedAssemblyHandler() {}

    public static void run(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        processAssembly(provider, material);
    }

    private static void processAssembly(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        if (material.hasFlag(FlagKey.SEMI_PRESSED_SHEET)) {
            SemiPressedSheetFlag semiPressedSheetFlag = material.getFlag(FlagKey.SEMI_PRESSED_SHEET);
            IngotFlag ingotFlag = material.getFlag(FlagKey.INGOT);
            SheetFlag sheetFlag = material.getFlag(FlagKey.SHEET);
            ResourceLocation inputId = new ResourceLocation(ingotFlag.getExistingNamespace(), ingotFlag.getIdPattern().formatted(material.getName()));
            ResourceLocation outputId = new ResourceLocation(sheetFlag.getExistingNamespace(), sheetFlag.getIdPattern().formatted(material.getName()));
            ResourceLocation transitionalId = new ResourceLocation(semiPressedSheetFlag.getExistingNamespace(), semiPressedSheetFlag.getIdPattern().formatted(material.getName()));
            SequencedAssemblyRecipeBuilder builder = new SequencedAssemblyRecipeBuilder(Metallurgica.asResource("runtime_generated/" + inputId.getNamespace() + "/" + inputId.getPath() + "_to_" + outputId.getPath()));
            builder.transitionTo(BuiltInRegistries.ITEM.get(transitionalId));
            builder.require(BuiltInRegistries.ITEM.get(inputId));
            for (int i = 0; i < sheetFlag.getPressTimes(); i++) {
                builder.addStep(PressingRecipe::new, rb -> rb);
            }
            builder.loops(1);
            builder.addOutput(BuiltInRegistries.ITEM.get(outputId), 1);
            builder.build(provider);
        }
    }
}
