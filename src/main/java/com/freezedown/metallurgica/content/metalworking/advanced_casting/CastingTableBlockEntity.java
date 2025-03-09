package com.freezedown.metallurgica.content.metalworking.advanced_casting;

import com.drmangotea.tfmg.blocks.machines.TFMGMachineBlockEntity;
import com.freezedown.metallurgica.foundation.util.MetalLang;
import com.simibubi.create.AllTags;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.fluids.drain.ItemDrainBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.recipe.RecipeFinder;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public class CastingTableBlockEntity extends TFMGMachineBlockEntity implements IHaveGoggleInformation {
    // slots
    public String moldType;
    AdvancedCastingRecipe recipe;
    public int timer = -1;
    private boolean hasMold = false;
    public SmartInventory outputInventory = (new SmartInventory(1, this)).withMaxStackSize(1);
    public SmartInventory moldInventory = (new SmartInventory(1, this)).withMaxStackSize(1);
    public LazyOptional<IItemHandlerModifiable> itemCapability = LazyOptional.of(() -> {
        return new CombinedInvWrapper(this.outputInventory, this.moldInventory);
    });
    private static final Object CastingRecipesKey = new Object();
    public int getTimer() {
        return this.timer;
    }
    public int getCoolingTime() {
        if (this.recipe == null) {
            return 0;
        } else
            return this.recipe.getCoolingTime();
    }
    public CastingTableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.tank1.getPrimaryHandler().setCapacity(4000);
        this.tank2.forbidExtraction();
        this.tank2.forbidInsertion();
        this.moldInventory.forbidExtraction();
        this.moldInventory.forbidInsertion();
        this.outputInventory.forbidInsertion();
    }
    public void tick() {
        super.tick();
        this.findRecipe();
        this.findMold();
        this.setCapacity();
        if (this.recipe != null && this.hasMold) {
            if (this.outputInventory.getStackInSlot(0).getCount() == 0) {
                if (this.tank1.getPrimaryHandler().getCapacity() == this.tank1.getPrimaryHandler().getFluidAmount()) {
                    if (this.timer == -1) {
                        this.timer = this.recipe.getCoolingTime();
                    }
                    if (this.timer > 0) {
                        --this.timer;
                    }
                    
                    if (this.timer == 0) {
                        this.outputInventory.setStackInSlot(0, this.recipe.getRecipeOutput(level.registryAccess()).copy());
                        this.tank1.getPrimaryHandler().setFluid(new FluidStack(Fluids.EMPTY, 0));
                        this.timer = -1;
                    }
                }
                
            }
        }
    }
    public void findRecipe() {
        CombinedTankWrapper tankIn = new CombinedTankWrapper(this.tank1.getPrimaryHandler(), this.tank2.getPrimaryHandler());
        if (this.recipe == null || !this.recipe.matches(tankIn, this.level)) {
            AdvancedCastingRecipe recipe = this.getMatchingRecipes();
            if (recipe != null) {
                this.recipe = recipe;
                this.sendData();
            }
        }
        
    }
    public void findMold() {
        //Get the moldType from the recipe and check the mold slot for an item with a mold tag specified in the moldType
        if (this.recipe != null) {
            if (!this.moldInventory.getStackInSlot(0).isEmpty()) {
                ItemStack mold = this.moldInventory.getStackInSlot(0);
                String moldType = this.recipe.getCastingMoldType();
                if (mold.is(AllTags.forgeItemTag("advanced_casting_molds/" + moldType))) {
                    this.moldType = moldType;
                    this.hasMold = true;
                }
            }
        }
    }
    public void setCapacity() {
        if (this.moldType == null) {
            if (this.tank1.getPrimaryHandler().getCapacity() < 4000) {
                this.tank1.getPrimaryHandler().setCapacity(4000);
            }
        } else if (this.tank1.getPrimaryHandler().getCapacity() != this.recipe.getInputFluid().getRequiredAmount()) {
            this.tank1.getPrimaryHandler().setCapacity(this.recipe.getInputFluid().getRequiredAmount());
        }
        
    }
    protected <C extends Container> boolean matchStaticFilters(Recipe<C> r) {
        return r instanceof AdvancedCastingRecipe;
    }
    protected Object getRecipeCacheKey() {
        return CastingRecipesKey;
    }
    
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
    }
    protected AdvancedCastingRecipe getMatchingRecipes() {
        List<Recipe<?>> list = RecipeFinder.get(this.getRecipeCacheKey(), this.level, this::matchStaticFilters);
        
        for(int i = 0; i < list.toArray().length; ++i) {
            AdvancedCastingRecipe recipe = (AdvancedCastingRecipe)list.get(i);
            
            for(int y = 0; y < recipe.getFluidIngredients().get(0).getMatchingFluidStacks().toArray().length; ++y) {
                if (this.tank1.getPrimaryHandler().getFluid().getFluid() == recipe.getFluidIngredients().get(0).getMatchingFluidStacks().get(y).getFluid() && this.tank1.getPrimaryHandler().getFluidAmount() >= recipe.getFluidIngredients().get(0).getRequiredAmount()) {
                    return recipe;
                }
            }
        }
        
        return null;
    }
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.itemCapability.cast();
        } else {
            return cap == ForgeCapabilities.FLUID_HANDLER ? this.fluidCapability.cast() : super.getCapability(cap, side);
        }
        //ForgeEventFactory.getMobGriefingEvent();
    }
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return containedFluidTooltip(tooltip, isPlayerSneaking, getCapability(ForgeCapabilities.FLUID_HANDLER));
    }
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        this.outputInventory.deserializeNBT(compound.getCompound("OutputItems"));
        this.moldInventory.deserializeNBT(compound.getCompound("CurrentMold"));
        this.timer = compound.getInt("Timer");
    }
    
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("OutputItems", this.outputInventory.serializeNBT());
        compound.put("CurrentMold", this.moldInventory.serializeNBT());
        compound.putInt("Timer", this.timer);
    }
    public void invalidate() {
        super.invalidate();
        this.itemCapability.invalidate();
    }
    
    public void interact(Player player, InteractionHand hand) {
        if (level == null || level.isClientSide) {
            return;
        }
        // can't interact if liquid inside
        if (!tank1.isEmpty()) {
            return;
        }
        
        ItemStack held = player.getItemInHand(hand);
        
        // if the held item is a mold, put it in the mold slot
        if (!held.isEmpty()) {
            if (moldInventory.getStackInSlot(0).isEmpty()) {
                moldInventory.setStackInSlot(0, held.split(1));
            }
        }
        boolean canRemoveOutput = (held.is(outputInventory.getStackInSlot(0).getItem()) && held.isStackable()) || held.isEmpty();
        // if the output is full, ignore the mold and give the item to the player
        if (!outputInventory.getStackInSlot(0).isEmpty() && canRemoveOutput) {
            ItemHandlerHelper.giveItemToPlayer(player, outputInventory.getStackInSlot(0), player.getInventory().selected);
            outputInventory.setStackInSlot(0, ItemStack.EMPTY);
            return;
        }
        
        // remove the mold from the mold slot and put it in the player's hand
        if (!moldInventory.getStackInSlot(0).isEmpty() && outputInventory.getStackInSlot(0).isEmpty() && held.isEmpty()) {
            ItemHandlerHelper.giveItemToPlayer(player, moldInventory.getStackInSlot(0), player.getInventory().selected);
            moldInventory.setStackInSlot(0, ItemStack.EMPTY);
        }
    }
}
