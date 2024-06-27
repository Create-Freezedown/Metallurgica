package com.freezedown.metallurgica.compat.jei;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.compat.jei.category.electrolyzer.ElectrolysisCategory;
import com.freezedown.metallurgica.compat.jei.category.RecipeCategoryBuilder;
import com.freezedown.metallurgica.registry.MetallurgicaBlocks;
import com.freezedown.metallurgica.registry.MetallurgicaFluids;
import com.freezedown.metallurgica.registry.MetallurgicaItems;
import com.freezedown.metallurgica.registry.MetallurgicaRecipeTypes;
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
        List<FluidStack> fluidIngredients = new ArrayList<>(MetallurgicaFluids.getVirtualFluidStacks());
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
    }
}
