package com.freezedown.metallurgica.content.machines.blast_furnace.hearth;


import com.drmangotea.tfmg.blocks.machines.TFMGMachineBlockEntity;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.freezedown.metallurgica.content.machines.blast_furnace.HeavyBlastingRecipe;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("ALL")
public class HearthBlockEntity extends TFMGMachineBlockEntity implements IHaveGoggleInformation {
    public Direction outputFacing;
    public BlockPos mainFloor;
    public float fuelEfficiency;
    public float speedModifier;
    public int timer;
    public HeavyBlastingRecipe recipe;
    public LerpedFloat coalCokeHeight;
    public LazyOptional<IItemHandlerModifiable> itemCapability;
    public SmartInventory inputInventory;
    public SmartInventory fuelInventory;
    int debug;
    
    // For rendering purposes only
    //private LerpedFloat slagLevel;
    //private LerpedFloat moltenMetalLevel;
    public HearthBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.outputFacing = this.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
        this.fuelEfficiency = 1000.0F;
        this.speedModifier = 1.0F;
        this.timer = -1;
        this.coalCokeHeight = LerpedFloat.linear();
        this.debug = 0;
        this.inputInventory = (new SmartInventory(1, this)).forbidInsertion().withMaxStackSize(64);
        this.fuelInventory = (new SmartInventory(1, this)).forbidInsertion().withMaxStackSize(64);
        this.itemCapability = LazyOptional.of(() -> {
            return new CombinedInvWrapper(this.inputInventory, this.fuelInventory);
        });
        this.tank1.getPrimaryHandler().setCapacity(8000);
        this.tank2.getPrimaryHandler().setCapacity(8000);
    }
    
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
    }
    
    public void tick() {
        super.tick();
        if (this.level == null) {
            return;
        }
        if (!(level.getBlockState(worldPosition).hasProperty(HearthBlock.validStructure)) && !(level.getBlockState(worldPosition).getValue(HearthBlock.validStructure))) {
            if (level.getGameTime() % 80 == 0) {
                ((HearthBlock) level.getBlockState(worldPosition).getBlock()).updateState(level.getBlockState(worldPosition), level, worldPosition);
            }
            return;
        }
        //if (slagLevel != null)
        //    slagLevel.tickChaser();
        //if (moltenMetalLevel != null)
        //    moltenMetalLevel.tickChaser();
        if (level.getGameTime() % 80 == 0) {
            ((HearthBlock) level.getBlockState(worldPosition).getBlock()).updateState(level.getBlockState(worldPosition), level, worldPosition);
        }
        HearthBlock hearth = (HearthBlock) this.getBlockState().getBlock();
        if (this.speedModifier != 0.0F) {
            this.fuelEfficiency = 400.0F * this.speedModifier;
        } else {
            this.fuelEfficiency = 400.0F;
        }
        this.speedModifier = 1.0F;
        
        this.outputFacing = this.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
        this.coalCokeHeight.chase(this.fuelInventory.getStackInSlot(0).getCount() + this.inputInventory.getStackInSlot(0).getCount(), 0.10000000149011612, LerpedFloat.Chaser.EXP);
        this.coalCokeHeight.tickChaser();
        if (level.getBlockState(worldPosition).hasProperty(HearthBlock.validStructure) && level.getBlockState(worldPosition).getValue(HearthBlock.validStructure)) {
            if (this.recipe != null && this.timer == -1 && this.tank1.getPrimaryHandler().getFluidAmount() + this.recipe.getFluidResults().get(0).getAmount() <= this.tank1.getPrimaryHandler().getCapacity() && this.tank2.getPrimaryHandler().getFluidAmount() + ((FluidStack)this.recipe.getFluidResults().get(1)).getAmount() <= this.tank2.getPrimaryHandler().getCapacity() && !this.fuelInventory.isEmpty()) {
                this.timer = (int)((float)this.recipe.getProcessingDuration() / this.speedModifier);
                this.inputInventory.getStackInSlot(0).setCount(this.inputInventory.getStackInSlot(0).getCount() - 1);
            }
            
            RecipeWrapper inventoryIn = new RecipeWrapper(this.inputInventory);
            //if (this.recipe == null || !this.recipe.matches(inventoryIn, this.level)) {
            //    Optional<HeavyBlastingRecipe> recipe = MetallurgicaRecipeTypes.heavy_blasting.find(inventoryIn, this.level);
            //    if (!recipe.isPresent()) {
            //        this.timer = -1;
            //        this.sendData();
            //    } else {
            //        this.recipe = recipe.get();
            //        this.sendData();
            //    }
            //}
            
            this.acceptInsertedItems();
            if (this.timer > 0 && this.tank1.getPrimaryHandler().getFluidAmount() + this.recipe.getFluidResults().get(0).getAmount() <= this.tank1.getPrimaryHandler().getCapacity() && this.tank2.getPrimaryHandler().getFluidAmount() + ((FluidStack)this.recipe.getFluidResults().get(1)).getAmount() <= this.tank2.getPrimaryHandler().getCapacity()) {
                --this.timer;
                int random = Create.RANDOM.nextInt((int)this.fuelEfficiency);
                if (random == 69) {
                    this.fuelInventory.getStackInSlot(0).shrink(1);
                }
            }
            
            if (this.timer == 0) {
                this.process();
                this.timer = -1;
            }
        }
    }
    protected AABB createRenderBoundingBox() {
        int x = this.getBlockPos().getX();
        int y = this.getBlockPos().getY();
        int z = this.getBlockPos().getZ();
        return new AABB((double)(x - 10), (double)(y - 10), (double)(z - 10), (double)(x + 10), (double)(y + 10), (double)(z + 10));
    }
    
    public void process() {
        if (!this.level.isClientSide) {
            this.tank1.getPrimaryHandler().setFluid(new FluidStack(this.recipe.getFluidResults().get(0).getFluid(), this.tank1.getPrimaryHandler().getFluidAmount() + this.recipe.getFluidResults().get(0).getAmount()));
            this.tank2.getPrimaryHandler().setFluid(new FluidStack(this.recipe.getFluidResults().get(1).getFluid(), this.tank2.getPrimaryHandler().getFluidAmount() + this.recipe.getFluidResults().get(1).getAmount()));
        }
    }
    protected void onFluidStackChanged(FluidStack newFluidStack) {
        if (!hasLevel())
            return;
        
        FluidType attributes = newFluidStack.getFluid().getFluidType();
        int luminosity = (int) (attributes.getLightLevel(newFluidStack) / 1.2f);
        boolean reversed = attributes.isLighterThanAir();
        
        if (!level.isClientSide) {
            setChanged();
            sendData();
        }
        
        //if (isVirtual()) {
        //    if (slagLevel == null)
        //        slagLevel = LerpedFloat.linear().startWithValue(getFillState());
        //    if (moltenMetalLevel == null)
        //        moltenMetalLevel = LerpedFloat.linear().startWithValue(getFillState());
        //    slagLevel.chase(getFillState(), .5f, LerpedFloat.Chaser.EXP);
        //    moltenMetalLevel.chase(getFillState(), .5f, LerpedFloat.Chaser.EXP);
        //}
    }
    public void acceptInsertedItems() {
        List<ItemEntity> itemsToPick = this.getItemsToPick();
        Iterator var2 = itemsToPick.iterator();
        
        while(true) {
            while(var2.hasNext()) {
                ItemEntity itemEntity = (ItemEntity)var2.next();
                ItemStack itemStack = itemEntity.getItem();
                int freeSpace;
                int count;
                if (itemStack.is(TFMGItems.COAL_COKE_DUST.get())) {
                    freeSpace = this.fuelInventory.getStackInSlot(0).getMaxStackSize() - this.fuelInventory.getStackInSlot(0).getCount();
                    count = itemStack.getCount();
                    if (count > freeSpace) {
                        itemStack.setCount(itemStack.getCount() - freeSpace);
                        this.fuelInventory.setItem(0, new ItemStack(TFMGItems.COAL_COKE_DUST.get(), this.fuelInventory.getStackInSlot(0).getCount() + freeSpace));
                    } else {
                        this.fuelInventory.setItem(0, new ItemStack(TFMGItems.COAL_COKE_DUST.get(), this.fuelInventory.getStackInSlot(0).getCount() + itemStack.getCount()));
                        itemEntity.discard();
                    }
                } else {
                    freeSpace = this.inputInventory.getStackInSlot(0).getMaxStackSize() - this.inputInventory.getStackInSlot(0).getCount();
                    count = itemStack.getCount();
                    if (this.inputInventory.isEmpty() || this.inputInventory.getItem(0).is(itemStack.getItem())) {
                        if (count > freeSpace) {
                            itemStack.setCount(itemStack.getCount() - freeSpace);
                            this.inputInventory.setItem(0, new ItemStack(itemStack.getItem(), this.inputInventory.getStackInSlot(0).getCount() + freeSpace));
                        } else {
                            this.inputInventory.setItem(0, new ItemStack(itemStack.getItem(), this.inputInventory.getStackInSlot(0).getCount() + itemStack.getCount()));
                            itemEntity.discard();
                        }
                    }
                }
            }
            
            return;
        }
    }
    
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        Lang.translate("goggles.blast_furnace.stats").style(ChatFormatting.GRAY).forGoggles(tooltip, 1);
        if (this.timer > 0) {
            Lang.translate("goggles.blast_furnace.status.running").style(ChatFormatting.YELLOW).forGoggles(tooltip, 1);
        } else {
            Lang.translate("goggles.blast_furnace.status.off").style(ChatFormatting.YELLOW).forGoggles(tooltip, 1);
        }
        
        Lang.translate("goggles.blast_furnace.size_stats").style(ChatFormatting.DARK_GRAY).forGoggles(tooltip, 1);
        
        Lang.translate("goggles.misc.storage_info").style(ChatFormatting.DARK_GRAY).forGoggles(tooltip, 1);
        Lang.translate("goggles.blast_furnace.item_count", this.inputInventory.getStackInSlot(0).getCount()).style(ChatFormatting.AQUA).forGoggles(tooltip, 1);
        Lang.translate("goggles.blast_furnace.fuel_amount", this.fuelInventory.getStackInSlot(0).getCount()).style(ChatFormatting.AQUA).forGoggles(tooltip, 1);
        Lang.translate("goggles.blast_furnace.nothing_lol").style(ChatFormatting.AQUA).forGoggles(tooltip, 1);
        
        
        LazyOptional<IFluidHandler> handler = this.getCapability(ForgeCapabilities.FLUID_HANDLER);
        Optional<IFluidHandler> resolve = handler.resolve();
        if (!resolve.isPresent()) {
            return false;
        } else {
            IFluidHandler tank = resolve.get();
            if (tank.getTanks() == 0) {
                return false;
            } else {
                LangBuilder mb = Lang.translate("generic.unit.millibuckets", new Object[0]);
                boolean isEmpty = true;
                
                for(int i = 0; i < tank.getTanks(); ++i) {
                    FluidStack fluidStack = tank.getFluidInTank(i);
                    if (!fluidStack.isEmpty()) {
                        Lang.fluidName(fluidStack).style(ChatFormatting.GRAY).forGoggles(tooltip, 1);
                        Lang.builder().add(Lang.number((double)fluidStack.getAmount()).add(mb).style(ChatFormatting.DARK_GREEN)).text(ChatFormatting.GRAY, " / ").add(Lang.number(tank.getTankCapacity(i)).add(mb).style(ChatFormatting.DARK_GRAY)).forGoggles(tooltip, 1);
                        isEmpty = false;
                    }
                }
                
                if (tank.getTanks() > 1) {
                    if (isEmpty) {
                        tooltip.remove(tooltip.size() - 1);
                    }
                    
                    return true;
                } else if (!isEmpty) {
                    return true;
                } else {
                    Lang.translate("gui.goggles.fluid_container.capacity", new Object[0]).add(Lang.number(tank.getTankCapacity(0)).add(mb).style(ChatFormatting.DARK_GREEN)).style(ChatFormatting.DARK_GRAY).forGoggles(tooltip, 1);
                    return true;
                }
            }
        }
    }
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        this.inputInventory.deserializeNBT(compound.getCompound("InputItems"));
        this.fuelInventory.deserializeNBT(compound.getCompound("Fuel"));
        this.timer = compound.getInt("Timer");
    }
    
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("InputItems", this.inputInventory.serializeNBT());
        compound.put("Fuel", this.fuelInventory.serializeNBT());
        compound.putInt("Timer", this.timer);
    }
    //public LerpedFloat getSlagLevel() {
    //    return slagLevel;
    //}
    //
    //public LerpedFloat getMoltenMetalLevel() {
    //    return moltenMetalLevel;
    //}
    public void invalidate() {
        super.invalidate();
        this.itemCapability.invalidate();
    }
    
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.itemCapability.cast();
        } else {
            return cap == ForgeCapabilities.FLUID_HANDLER ? this.fluidCapability.cast() : super.getCapability(cap, side);
        }
    }
    private AABB hearthItemGrabber(Direction direction, BlockPos pos) {
        return switch (direction) {
            case NORTH -> new AABB(pos.above().above().above().above().south().east(), pos.above().above().above().above().above(5).south().south().south().west());
            case SOUTH -> new AABB(pos.above().above().above().above().north().west(), pos.above().above().above().above().above(5).north().north().north().east());
            case EAST -> new AABB(pos.above().above().above().above().west().south(), pos.above().above().above().above().above(5).west().west().west().north());
            case WEST -> new AABB(pos.above().above().above().above().east().north(), pos.above().above().above().above().above(5).east().east().east().south());
            default -> null;
        };
    }
    public List<ItemEntity> getItemsToPick() {
        AABB searchArea = hearthItemGrabber(this.outputFacing, this.getBlockPos());
        
        
        if (searchArea != null) {
            return new ArrayList<>(this.level.getEntitiesOfClass(ItemEntity.class, searchArea));
        } else {
            return new ArrayList<>();
        }
    }
}
