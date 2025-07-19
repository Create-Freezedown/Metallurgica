package com.freezedown.metallurgica.foundation.mixin.jei;

import com.freezedown.metallurgica.Metallurgica;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.compat.jei.category.CrushingCategory;
import com.simibubi.create.content.kinetics.crusher.AbstractCrushingRecipe;
import mezz.jei.api.registration.IRecipeRegistration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.function.Supplier;

@Mixin(CrushingCategory.class)
public abstract class CrushingCategoryMixin extends CreateRecipeCategory<AbstractCrushingRecipe> {

    public CrushingCategoryMixin(Info<AbstractCrushingRecipe> info) {
        super(info);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Supplier<List<AbstractCrushingRecipe>> recipes = ((CreateRecipeCategoryAccessor<AbstractCrushingRecipe>)this).getRecipes();
        List<AbstractCrushingRecipe> filteredRecipes = recipes.get().stream().filter(cr -> !(cr.getId().getNamespace().equals(Metallurgica.ID) && cr.getId().getPath().startsWith("crushing/scrapping/"))).toList();
        registration.addRecipes(type, filteredRecipes);
    }
}
