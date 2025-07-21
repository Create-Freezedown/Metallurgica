package com.freezedown.metallurgica.foundation.block_entity.behaviour;

import com.freezedown.metallurgica.content.fluids.effects.corrosion.CorrosionHandler;
import com.freezedown.metallurgica.foundation.item.lining.tank_lining.TankLiningStats;
import com.simibubi.create.foundation.blockEntity.IMultiBlockEntityContainer;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class TankLiningBehaviour extends BlockEntityBehaviour {
    public static final BehaviourType<TankLiningBehaviour> TYPE = new BehaviourType<>();

    private ItemStack liningStack;
    private TankLiningStats liningStats;

    boolean isMultiblockContainer = false;

    private CorrosionHandler corrosionHandler;

    public TankLiningBehaviour(SmartBlockEntity be) {
        super(be);
    }

    public TankLiningBehaviour forMultiblockContainer() {
        this.isMultiblockContainer = true;
        return this;
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    @Override
    public void initialize() {
        super.initialize();
        corrosionHandler = CorrosionHandler.create(this);
    }

    // Drop the lining item when the block is broken or destroyed. ONLY if it is the controller;

    @Override
    public void destroy() {
        super.destroy();
        if (isController()) {
            ItemStack liningStack = applyStats(getLiningStack());
            Containers.dropItemStack(getWorld(), getPos().getX(), getPos().getY(), getPos().getZ(), liningStack);
        }
    }

    // Use these for handling the lining and tank damage based on contained fluids and items.

    @Override
    public void lazyTick() {
        super.lazyTick();

        corrosionHandler.getInstance().tick();

    }

    @Override
    public void tick() {
        super.tick();

        if (getLiningStats() != null) {
            if (getLiningStats().getDamage() > getLiningStats().getMaxDurability()) {
                setLiningStack(ItemStack.EMPTY);
                setLiningStats(null);
            }
        }
    }

    // Everything beyond this point is specific to maintaining the data for the tank lining.
    // It works as intended.
    // DO NOT TOUCH!!!

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {
        super.read(nbt, clientPacket);
        if (isMultiblockContainer) {
            if (blockEntity instanceof IMultiBlockEntityContainer multiBlockEntityContainer) {
                if (multiBlockEntityContainer.isController()) readNbt(nbt);
            }
        } else {
            readNbt(nbt);
        }
    }

    @Override
    public void write(CompoundTag nbt, boolean clientPacket) {
        super.write(nbt, clientPacket);
        if (isMultiblockContainer) {
            if (blockEntity instanceof IMultiBlockEntityContainer multiBlockEntityContainer) {
                if (multiBlockEntityContainer.isController()) writeNbt(nbt);
            }
        } else {
            writeNbt(nbt);
        }
    }

    public ItemStack getLiningStack() {
        if (isController()) {
            return getBlockEntity().getBehaviour(TYPE).liningStack;
        } else {
            SmartBlockEntity controllerBE = ((IMultiBlockEntityContainer) blockEntity).getControllerBE();
            if (controllerBE != null) {
                return controllerBE.getBehaviour(TYPE).liningStack;
            }
        }
        return ItemStack.EMPTY;
    }

    public void setLiningStack(ItemStack liningStack) {
        if (isController()) {
            this.liningStack = liningStack;
            getBlockEntity().notifyUpdate();
        } else {
            SmartBlockEntity controllerBE = ((IMultiBlockEntityContainer) blockEntity).getControllerBE();
            if (controllerBE != null) {
                controllerBE.getBehaviour(TYPE).liningStack = liningStack;
                controllerBE.notifyUpdate();
            }
        }
    }

    public TankLiningStats getLiningStats() {
        if (isController()) {
            return getBlockEntity().getBehaviour(TYPE).liningStats;
        } else {
            SmartBlockEntity controllerBE = ((IMultiBlockEntityContainer) blockEntity).getControllerBE();
            if (controllerBE != null) {
                return controllerBE.getBehaviour(TYPE).liningStats;
            }
        }
        return null;
    }

    public void setLiningStats(TankLiningStats liningStats) {
        if (isController()) {
            getBlockEntity().getBehaviour(TYPE).liningStats = liningStats;
        } else {
            SmartBlockEntity controllerBE = ((IMultiBlockEntityContainer) blockEntity).getControllerBE();
            if (controllerBE != null) {
                controllerBE.getBehaviour(TYPE).liningStats = liningStats;
            }
        }
    }

    public boolean hasLining() {
        if (getLiningStack() == null) return false;
        return !getLiningStack().isEmpty();
    }

    public void setLining(ItemStack liningStack) {
        setLiningStats(TankLiningStats.readFromNBT(liningStack.getOrCreateTag()));
        setLiningStack(cullStats(liningStack));
        getBlockEntity().notifyUpdate();
    }

    public ItemStack removeLining() {
        ItemStack removedLining = applyStats(getLiningStack().copy());
        setLiningStack(ItemStack.EMPTY);
        setLiningStats(null);
        getBlockEntity().notifyUpdate();
        return removedLining;
    }

    public ItemStack cullStats(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return ItemStack.EMPTY;
        CompoundTag nbt = stack.getOrCreateTag();
        if (nbt.contains("liningStats")) {
            nbt.remove("liningStats");
        }
        stack.setTag(nbt);
        return stack;
    }

    public ItemStack applyStats(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return ItemStack.EMPTY;
        CompoundTag nbt = stack.getOrCreateTag();
        getLiningStats().writeToNBT(nbt);
        stack.setTag(nbt);
        return stack;
    }

    private SmartBlockEntity getBlockEntity() {
        if (isMultiblockContainer) {
            if (blockEntity instanceof IMultiBlockEntityContainer multiBlockEntityContainer) {
                return multiBlockEntityContainer.getControllerBE();
            }
        }
        return blockEntity;
    }

    private boolean isController() {
        if (isMultiblockContainer) {
            if (blockEntity instanceof IMultiBlockEntityContainer multiBlockEntityContainer) {
                return multiBlockEntityContainer.isController();
            }
        }
        return true;
    }

    private void writeNbt(CompoundTag nbt) {
        if (hasLining()) {
            nbt.put("liningStack", getLiningStack().save(new CompoundTag()));
            CompoundTag liningTag = new CompoundTag();
            getLiningStats().writeToNBT(liningTag);
            nbt.put("liningStats", liningTag);
        } else {
            nbt.remove("liningStack");
            nbt.remove("liningStats");
        }
    }

    private void readNbt(CompoundTag nbt) {
        if (nbt.contains("liningStack")) {
            setLiningStack(ItemStack.of(nbt.getCompound("liningStack")));
            if (nbt.contains("liningStats")) {
                setLiningStats(TankLiningStats.readFromNBT(nbt.getCompound("liningStats")));
            } else {
                setLiningStats(null);
            }
        } else {
            setLiningStack(ItemStack.EMPTY);
            setLiningStats(null);
        }
    }

    public void appendToTooltip(List<Component> tooltipComponents) {
        if (getLiningStats() != null && getLiningStack() != null) {
            getLiningStats().addToGoggleTooltip(tooltipComponents, getLiningStack());
        }
    }
}
