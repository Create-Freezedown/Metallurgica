package com.freezedown.metallurgica.content.primitive.ceramic.ceramic_mixing_pot;

import com.freezedown.metallurgica.registry.MetallurgicaRecipeTypes;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.recipe.DummyCraftingContainer;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CeramicMixingRecipe extends ProcessingRecipe<SmartInventory> {
    
    public static boolean match(CeramicMixingPotBlockEntity basin, Recipe<?> recipe) {
        FilteringBehaviour filter = basin.getFilter();
        if (filter == null)
            return false;
        
        boolean filterTest = filter.test(recipe.getResultItem(basin.getLevel().registryAccess()));
        if (recipe instanceof CeramicMixingRecipe) {
            CeramicMixingRecipe basinRecipe = (CeramicMixingRecipe) recipe;
            if (basinRecipe.getRollableResults()
                    .isEmpty()
                    && !basinRecipe.getFluidResults()
                    .isEmpty())
                filterTest = filter.test(basinRecipe.getFluidResults()
                        .get(0));
        }
        
        if (!filterTest)
            return false;
        
        return apply(basin, recipe, true);
    }
    
    public static boolean apply(CeramicMixingPotBlockEntity basin, Recipe<?> recipe) {
        return apply(basin, recipe, false);
    }
    
    private static boolean apply(CeramicMixingPotBlockEntity basin, Recipe<?> recipe, boolean test) {
        boolean isBasinRecipe = recipe instanceof CeramicMixingRecipe;
        IItemHandler availableItems = basin.getCapability(ForgeCapabilities.ITEM_HANDLER)
                .orElse(null);
        IFluidHandler availableFluids = basin.getCapability(ForgeCapabilities.FLUID_HANDLER)
                .orElse(null);
        
        if (availableItems == null || availableFluids == null)
            return false;
        
        BlazeBurnerBlock.HeatLevel heat = CeramicMixingPotBlockEntity.getHeatLevelOf(basin.getLevel()
                .getBlockState(basin.getBlockPos()
                        .below(1)));
        if (isBasinRecipe && !((CeramicMixingRecipe) recipe).getRequiredHeat()
                .testBlazeBurner(heat))
            return false;
        
        List<ItemStack> recipeOutputItems = new ArrayList<>();
        List<FluidStack> recipeOutputFluids = new ArrayList<>();
        
        List<Ingredient> ingredients = new LinkedList<>(recipe.getIngredients());
        List<FluidIngredient> fluidIngredients =
                isBasinRecipe ? ((CeramicMixingRecipe) recipe).getFluidIngredients() : Collections.emptyList();
        
        for (boolean simulate : Iterate.trueAndFalse) {
            
            if (!simulate && test)
                return true;
            
            int[] extractedItemsFromSlot = new int[availableItems.getSlots()];
            int[] extractedFluidsFromTank = new int[availableFluids.getTanks()];
            
            Ingredients: for (int i = 0; i < ingredients.size(); i++) {
                Ingredient ingredient = ingredients.get(i);
                
                for (int slot = 0; slot < availableItems.getSlots(); slot++) {
                    if (simulate && availableItems.getStackInSlot(slot)
                            .getCount() <= extractedItemsFromSlot[slot])
                        continue;
                    ItemStack extracted = availableItems.extractItem(slot, 1, true);
                    if (!ingredient.test(extracted))
                        continue;
                    if (!simulate)
                        availableItems.extractItem(slot, 1, false);
                    extractedItemsFromSlot[slot]++;
                    continue Ingredients;
                }
                
                // something wasn't found
                return false;
            }
            
            boolean fluidsAffected = false;
            FluidIngredients: for (int i = 0; i < fluidIngredients.size(); i++) {
                FluidIngredient fluidIngredient = fluidIngredients.get(i);
                int amountRequired = fluidIngredient.getRequiredAmount();
                
                for (int tank = 0; tank < availableFluids.getTanks(); tank++) {
                    FluidStack fluidStack = availableFluids.getFluidInTank(tank);
                    if (simulate && fluidStack.getAmount() <= extractedFluidsFromTank[tank])
                        continue;
                    if (!fluidIngredient.test(fluidStack))
                        continue;
                    int drainedAmount = Math.min(amountRequired, fluidStack.getAmount());
                    if (!simulate) {
                        fluidStack.shrink(drainedAmount);
                        fluidsAffected = true;
                    }
                    amountRequired -= drainedAmount;
                    if (amountRequired != 0)
                        continue;
                    extractedFluidsFromTank[tank] += drainedAmount;
                    continue FluidIngredients;
                }
                
                // something wasn't found
                return false;
            }
            
            if (fluidsAffected) {
                basin.getBehaviour(SmartFluidTankBehaviour.INPUT)
                        .forEach(SmartFluidTankBehaviour.TankSegment::onFluidStackChanged);
                basin.getBehaviour(SmartFluidTankBehaviour.OUTPUT)
                        .forEach(SmartFluidTankBehaviour.TankSegment::onFluidStackChanged);
            }
            
            if (simulate) {
                if (recipe instanceof CeramicMixingRecipe basinRecipe) {
                    recipeOutputItems.addAll(basinRecipe.rollResults());
                    recipeOutputFluids.addAll(basinRecipe.getFluidResults());
                    recipeOutputItems.addAll(basinRecipe.getRemainingItems(basin.getInputInventory()));
                } else {
                    recipeOutputItems.add(recipe.getResultItem(basin.getLevel().registryAccess()));
                    
                    if (recipe instanceof CraftingRecipe craftingRecipe) {
                        recipeOutputItems.addAll(craftingRecipe.getRemainingItems(new DummyCraftingContainer(availableItems, extractedItemsFromSlot)));
                    }
                }
            }
            
            if (!basin.acceptOutputs(recipeOutputItems, recipeOutputFluids, simulate))
                return false;
        }
        
        return true;
    }
    
    public CeramicMixingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(MetallurgicaRecipeTypes.ceramic_mixing, params);
    }
    
    @Override
    protected int getMaxInputCount() {
        return 3;
    }
    
    @Override
    protected int getMaxOutputCount() {
        return 3;
    }
    
    @Override
    protected int getMaxFluidInputCount() {
        return 2;
    }
    
    @Override
    protected int getMaxFluidOutputCount() {
        return 2;
    }
    
    @Override
    protected boolean canRequireHeat() {
        return true;
    }
    
    @Override
    protected boolean canSpecifyDuration() {
        return true;
    }
    
    @Override
    public boolean matches(SmartInventory inv, @Nonnull Level worldIn) {
        return false;
    }
}
