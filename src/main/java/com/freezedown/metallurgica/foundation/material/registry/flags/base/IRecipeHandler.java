package com.freezedown.metallurgica.foundation.material.registry.flags.base;

import com.freezedown.metallurgica.foundation.material.registry.Material;
import net.minecraft.data.recipes.FinishedRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface IRecipeHandler {

    void run(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material);
}
