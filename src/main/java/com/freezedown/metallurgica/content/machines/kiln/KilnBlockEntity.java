package com.freezedown.metallurgica.content.machines.kiln;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class KilnBlockEntity extends SmartBlockEntity implements IWrenchable, IItemHandler {
    public KilnBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> list) {

    }

    @Override
    public int getSlots() {
        return 0;
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int i) {
        return null;
    }

    @Override
    public @NotNull ItemStack insertItem(int i, @NotNull ItemStack itemStack, boolean b) {
        return null;
    }

    @Override
    public @NotNull ItemStack extractItem(int i, int i1, boolean b) {
        return null;
    }

    @Override
    public int getSlotLimit(int i) {
        return 0;
    }

    @Override
    public boolean isItemValid(int i, @NotNull ItemStack itemStack) {
        return false;
    }
}
