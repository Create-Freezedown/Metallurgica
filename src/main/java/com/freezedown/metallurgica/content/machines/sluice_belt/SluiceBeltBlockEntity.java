package com.freezedown.metallurgica.content.machines.sluice_belt;

import com.freezedown.metallurgica.registry.MetallurgicaRecipeTypes;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.recipe.RecipeFinder;
import com.simibubi.create.foundation.utility.Couple;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;
import java.util.List;

public class SluiceBeltBlockEntity extends KineticBlockEntity {
    public SmartFluidTankBehaviour inputTank;
    protected SmartInventory outputInventory;
    protected SmartFluidTankBehaviour outputTank;
    private boolean contentsChanged;
    
    private Couple<SmartFluidTankBehaviour> tanks;
    
    protected LazyOptional<IItemHandlerModifiable> itemCapability;
    protected LazyOptional<IFluidHandler> fluidCapability;
    
    private SluicingRecipe lastRecipe;
    private static final Object sluicingRecipesKey = new Object();
    
    public int timer;
    
    public SluiceBeltBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        itemCapability = LazyOptional.of(() -> new CombinedInvWrapper(outputInventory));
        contentsChanged = true;
        tanks = Couple.create(inputTank, outputTank);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        inputTank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.INPUT, this, 1, 1000, true)
                .whenFluidUpdates(() -> contentsChanged = true);
        outputTank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.OUTPUT, this, 1, 1000, true)
                .whenFluidUpdates(() -> contentsChanged = true)
                .forbidInsertion();
        behaviours.add(inputTank);
        behaviours.add(outputTank);
        this.timer = -1;
        fluidCapability = LazyOptional.of(() -> {
            LazyOptional<? extends IFluidHandler> inputCap = inputTank.getCapability();
            LazyOptional<? extends IFluidHandler> outputCap = outputTank.getCapability();
            return new CombinedTankWrapper(outputCap.orElse(null), inputCap.orElse(null));
        });
    }
    
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        outputInventory.deserializeNBT(compound.getCompound("OutputItems"));
    }
    
    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("OutputItems", outputInventory.serializeNBT());
    }
    
    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(level, worldPosition, outputInventory);
    }
    
    @Override
    public void remove() {
        super.remove();
    }
    
    @Override
    public void invalidate() {
        super.invalidate();
        itemCapability.invalidate();
        fluidCapability.invalidate();
    }
    
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return itemCapability.cast();
        if (cap == ForgeCapabilities.FLUID_HANDLER)
            return fluidCapability.cast();
        return super.getCapability(cap, side);
    }
    public void tick() {
        super.tick();
        if (this.level == null) {
            return;
        }
        if(lastRecipe !=null) {
            if (timer == -1 && (outputTank.getPrimaryHandler().getFluidAmount() + lastRecipe.getFluidResults().get(0).getAmount()) <= outputTank.getPrimaryHandler().getCapacity() && canContinue()) {
                timer = lastRecipe.getProcessingDuration();
            }
        }
        
        findRecipe();
        
        if (timer > 0 &&
                (outputTank.getPrimaryHandler().getFluidAmount() + lastRecipe.getFluidResults().get(0).getAmount()) <= outputTank.getPrimaryHandler().getCapacity() && canContinue()
        ) {
            timer--;
        }
        
        if (timer == 0) {
            process();
            timer = -1;
        }
        
    }
    
    
    public boolean canContinue() {
        CombinedTankWrapper tankIn = new CombinedTankWrapper(inputTank.getPrimaryHandler());
        if (lastRecipe == null)
            return false;
        return lastRecipe.matches(tankIn, level) && outputInventory.isEmpty() && (outputTank.getPrimaryHandler().getFluid() == FluidStack.EMPTY || outputTank.getPrimaryHandler().getFluid().getFluid() == lastRecipe.getFluidResults().get(0).getFluid());
    }
    
    public void process() {
        if (level == null)
            return;
        if (level.isClientSide)
            return;
        
        outputTank.getPrimaryHandler().setFluid(new FluidStack(lastRecipe.getFluidResults().get(0).getFluid(), outputTank.getPrimaryHandler().getFluidAmount()+lastRecipe.getFluidResults().get(0).getAmount()));
        for (int i = 0; i < lastRecipe.getRollableResults().size(); i++) {
            outputInventory.insertItem(i, lastRecipe.getRollableResults().get(i).getStack(), false);
        }
        inputTank.getPrimaryHandler().drain(lastRecipe.getFluidIngredients().get(0).getRequiredAmount(), IFluidHandler.FluidAction.EXECUTE);
    }
    
    public SmartInventory getOutputInventory() {
        return outputInventory;
    }
    
    public void findRecipe(){
        CombinedTankWrapper tankIn = new CombinedTankWrapper(inputTank.getPrimaryHandler());
        if (lastRecipe == null || !lastRecipe.matches(tankIn, level)) {
            SluicingRecipe recipe = getMatchingRecipes();
            if (recipe!=null) {
                this.lastRecipe = recipe;
                sendData();
            }
        }
    }
    
    protected SluicingRecipe getMatchingRecipes() {
        
        
        List<Recipe<?>> list = RecipeFinder.get(sluicingRecipesKey, level, this::matchStaticFilters);
        
        
        for(int i = 0; i < list.toArray().length;i++){
            SluicingRecipe recipe = (SluicingRecipe) list.get(i);
            for(int y = 0; y < recipe.getFluidIngredients().get(0).getMatchingFluidStacks().toArray().length;y++)
                if(inputTank.getPrimaryHandler().getFluid().getFluid()==recipe.getFluidIngredients().get(0).getMatchingFluidStacks().get(y).getFluid())
                    if(inputTank.getPrimaryHandler().getFluidAmount()>=recipe.getFluidIngredients().get(0).getRequiredAmount())
                        return recipe;
        }
        
        return null;
    }
    
    protected <C extends Container> boolean matchStaticFilters(Recipe<C> r) {
        return r.getType() == MetallurgicaRecipeTypes.sluicing.getType();
    }
    
    
    
}
