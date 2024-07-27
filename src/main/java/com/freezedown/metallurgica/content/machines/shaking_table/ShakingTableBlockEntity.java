package com.freezedown.metallurgica.content.machines.shaking_table;

import com.freezedown.metallurgica.registry.MetallurgicaRecipeTypes;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.List;
import java.util.Optional;

public class ShakingTableBlockEntity extends KineticBlockEntity {
    protected LazyOptional<IFluidHandler> fluidCapability;
    protected FluidTank tankInventory;
    ShakingTableBehaviour shakingTableBehaviour;
    
    private ShakingRecipe lastRecipe;
    public int timer;
    
    public ShakingTableBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        tankInventory = createInventory();
        fluidCapability = LazyOptional.of(() -> tankInventory);
    }
    
    public void tick() {
        super.tick();
        if (this.level == null) {
            return;
        }
        if (this.getSpeed() != 0.0F) {
            //if (!shakingTableBehaviour.isOutputEmpty()) {
            //    return;
            //}
            if (this.timer > 0) {
                this.timer -= this.getProcessingSpeed();
                if (this.level.isClientSide) {
                    this.spawnParticles();
                } else {
                    if (this.timer <= 0 && this.haveRecipe()) {
                        this.shakingTableBehaviour.heldItem.stack.shrink(1);
                        this.process();
                        this.timer = 0;
                    }
            
                }
            } else if (this.lastRecipe != null && lastRecipe.getIngredients().get(0).test(this.shakingTableBehaviour.heldItem.stack)) {
                RecipeWrapper inventoryIn = new RecipeWrapper(this.shakingTableBehaviour.itemHandler);
                if (this.lastRecipe != null && this.lastRecipe.matches(inventoryIn, this.level)) {
                    this.timer = this.lastRecipe.getProcessingDuration();
                    if (this.timer == 0) {
                        this.timer = 100;
                    }
                    
                    this.sendData();
                } else {
                    Optional<ShakingRecipe> recipe = MetallurgicaRecipeTypes.shaking.find(inventoryIn, this.level);
                    if (!recipe.isPresent()) {
                        this.timer = 100;
                        this.sendData();
                    } else {
                        this.lastRecipe = recipe.get();
                        this.timer = this.lastRecipe.getProcessingDuration();
                        this.sendData();
                    }
                }
            }
        }
    }
    
    public void spawnParticles() {
        if (this.haveRecipe()) {
            ItemStack stackInSlot = this.shakingTableBehaviour.getHeldItemStack();
            if (!stackInSlot.isEmpty()) {
                ItemParticleOption data = new ItemParticleOption(ParticleTypes.ITEM, stackInSlot);
                float angle = this.level.random.nextFloat() * 360.0F;
                Vec3 offset = new Vec3(0.0, 0.0, 0.5);
                offset = VecHelper.rotate(offset, (double)angle, Direction.Axis.Y);
                Vec3 target = VecHelper.rotate(offset, this.getSpeed() > 0.0F ? 25.0 : -25.0, Direction.Axis.Y);
                Vec3 center = offset.add(VecHelper.getCenterOf(this.worldPosition));
                target = VecHelper.offsetRandomly(target.subtract(offset), this.level.random, 0.0078125F);
                this.level.addParticle(data, center.x, center.y + 0.5, center.z, target.x, target.y, target.z);
            }
        }
    }
    
    public boolean haveRecipe() {
        return this.canProcess(this.shakingTableBehaviour.getHeldItemStack());
    }
    
    private boolean canProcess(ItemStack stack) {
        if (level == null) {
            return false;
        }
        if (Mth.abs(this.getSpeed()) < IRotate.SpeedLevel.FAST.getSpeedValue()) {
            return false;
        } else {
            ItemStackHandler tester = new ItemStackHandler(1);
            tester.setStackInSlot(0, stack);
            RecipeWrapper inventoryIn = new RecipeWrapper(tester);
            if (this.lastRecipe != null && this.lastRecipe.matches(inventoryIn, this.level)) {
                return true;
            } else
                return MetallurgicaRecipeTypes.shaking.find(inventoryIn, this.level).isPresent();
        }
    }
    
    public int getProcessingSpeed() {
        return Mth.clamp((int)Math.abs(this.getSpeed() / 16.0F), 1, 512);
    }
    
    private void process() {
       
        this.lastRecipe.rollResults().forEach((stackx) -> {
            ItemHandlerHelper.insertItemStacked(this.shakingTableBehaviour.processingOutputBuffer, stackx, false);
        });
    }
    
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(shakingTableBehaviour = new ShakingTableBehaviour(this));
        shakingTableBehaviour.addSubBehaviours(behaviours);
    }
    
    protected SmartFluidTank createInventory() {
        return new SmartFluidTank(1000, this::onFluidStackChanged);
    }
    
    protected void onFluidStackChanged(FluidStack newFluidStack) {
        this.sendData();
    }
    
    @Override
    public void invalidate() {
        super.invalidate();
        fluidCapability.invalidate();
    }
    
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return shakingTableBehaviour.getItemCapability(cap, side).cast();
        if (isFluidHandlerCap(cap) && (side == null || ShakingTableBlock.hasPipeTowards(level, worldPosition, getBlockState(), side)))
            return fluidCapability.cast();
        return super.getCapability(cap, side);
    }
    
    public ItemStack getHeldItem() {
        return shakingTableBehaviour.getHeldItemStack();
    }
    
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        LazyOptional<IFluidHandler> handler = this.getCapability(ForgeCapabilities.FLUID_HANDLER);
        Optional<IFluidHandler> resolve = handler.resolve();
        if (!resolve.isPresent()) {
            return false;
        } else {
            IFluidHandler tank = resolve.get();
            if (tank.getTanks() == 0) {
                return false;
            } else {
                LangBuilder mb = Lang.translate("generic.unit.millibuckets");
                boolean isEmpty = true;
                
                for(int i = 0; i < tank.getTanks(); ++i) {
                    FluidStack fluidStack = tank.getFluidInTank(i);
                    if (!fluidStack.isEmpty()) {
                        Lang.fluidName(fluidStack).style(ChatFormatting.GRAY).forGoggles(tooltip, 1);
                        Lang.builder().add(Lang.number((double)fluidStack.getAmount()).add(mb).style(ChatFormatting.GOLD)).text(ChatFormatting.GRAY, " / ").add(Lang.number((double)tank.getTankCapacity(i)).add(mb).style(ChatFormatting.DARK_GRAY)).forGoggles(tooltip, 1);
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
}
