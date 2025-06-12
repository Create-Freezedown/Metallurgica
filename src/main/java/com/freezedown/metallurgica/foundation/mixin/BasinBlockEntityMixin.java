package com.freezedown.metallurgica.foundation.mixin;

import com.freezedown.metallurgica.content.items.reaction.FluidReactionRecipe;
import com.freezedown.metallurgica.foundation.util.recipe.helper.PhysicalRecipeHelper;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.item.SmartInventory;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.data.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(BasinBlockEntity.class)
public abstract class BasinBlockEntityMixin extends SmartBlockEntity {

    public BasinBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Shadow public abstract SmartInventory getInputInventory();

    @Shadow public abstract SmartInventory getOutputInventory();

    @Shadow public abstract Couple<SmartFluidTankBehaviour> getTanks();

    @Inject(method = "tick()V", at = @At("HEAD"), remap = false)
    private void onTick(CallbackInfo ci) {
        Couple<SmartFluidTankBehaviour> tanks = getTanks();
        SmartInventory inputInv = getInputInventory();
        SmartInventory outputInv = getOutputInventory();
        metallurgica$handleBasinReaction(inputInv, tanks);
        metallurgica$handleBasinReaction(outputInv, tanks);
    }

    @Unique
    private void metallurgica$handleBasinReaction(SmartInventory inv, Couple<SmartFluidTankBehaviour> tanks) {
        for (int i = 0; i < inv.getSlots(); i++) {
            if (inv.getStackInSlot(i).isEmpty()) {
                continue;
            }
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.getCount() <= 0) {
                continue;
            }
            FluidStack inputFluid = tanks.getFirst().getPrimaryHandler().getFluid();
            FluidStack outputFluid = tanks.getSecond().getPrimaryHandler().getFluid();
            List<FluidStack> toTest = new ArrayList<>();
            if (!inputFluid.isEmpty()) toTest.add(inputFluid);
            if (!outputFluid.isEmpty()) toTest.add(outputFluid);
            Pair<FluidReactionRecipe, FluidStack> recipeStack = PhysicalRecipeHelper.matchFluidReactionRecipe(getLevel(), stack, toTest);
            if (recipeStack == null) continue;
            FluidReactionRecipe recipe = recipeStack.getFirst();
            FluidStack matchedFluid = recipeStack.getSecond();
            if (recipeStack.getFirst() != null) {
                final Container container = new SimpleContainer(stack);
                ItemStack result = recipe.assemble(container, getLevel().registryAccess());
                if (result.isEmpty()) {
                    continue;
                }
                if (recipe.isRemove()) {
                    if (matchedFluid.containsFluid(inputFluid)) {
                        tanks.getFirst().getPrimaryHandler().drain(matchedFluid.getAmount(), SmartFluidTank.FluidAction.EXECUTE);
                    } else if (matchedFluid.containsFluid(outputFluid)) {
                        tanks.getSecond().getPrimaryHandler().drain(matchedFluid.getAmount(), SmartFluidTank.FluidAction.EXECUTE);
                    }
                }
                inv.setStackInSlot(i, result);
            }
        }
    }
}
