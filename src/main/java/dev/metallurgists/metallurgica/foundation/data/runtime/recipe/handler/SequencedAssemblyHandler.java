package dev.metallurgists.metallurgica.foundation.data.runtime.recipe.handler;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.infastructure.material.MaterialHelper;
import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.item.SheetFlag;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class SequencedAssemblyHandler {

    private SequencedAssemblyHandler() {}

    public static void run(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        processAssembly(provider, material);
    }

    private static void processAssembly(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        if (material.hasFlag(FlagKey.SEMI_PRESSED_SHEET)) {
            if (material.noRegister(FlagKey.SHEET)) {
                Metallurgica.LOGGER.info("Skipping pressing recipe for {} as it is not in the metallurgica namespace and likely already has one", material.getName() + "_sheet");
                return;
            }
            SheetFlag sheetFlag = material.getFlag(FlagKey.SHEET);
            Item ingot = MaterialHelper.getItem(material, FlagKey.INGOT);
            Item sheet = MaterialHelper.getItem(material, FlagKey.SHEET);
            Item transitional = MaterialHelper.getItem(material, FlagKey.SEMI_PRESSED_SHEET);
            SequencedAssemblyRecipeBuilder builder = new SequencedAssemblyRecipeBuilder(Metallurgica.asResource("sequenced_assembly/runtime_generated/" + material.getNamespace() + "/" + material.getName() + "_sheet"));
            builder.transitionTo(transitional);
            builder.require(ingot);
            for (int i = 0; i < sheetFlag.getPressTimes(); i++) {
                builder.addStep(PressingRecipe::new, rb -> rb);
            }
            builder.loops(1);
            builder.addOutput(sheet, 1);
            builder.build(provider);
        }
    }
}
