package com.freezedown.metallurgica.foundation.mixin;

import com.freezedown.metallurgica.foundation.block_entity.behaviour.DisplayStateBehaviour;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.extensions.IForgeBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockState.class)
public abstract class BlockStateMixin extends BlockBehaviour.BlockStateBase implements IForgeBlockState {

    protected BlockStateMixin(Block owner, ImmutableMap<Property<?>, Comparable<?>> values, MapCodec<BlockState> propertiesCodec) {
        super(owner, values, propertiesCodec);
    }

    @Override
    public ItemStack getCloneItemStack(HitResult target, BlockGetter level, BlockPos pos, Player player) {
        BlockState thisState = this.asState();
        ItemStack stack = thisState.getBlock().getCloneItemStack(thisState, target, level, pos, player);
        BlockEntity blockEntity = metallurgica$getBE(level, pos);
        if (blockEntity != null) {
            BlockState displayedState = metallurgica$processDisplayState(blockEntity, thisState);
            stack = displayedState.getBlock().getCloneItemStack(displayedState, target, level, pos, player);
        }
        return stack;
    }

    @Override
    public boolean canHarvestBlock(BlockGetter level, BlockPos pos, Player player) {
        BlockState thisState = this.asState();
        boolean canHarvest = ForgeHooks.isCorrectToolForDrops(thisState, player);
        BlockEntity blockEntity = metallurgica$getBE(level, pos);
        if (blockEntity != null) {
            BlockState displayedState = metallurgica$processDisplayState(blockEntity, thisState);
            canHarvest = ForgeHooks.isCorrectToolForDrops(displayedState, player);
        }
        return canHarvest;
    }

    @Override
    public float getDestroySpeed(BlockGetter level, BlockPos pos) {
        BlockState thisState = this.asState();
        float destroySpeed = thisState.destroySpeed;
        BlockEntity blockEntity = metallurgica$getBE(level, pos);
        if (blockEntity != null) {
            BlockState displayedState = metallurgica$processDisplayState(blockEntity, thisState);
            destroySpeed = displayedState.destroySpeed;
        }
        return destroySpeed;
    }


    @Unique
    private BlockEntity metallurgica$getBE(BlockGetter level, BlockPos pos) {
        return level.getBlockEntity(pos);
    }

    @Unique
    private BlockState metallurgica$processDisplayState(BlockEntity blockEntity, BlockState original) {
        BlockState state = original;
        if (blockEntity instanceof SmartBlockEntity smart) {
            if (smart.getBehaviour(DisplayStateBehaviour.TYPE) != null) {
                DisplayStateBehaviour displayStateBehaviour = smart.getBehaviour(DisplayStateBehaviour.TYPE);
                if (!displayStateBehaviour.hasDisplayState()) return original;
                state = displayStateBehaviour.getDisplayState();
            }
        }
        return state;
    }
}
