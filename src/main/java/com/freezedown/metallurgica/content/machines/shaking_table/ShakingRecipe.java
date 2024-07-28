package com.freezedown.metallurgica.content.machines.shaking_table;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.registry.MetallurgicaBlocks;
import com.freezedown.metallurgica.registry.MetallurgicaRecipeTypes;
import com.negodya1.vintageimprovements.VintageBlocks;
import com.negodya1.vintageimprovements.compat.jei.category.assemblies.AssemblyCentrifugation;
import com.negodya1.vintageimprovements.content.kinetics.vibration.VibratingTableBlockEntity;
import com.negodya1.vintageimprovements.foundation.utility.VintageLang;
import com.simibubi.create.compat.jei.category.sequencedAssembly.SequencedAssemblySubCategory;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.sequenced.IAssemblyRecipe;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class ShakingRecipe extends ProcessingRecipe<RecipeWrapper> implements IAssemblyRecipe {
    
    public ShakingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(MetallurgicaRecipeTypes.shaking, params);
    }
    
    public boolean matches(RecipeWrapper inv, Level worldIn) {
        return inv.isEmpty() ? false : this.ingredients.get(0).test(inv.getItem(0));
    }
    public boolean matches(RecipeWrapper inv, FluidTank tank, Level worldIn) {
        if (!this.getFluidIngredients().isEmpty()) {
            return this.matches(inv, worldIn) && this.getFluidIngredients().get(0).test(tank.getFluid());
        }
        return this.matches(inv, worldIn);
    }
    
    protected int getMaxFluidInputCount() {
        return 1;
    }
    
    protected int getMaxInputCount() {
        return 1;
    }
    
    protected int getMaxOutputCount() {
        return 6;
    }
    
    protected boolean canSpecifyDuration() {
        return true;
    }
    
    @Override
    public Component getDescriptionForAssembly() {
        MutableComponent result = VintageLang.translateDirect("recipe.assembly.shaking");
        if (this.fluidIngredients.isEmpty()) {
            return result;
        } else {
            result.append(" ").append(VintageLang.translateDirect("recipe.assembly.with")).append(" ");
            if (!this.fluidIngredients.isEmpty() && !this.fluidIngredients.get(0).getMatchingFluidStacks().isEmpty()) {
                result.append(this.fluidIngredients.get(0).getMatchingFluidStacks().get(0).getDisplayName());
            }
        }
        return result;
    }
    
    @Override
    public void addRequiredMachines(Set<ItemLike> list) {
        list.add(MetallurgicaBlocks.shakingTable.get());
    }
    
    @Override
    public void addAssemblyIngredients(List<Ingredient> list) {
    
    }
    
    @Override
    public Supplier<Supplier<SequencedAssemblySubCategory>> getJEISubCategory() {
        return () -> {
            return AssemblyCentrifugation::new;
        };
    }
}
