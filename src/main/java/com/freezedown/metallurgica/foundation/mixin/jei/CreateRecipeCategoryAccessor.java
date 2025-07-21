package com.freezedown.metallurgica.foundation.mixin.jei;

import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.kinetics.crusher.CrushingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.function.Supplier;

@Mixin(value = CreateRecipeCategory.class, remap = false)
public interface CreateRecipeCategoryAccessor<T extends Recipe<?>> {

    @Accessor
    Supplier<List<T>> getRecipes();
}
