package com.freezedown.metallurgica.foundation.item;

import net.createmod.catnip.theme.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;

import java.util.Collections;
import java.util.Optional;

public abstract class ReactiveItem extends MetallurgicaItem {

    private final NonNullList<Fluid> reactants;
    private boolean sensitiveToAir = false;
    private int maxAirExposure = 10;
    private boolean sensitiveToRain = false;
    private boolean sensitiveToHeat = false;
    private Optional<ItemStack> result = Optional.empty();


    public ReactiveItem(Properties pProperties) {
        super(pProperties);
        this.reactants = NonNullList.create();
    }

    public ReactiveItem withReactants(Fluid... reactants) {
        Collections.addAll(this.reactants, reactants);
        return this;
    }

    public ReactiveItem addReactant(Fluid reactant) {
        this.reactants.add(reactant);
        return this;
    }

    public ReactiveItem sensitiveToAir(int maxAirExposure) {
        this.sensitiveToAir = true;
        this.maxAirExposure = maxAirExposure;
        return this;
    }

    public ReactiveItem sensitiveToRain() {
        this.sensitiveToRain = true;
        return this;
    }

    public ReactiveItem sensitiveToHeat() {
        this.sensitiveToHeat = true;
        return this;
    }

    public ReactiveItem withResult(ItemStack result) {
        this.result = Optional.of(result);
        return this;
    }

    public NonNullList<Fluid> getReactants() {
        return this.reactants;
    }

    public boolean reactsWith(Fluid fluid) {
        return this.reactants.contains(fluid);
    }

    public boolean isSensitiveToAir() {
        return sensitiveToAir;
    }

    public boolean isSensitiveToRain() {
        return sensitiveToRain;
    }

    public boolean isSensitiveToHeat() {
        return sensitiveToHeat;
    }

    public int airExposureCounter(ItemStack stack) {
        return stack.getOrCreateTag().getInt("AirExposure");
    }


    public int getBarColor(ItemStack stack) {
        return Color.mixColors(0xf5f6f9, 0xb6bec7, (float)this.airExposureCounter(stack) / maxAirExposure);
    }

    public int getBarWidth(ItemStack stack) {
        return Math.round(13.0F * (float)this.airExposureCounter(stack) / maxAirExposure);
    }

    public boolean isBarVisible(ItemStack stack) {
        return this.sensitiveToAir && this.airExposureCounter(stack) > 0;
    }

    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        //every second in air, the item will be exposed to air
        if (entity.level().isClientSide)
            return false;

        if (entity.tickCount % 20 == 0) {
            if (entity.isInWaterOrRain()) {
                stack.getOrCreateTag().putInt("AirExposure", 0);
                return false;
            }
            if (this.sensitiveToAir) {
                int airExposure = this.airExposureCounter(stack);
                if (airExposure < maxAirExposure) {
                    stack.getOrCreateTag().putInt("AirExposure", airExposure + 1);
                }
            }
        }

        if (this.sensitiveToAir && this.airExposureCounter(stack) >= maxAirExposure) {
            if (this.result.isPresent()) {
                replaceStack(true, entity, stack, this.result.get());
            } else {
                entity.remove(Entity.RemovalReason.DISCARDED);
            }
            onReaction(stack, entity.level(), entity, Optional.empty(), ReactionType.AIR);
        }

        for (Fluid reactant : this.reactants) {
            if (isInFluidType(entity.level(), entity, reactant)) {
                onReaction(stack, entity.level(), entity, Optional.of(reactant), ReactionType.FLUID);
                return true;
            }
        }

        if (this.sensitiveToRain && entity.level().isRainingAt(entity.blockPosition())) {
            onReaction(stack, entity.level(), entity, Optional.empty(), ReactionType.RAIN);
            return true;
        }

        return false;
    }

    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        if (world.isClientSide)
            return;

        if (entity instanceof Player player) {
            if (entity.tickCount % 20 == 0 && isBeingHeld(player, stack)) {
                if (entity.isInFluidType()) {
                    stack.getOrCreateTag().putInt("AirExposure", 0);
                    return;
                }
                if (this.sensitiveToAir) {
                    int airExposure = this.airExposureCounter(stack);
                    if (airExposure < 10) {
                        stack.getOrCreateTag().putInt("AirExposure", airExposure + 1);
                    }
                }
            }

            if (this.sensitiveToAir && this.airExposureCounter(stack) >= 10) {
                if (this.result.isPresent()) {
                    replaceStack(false, entity, stack, this.result.get());
                } else {
                    stack.shrink(stack.getCount());
                }
                onReaction(stack, world, entity, Optional.empty(), ReactionType.AIR);
            }

            for (Fluid reactant : this.reactants) {
                if (isInFluidType(world, entity, reactant) && isBeingHeld(player, stack) && player.isEyeInFluidType(reactant.getFluidType())) {
                    onReaction(stack, world, entity, Optional.of(reactant), ReactionType.FLUID);
                    return;
                }
            }

            if (this.sensitiveToRain && entity.level().isRainingAt(entity.blockPosition())) {
                onReaction(stack, entity.level(), entity, Optional.empty(), ReactionType.RAIN);
            }
        }
    }

    private boolean isBeingHeld(LivingEntity entity, ItemStack stack) {
        return entity.getMainHandItem() == stack || entity.getOffhandItem() == stack;
    }

    private boolean isInFluidType(Level world, Entity entity, Fluid fluid) {
        BlockPos pos = entity.blockPosition();
        return world.getFluidState(pos).getType() == fluid;
    }

    private void replaceStack(boolean itemEntity, Entity entity, ItemStack old, ItemStack replacement) {
        if (itemEntity && entity instanceof ItemEntity iEntity) {
            replacement.setCount(old.getCount());
            iEntity.setItem(replacement);
        } else {
            if (entity instanceof Player player) {
                IItemHandler inv = new PlayerMainInvWrapper(player.getInventory());
                for (int i = 0; i < inv.getSlots(); i++) {
                    if (inv.getStackInSlot(i).getItem() == this) {
                        replacement.setCount(inv.getStackInSlot(i).getCount());
                        inv.extractItem(i, inv.getStackInSlot(i).getCount(), false);
                        inv.insertItem(i, replacement, false);
                    }
                }
            }
        }
    }

    public abstract void onReaction(ItemStack stack, Level world, Entity entity, Optional<Fluid> fluid, ReactionType type);


    public enum ReactionType {
        AIR,
        FLUID,
        RAIN,
        HEAT
    }
}
