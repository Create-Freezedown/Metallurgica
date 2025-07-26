package dev.metallurgists.metallurgica.content.machines.shaking_table;

import dev.metallurgists.metallurgica.compat.jei.category.shaking.AssemblyShaking;
import dev.metallurgists.metallurgica.foundation.util.MetalLang;
import dev.metallurgists.metallurgica.registry.MetallurgicaBlocks;
import dev.metallurgists.metallurgica.registry.MetallurgicaRecipeTypes;
import com.simibubi.create.compat.jei.category.sequencedAssembly.SequencedAssemblySubCategory;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.sequenced.IAssemblyRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.wrapper.RecipeWrapper;

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
        MutableComponent result = MetalLang.translateDirect("recipe.assembly.shaking");
        if (this.fluidIngredients.isEmpty()) {
            return result;
        } else {
            result.append(" ").append(MetalLang.translateDirect("recipe.assembly.with")).append(" ");
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
            return AssemblyShaking::new;
        };
    }
}
