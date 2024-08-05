package com.freezedown.metallurgica.compat.jei.category.drill;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.registry.MetallurgicaRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;

public class DrillingRecipe extends ProcessingRecipe<RecipeWrapper> {
    static float minEfficiency = 0;
    
    public static DrillingRecipe create(ItemStack from, ItemStack to, float efficiency) {
        minEfficiency = efficiency;
        ResourceLocation recipeId = Metallurgica.asResource("deposit_drilling_" + ForgeRegistries.ITEMS.getKey(to.getItem()).toString().replace(":", "/"));
        return new ProcessingRecipeBuilder<>(DrillingRecipe::new, recipeId)
                .withItemIngredients(Ingredient.of(from))
                .withSingleItemOutput(to)
                .build();
    }
    
    public DrillingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(MetallurgicaRecipeTypes.deposit_drilling, params);
    }
    
    public float getMinEfficiency() {
        return minEfficiency;
    }
    
    @Override
    protected int getMaxInputCount() {
        return 1;
    }
    
    @Override
    protected int getMaxOutputCount() {
        return 1;
    }
    
    @Override
    public boolean matches(RecipeWrapper recipeWrapper, Level level) {
        return false;
    }
}
