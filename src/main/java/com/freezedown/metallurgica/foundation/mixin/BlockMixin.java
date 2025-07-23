package com.freezedown.metallurgica.foundation.mixin;

import com.freezedown.metallurgica.foundation.block_entity.behaviour.DisplayStateBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.extensions.IForgeBlock;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Block.class)
public abstract class BlockMixin extends BlockBehaviour implements IForgeBlock {

    public BlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;)Ljava/util/List;", at = @At("HEAD"), cancellable = true)
    private static void getDrops(BlockState state, ServerLevel level, BlockPos pos, BlockEntity blockEntity, CallbackInfoReturnable<List<ItemStack>> cir) {
        LootParams.Builder lootparams$builder = (new LootParams.Builder(level)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity);
        if (blockEntity instanceof SmartBlockEntity smart && smart.getBehaviour(DisplayStateBehaviour.TYPE) != null) {
            DisplayStateBehaviour displayStateBehaviour = smart.getBehaviour(DisplayStateBehaviour.TYPE);
            cir.setReturnValue(displayStateBehaviour.getDrops(lootparams$builder));
        }
    }

    @Inject(method = "getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;", at = @At("HEAD"), cancellable = true)
    private static void getDrops(BlockState state, ServerLevel level, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack tool, CallbackInfoReturnable<List<ItemStack>> cir) {
        LootParams.Builder lootparams$builder = (new LootParams.Builder(level)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).withParameter(LootContextParams.TOOL, tool).withOptionalParameter(LootContextParams.THIS_ENTITY, entity).withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity);
        if (blockEntity instanceof SmartBlockEntity smart && smart.getBehaviour(DisplayStateBehaviour.TYPE) != null) {
            DisplayStateBehaviour displayStateBehaviour = smart.getBehaviour(DisplayStateBehaviour.TYPE);
            cir.setReturnValue(displayStateBehaviour.getDrops(lootparams$builder));
        }
    }

    @Override
    public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity) {
        BlockEntity blockEntity = metallurgica$getBE(level, pos);
        if (blockEntity instanceof SmartBlockEntity smart && smart.getBehaviour(DisplayStateBehaviour.TYPE) != null) {
            DisplayStateBehaviour displayStateBehaviour = smart.getBehaviour(DisplayStateBehaviour.TYPE);
            return displayStateBehaviour.getDisplayedSoundType();
        }
        return soundType;
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
