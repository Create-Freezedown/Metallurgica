package com.freezedown.metallurgica.foundation.item.registry.flags.base;

import com.freezedown.metallurgica.foundation.item.registry.Material;
import net.minecraft.data.recipes.FinishedRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface IRecipeHandler {

    void run(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material);
}
