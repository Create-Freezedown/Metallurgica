package com.freezedown.metallurgica.infastructure.material.registry.flags.base;

import com.freezedown.metallurgica.infastructure.material.Material;
import net.minecraft.data.recipes.FinishedRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface IRecipeHandler {

    void run(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material);
}
