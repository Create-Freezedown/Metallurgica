package com.freezedown.metallurgica.compat.jei;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.compat.jei.category.ceramic_mixing.CeramicMixingCategory;
import com.freezedown.metallurgica.compat.jei.category.drill.DrillingCategory;
import com.freezedown.metallurgica.compat.jei.category.drill.DrillingRecipe;
import com.freezedown.metallurgica.compat.jei.category.electrolyzer.ElectrolysisCategory;
import com.freezedown.metallurgica.compat.jei.category.RecipeCategoryBuilder;
import com.freezedown.metallurgica.compat.jei.category.shaking.ShakingCategory;
import com.freezedown.metallurgica.content.machines.shaking_table.ShakingRecipe;
import com.freezedown.metallurgica.content.primitive.ceramic.ceramic_mixing_pot.CeramicMixingRecipe;
import com.freezedown.metallurgica.registry.*;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

import static com.freezedown.metallurgica.registry.MetallurgicaFluids.getVirtualFluidStacks;

@ParametersAreNonnullByDefault
@JeiPlugin
public class MetallurgicaJei implements IModPlugin {
    
    private static final ResourceLocation ID = Metallurgica.asResource("jei_plugin");
    
    protected final List<CreateRecipeCategory<?>> allCategories = new ArrayList<>();
    protected IIngredientManager ingredientManager;
    
    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }
    
    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        loadCategories(registration);
        registration.addRecipeCategories(allCategories.toArray(IRecipeCategory[]::new));
    }
    
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ingredientManager = registration.getIngredientManager();
        allCategories.forEach(c -> c.registerRecipes(registration));
    }
    
    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        allCategories.forEach(c -> c.registerCatalysts(registration));
    }
    
    
    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        List<FluidStack> fluidIngredients = new ArrayList<>(getVirtualFluidStacks());
        jeiRuntime.getIngredientManager().addIngredientsAtRuntime(ForgeTypes.FLUID_STACK,fluidIngredients);
    }
    
    private static <T extends Recipe<?>> RecipeCategoryBuilder<T> builder(Class<T> cls) {
        return new RecipeCategoryBuilder<>(Metallurgica.ID, cls);
    }
    
    private void loadCategories(IRecipeCategoryRegistration registration) {
        allCategories.clear();
        allCategories.add(
                builder(BasinRecipe.class)
                        .addTypedRecipes(MetallurgicaRecipeTypes.electrolysis)
                        .catalyst(MetallurgicaBlocks.electrolyzer::get)
                        .doubleItemIcon(MetallurgicaBlocks.electrolyzer.get(), MetallurgicaItems.washedAlumina.get())
                        .emptyBackground(177, 103)
                        .build("electrolysis", ElectrolysisCategory::new)
        );
        allCategories.add(
                builder(DrillingRecipe.class)
                        .addRecipes(() -> DrillingCategory.RECIPES)
                        .catalyst(MetallurgicaBlocks.drillActivator::get)
                        .catalyst(MetallurgicaBlocks.drillExpansion::get)
                        .doubleItemIcon(MetallurgicaOre.BAUXITE.MATERIAL.depositBlock().get(), MetallurgicaItems.loosenedBauxite.get())
                        .emptyBackground(177, 150)
                        .build("deposit_drilling", DrillingCategory::new)
        );
        allCategories.add(
                builder(ShakingRecipe.class)
                        .addTypedRecipes(MetallurgicaRecipeTypes.shaking)
                        .catalyst(MetallurgicaBlocks.shakingTable::get)
                        .itemIcon(MetallurgicaBlocks.shakingTable.get())
                        .emptyBackground(177, 70)
                        .build("shaking", ShakingCategory::new)
        );
        allCategories.add(
                builder(CeramicMixingRecipe.class)
                        .addTypedRecipes(MetallurgicaRecipeTypes.ceramic_mixing)
                        .catalyst(MetallurgicaBlocks.ceramicMixingPot::get)
                        .doubleItemIcon(MetallurgicaBlocks.ceramicMixingPot.get(), MetallurgicaItems.dirtyClayBall.get())
                        .emptyBackground(177, 103)
                        .build("ceramic_mixing", CeramicMixingCategory::new)
        );
    }
}
