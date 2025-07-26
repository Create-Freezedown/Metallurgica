package dev.metallurgists.metallurgica.foundation.util.recipe.helper;

import dev.metallurgists.metallurgica.content.items.reaction.FluidReactionRecipe;
import dev.metallurgists.metallurgica.registry.misc.MetallurgicaSpecialRecipes;
import net.createmod.catnip.data.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.util.AbortableIterationConsumer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class PhysicalRecipeHelper {

    public static AbortableIterationConsumer.Continuation matchFluidReactionRecipe(final ItemEntity entity) {
        /*if(entity.hasPickUpDelay()) {
            return;
        }*/
        if(entity.getItem().isEmpty()) {
            return AbortableIterationConsumer.Continuation.ABORT;
        }
        final BlockPos pos = new BlockPos(entity.blockPosition());
        final FluidState fluidState = entity.level().getFluidState(pos);
        if(fluidState.isEmpty()) {
            return AbortableIterationConsumer.Continuation.ABORT;
        }
        final Container container = new SimpleContainer(entity.getItem());
        final List<FluidReactionRecipe> recipes = entity.level().getRecipeManager().getAllRecipesFor(MetallurgicaSpecialRecipes.FLUID_REACTION.get());
        for(FluidReactionRecipe recipe : recipes) {
            if(recipe.matches(container, fluidState)) {
                ItemStack result = recipe.assemble(container, pos, entity.level());
                Block.popResource(entity.level(), pos, result);
                entity.setItem(container.getItem(0));
                return AbortableIterationConsumer.Continuation.ABORT;
            }
        }
        return AbortableIterationConsumer.Continuation.ABORT;
    }

    //For handling fluid reaction recipes within containers or other items
    public static Pair<FluidReactionRecipe, FluidStack> matchFluidReactionRecipe(Level level, ItemStack stack, List<FluidStack> fluidStacks) {
        if(stack.isEmpty()) {
            return null;
        }
        final Container container = new SimpleContainer(stack);
        final List<FluidReactionRecipe> recipes = level.getRecipeManager().getAllRecipesFor(MetallurgicaSpecialRecipes.FLUID_REACTION.get());
        for(FluidReactionRecipe recipe : recipes) {
            for(FluidStack fluidStack : fluidStacks) {
                if(recipe.matches(container, fluidStack)) {
                    ItemStack result = recipe.assemble(container, level.registryAccess());
                    stack.setCount(result.getCount());
                    stack.setTag(result.getTag());
                    return Pair.of(recipe, fluidStack);
                }
            }
        }
        return null;
    }
}
