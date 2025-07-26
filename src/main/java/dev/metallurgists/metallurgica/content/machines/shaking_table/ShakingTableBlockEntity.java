package dev.metallurgists.metallurgica.content.machines.shaking_table;

import dev.metallurgists.metallurgica.foundation.util.MetalLang;
import dev.metallurgists.metallurgica.registry.MetallurgicaRecipeTypes;
import com.simibubi.create.AllParticleTypes;
import com.simibubi.create.content.fluids.particle.FluidParticleData;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import net.createmod.catnip.lang.LangBuilder;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
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
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.List;
import java.util.Optional;

//TODO: Recipe continues even if the fluid requirement isn't met
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
                this.timer -= getProcessingSpeed();
                if (this.level.isClientSide) {
                    this.spawnParticles();
                } else {
                    if (this.timer <= 0 && this.haveRecipe()) {
                        this.process();
                        this.timer = 0;
                    }
            
                }
            } else if (this.shakingTableBehaviour.heldItem != null && !this.shakingTableBehaviour.heldItem.stack.isEmpty()) {
                RecipeWrapper inventoryIn = new RecipeWrapper(this.shakingTableBehaviour.itemHandler);
                if (this.lastRecipe != null && this.haveRecipe()) {
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
        if (level == null) {
            return;
        }
        if (this.haveRecipe()) {
            ItemStack stackInSlot = this.shakingTableBehaviour.getHeldItemStack();
            RandomSource r = level.random;
            float rim = 1 / 16f;
            float space = 13.5f / 16f;
            float surface = worldPosition.getY() + rim + space;
            float x = worldPosition.getX() + rim + space * r.nextFloat();
            float z = worldPosition.getZ() + rim + space * r.nextFloat();
            if (!stackInSlot.isEmpty()) {
                ItemParticleOption data = new ItemParticleOption(ParticleTypes.ITEM, stackInSlot);
                float angle = this.level.random.nextFloat() * 360.0F;
                Vec3 offset = new Vec3(0.0, 0.0, 0.5);
                offset = VecHelper.rotate(offset, angle, Direction.Axis.Y);
                Vec3 target = VecHelper.rotate(offset, this.getSpeed() > 0.0F ? 25.0 : -25.0, Direction.Axis.Y);
                target = VecHelper.offsetRandomly(target.subtract(offset), this.level.random, 0.0078125F);
                this.level.addParticle(data, x, surface, z, target.x, target.y, target.z);
            }
        }
    }
    public void spawnFluidParticles() {
        if (level == null) {
            return;
        }
        if (this.haveRecipe()) {
            FluidStack fluid = this.tankInventory.getFluid();
            RandomSource r = level.random;
            float rim = 1 / 16f;
            float space = 13.5f / 16f;
            float surface = worldPosition.getY() + rim + space;
            float x = worldPosition.getX() + rim + space * r.nextFloat();
            float z = worldPosition.getZ() + rim + space * r.nextFloat();
            if (!fluid.isEmpty()) {
                level.addAlwaysVisibleParticle(new FluidParticleData(AllParticleTypes.BASIN_FLUID.get(), fluid), x, surface, z, 0, 0, 0);
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
            if (this.lastRecipe != null && this.lastRecipe.matches(inventoryIn, tankInventory, this.level)) {
                return true;
            } else
                return MetallurgicaRecipeTypes.shaking.find(inventoryIn, this.level).isPresent();
        }
    }
    
    public int getProcessingSpeed() {
        return Mth.clamp((int)Math.abs(this.getSpeed() / 16.0F), 1, 512);
    }
    
    private void process() {
        if (!this.lastRecipe.getFluidIngredients().isEmpty()) {
            this.tankInventory.drain(this.lastRecipe.getFluidIngredients().get(0).getRequiredAmount(), IFluidHandler.FluidAction.EXECUTE);
        }
        this.shakingTableBehaviour.heldItem.stack.shrink(1);
        this.lastRecipe.rollResults().forEach((stackx) -> {
            ItemHandlerHelper.insertItemStacked(this.shakingTableBehaviour.processingOutputBuffer, stackx, false);
        });
        this.sendData();
        this.setChanged();
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
        if (cap == ForgeCapabilities.ITEM_HANDLER)
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
        LangBuilder mb = MetalLang.translate("generic.unit.millibuckets");
        
        FluidStack fluidStack = tankInventory.getFluid();
        if (!fluidStack.isEmpty()) {
            MetalLang.fluidName(fluidStack).style(ChatFormatting.GRAY).forGoggles(tooltip, 1);
            MetalLang.builder().add(MetalLang.number(fluidStack.getAmount()).add(mb).style(ChatFormatting.GOLD)).text(ChatFormatting.GRAY, " / ").add(MetalLang.number(tankInventory.getTankCapacity(0)).add(mb).style(ChatFormatting.DARK_GRAY)).forGoggles(tooltip, 1);
            return true;
        } else {
            MetalLang.translate("gui.goggles.fluid_container.capacity", new Object[0]).add(MetalLang.number(tankInventory.getTankCapacity(0)).add(mb).style(ChatFormatting.DARK_GREEN)).style(ChatFormatting.DARK_GRAY).forGoggles(tooltip, 1);
            return true;
        }
    }
}
