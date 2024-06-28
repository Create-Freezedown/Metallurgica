package com.freezedown.metallurgica.content.mineral.drill.drill_activator;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.mineral.deposit.*;
import com.freezedown.metallurgica.content.mineral.drill.drill_tower.DrillTowerBlock;
import com.freezedown.metallurgica.foundation.config.MetallurgicaConfigs;
import com.freezedown.metallurgica.registry.MetallurgicaBlocks;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.VersionedInventoryTrackerBehaviour;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import joptsimple.internal.Strings;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class DrillActivatorBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation {
    public ItemStack item;
    public boolean isFull;
    public DrillActivatorItemHandler itemHandler;
    public LazyOptional<IItemHandler> lazyHandler;
    public VersionedInventoryTrackerBehaviour invVersionTracker;
    public static final int maxProgress = 30;
    
    public DrillActivatorBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        item = ItemStack.EMPTY;
        itemHandler = new DrillActivatorItemHandler(this);
        lazyHandler = LazyOptional.of(() -> itemHandler);
    }
    public void setItem(ItemStack stack) {
        item = stack;
        invVersionTracker.reset();
        if (level == null) {
            return;
        }
        if (!level.isClientSide) {
            notifyUpdate();
            award(AllAdvancements.CHUTE);
        }
    }
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(invVersionTracker = new VersionedInventoryTrackerBehaviour(this));
    }
    protected boolean canAcceptItem() {
        return itemHandler.getStackInSlot(1).isEmpty();
    }
    public int height;
    public float efficiency;
    public boolean canActivate;
    public BlockPos depositPos;
    public int progress;
    @Override
    public void lazyTick() {
        super.lazyTick();
        if (this.level == null) {
            return;
        }
        if (canActivate && getSpeed() > 0) {
            createExcavationParticles();
        }
    }
    @Override
    public void tick() {
        super.tick();
        if (this.level == null) {
            return;
        }
        
        isFull = itemHandler.getStackInSlot(1).getCount() >= 64;
        
        findDeposit();
        if (!(this.level.getBlockState(this.worldPosition.below()).getBlock() instanceof DrillTowerBlock) )
            canActivate = false;
        
        height = obtainTowerHeight();
        if (level.getGameTime() % 30 == 0) {
            if (canActivate && getSpeed() > 0) {
                increaseProgress();
            }
        }
        if (progress >= maxProgress) {
            drillDeposit();
            progress = 0;
        }
        
        efficiency = (float) (1.1 * (height + 1) / 5);
        
        checkDrillsAndActivate();
    }
    
    private int obtainTowerHeight() {
        int height = 0;
        if (level == null) {
            return height;
        }
        for (int i = 0; i < this.getBlockPos().getY() + 64; i++) {
            
            BlockPos checkedPos = new BlockPos(this.getBlockPos().getX(), (this.getBlockPos().getY() + 1) + i, this.getBlockPos().getZ());
            
            if (level.getBlockState(new BlockPos(checkedPos)).is(MetallurgicaBlocks.drillExpansion.get())) {
                height++;
            } else break;
            
            
        }
        
        
        return height;
    }
    
    //DrMangoTea was there :3
    public void findDeposit() {
        if (level == null) {
            return;
        }
        for (int i = 0; i < this.getBlockPos().getY() + 64; i++) {
            
            BlockPos checkedPos = new BlockPos(this.getBlockPos().getX(), (this.getBlockPos().getY() - 1) - i, this.getBlockPos().getZ());
            
            if (depositType(this.level.getBlockState(checkedPos)) != null) {
                depositPos = checkedPos;
                return;
            }
            
            if (!(level.getBlockState(new BlockPos(checkedPos)).is(MetallurgicaBlocks.drillTower.get()))) {
                depositPos = null;
                return;
            }
            
            
        }
        depositPos = null;
        
    }
    
    
    private void checkDrillsAndActivate() {
        
        if(depositPos==null)
            return;
        if(level == null)
            return;
        Deposit deposit = depositType(this.level.getBlockState(depositPos));
        if (deposit == null) {
            Metallurgica.LOGGER.info("Deposit is null");
            return;
        }
        
        canActivate = efficiency >= deposit.minimumEfficiency();
    }
    public void increaseProgress() {
        if (this.level == null) {
            return;
        }
        if (this.depositPos == null) {
            return;
        }
        Deposit deposit = depositType(this.level.getBlockState(depositPos));
        if (deposit == null) {
            return;
        }
        if (efficiency >= deposit.minimumEfficiency()) {
            float successChance = deposit.chance() * efficiency;
            if (this.level.random.nextFloat() <= (successChance *  0.23)) {
                Metallurgica.LOGGER.info("Progress increased by 1, now at {}", progress);
                progress++;
            }
        }
    }
    public void createExcavationParticles() {
        boolean shouldRender = MetallurgicaConfigs.client().renderExcavationParticles.get();
        if (!shouldRender) {
            return;
        }
        if (this.level == null) {
            return;
        }
        if (this.depositPos == null) {
            return;
        }
        Deposit deposit = depositType(this.level.getBlockState(depositPos));
        if (deposit == null) {
            return;
        }
        if (efficiency >= deposit.minimumEfficiency()) {
            if (isFull) {
                for (int i = 0; i < 10; i++) {
                    double x = (double) getBlockPos().getX() + 0.5D + (level.random.nextDouble() * 0.6D - 0.3D);
                    double y = (double) getBlockPos().getY() + 0.5D + (level.random.nextDouble() * 0.4D - 0.2D);
                    double z = (double) getBlockPos().getZ() + 0.5D + (level.random.nextDouble() * 0.6D - 0.3D);
                    level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.3D, 0.0D, 0.3);
                }
            }
            BlockParticleOption blockParticleOption = new BlockParticleOption(ParticleTypes.BLOCK, level.getBlockState(depositPos));
            for (int i = 0; i < 10; i++) {
                double x = (double) getBlockPos().getX() + 0.5D + (level.random.nextDouble() * 0.6D - 0.3D);
                double y = (double) getBlockPos().getY() + 0.5D + (level.random.nextDouble() * 0.4D - 0.2D);
                double z = (double) getBlockPos().getZ() + 0.5D + (level.random.nextDouble() * 0.6D - 0.3D);
                level.addParticle(blockParticleOption, x, y, z, 0.3D, 0.0D, 0.3D);
            }
        }
    }
    public void drillDeposit() {
        if (this.level == null) {
            return;
        }
        if (this.depositPos == null) {
            return;
        }
        Deposit deposit = depositType(this.level.getBlockState(depositPos));
        if (deposit == null) {
            return;
        }
        int efficiency = Mth.floor(this.efficiency);
        int amount = Mth.randomBetweenInclusive(level.random,1, 3 + efficiency);
        if (itemHandler.getStackInSlot(1).getCount() < 64 && itemHandler.getStackInSlot(1) == deposit.mineralItem()) {
            ItemStack stack = itemHandler.getStackInSlot(0);
            stack.grow(amount);
            Metallurgica.LOGGER.info("Grew stack by {} items", amount);
            itemHandler.insertItem(1, stack, true);
        } else if (itemHandler.getStackInSlot(1).isEmpty()) {
            ItemStack output = deposit.mineralItem();
            output.setCount(amount);
            Metallurgica.LOGGER.info("Set stack to {} items", amount);
            itemHandler.insertItem(1, output, true);
        }
    }
    
    public Deposit depositType(BlockState block) {
        return DepositManager.getDepositPropertiesOrNull(block);
    }
    
    
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        item = ItemStack.of(compound.getCompound("Item"));
        if (compound.contains("depositX")) {
            depositPos = new BlockPos(compound.getInt("depositX"), compound.getInt("depositY"), compound.getInt("depositZ"));
        }
        height = compound.getInt("height");
        efficiency = compound.getFloat("efficiency");
        canActivate = compound.getBoolean("canActivate");
        progress = compound.getInt("progress");
    }
    
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("Item", item.serializeNBT());
        if (depositPos != null) {
            compound.putInt("depositX", depositPos.getX());
            compound.putInt("depositY", depositPos.getY());
            compound.putInt("depositZ", depositPos.getZ());
        }
        compound.putInt("height", height);
        compound.putFloat("efficiency", efficiency);
        compound.putBoolean("canActivate", canActivate);
        compound.putInt("progress", progress);
    }
    
    @Override
    public void invalidate() {
        if (lazyHandler != null)
            lazyHandler.invalidate();
        super.invalidate();
    }
    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return lazyHandler.cast();
        return super.getCapability(cap, side);
    }
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (this.level == null) {
            return false;
        }
        if (this.depositPos == null) {
            return false;
        }
        Deposit deposit = depositType(this.level.getBlockState(depositPos));
        if (deposit == null) {
            return false;
        }
        String extracting = deposit.mineralItem().getDisplayName().getString().replace("[", "").replace("]", "");
        Component extractingComponent = Component.literal(extracting);
        Lang.translate("goggles.drill_activator").forGoggles(tooltip);
        Lang.translate("goggles.drill_activator.deposit", extractingComponent).style(ChatFormatting.DARK_GREEN).forGoggles(tooltip);
        if (isPlayerSneaking) {
            Lang.translate("goggles.drill_activator.item").forGoggles(tooltip);
            if (itemHandler.getStackInSlot(1).isEmpty()) {
                Lang.translate("goggles.drill_activator.item.empty").style(ChatFormatting.DARK_RED).forGoggles(tooltip);
            } else {
                tooltip.add(storedItem());
            }
        }
        if (efficiency < deposit.minimumEfficiency()) {
            Lang.translate("goggles.drill_activator.insufficient_efficiency").style(ChatFormatting.DARK_RED).forGoggles(tooltip);
        } else {
            if (!isPlayerSneaking) {
                Lang.translate("goggles.drill_activator.height", height).style(ChatFormatting.DARK_GREEN).forGoggles(tooltip);
                Lang.translate("goggles.drill_activator.efficiency", efficiency).style(ChatFormatting.DARK_GREEN).forGoggles(tooltip);
            }
            Lang.translate("goggles.drill_activator.progress", progress).forGoggles(tooltip);
            tooltip.add(progressBar(progress));
        }
        
        return true;
    }
    private MutableComponent storedItem() {
        MutableComponent itemName = Component.literal(itemHandler.getStackInSlot(1).getDisplayName().getString().replace("[", "").replace("]", ""));
        return Lang.builder()
                .text(Strings.repeat(' ', 4))
                .add(itemName)
                .text(" x" + itemHandler.getStackInSlot(1).getCount())
                .style(ChatFormatting.DARK_GREEN)
                .component();
    }
    private MutableComponent progressBar(int progress) {
        int redBars = maxProgress - progress;
        return Lang.builder().text(Strings.repeat(' ', 4))
                .add(bars(progress, ChatFormatting.DARK_GREEN))
                .add(bars(redBars, ChatFormatting.DARK_RED))
                .add(percent(progress))
                .component();
    }
    
    private MutableComponent percent(int progress) {
        float percent = Math.round((float) progress / maxProgress * 100);
        return Lang.builder().text(" ")
                .text(percent + "%")
                .component();
    }
    private MutableComponent bars(int level, ChatFormatting format) {
        return Components.literal(Strings.repeat('|', level))
                .withStyle(format);
    }
}
