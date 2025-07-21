package com.freezedown.metallurgica.foundation.mixin;

import com.freezedown.metallurgica.foundation.block_entity.behaviour.DisplayStateBehaviour;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow @Deprecated public abstract BlockPos getOnPosLegacy();

    @Shadow public abstract Level level();

    @ModifyExpressionValue(method = "spawnSprintParticle",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;",
                    ordinal = 0
            ))
    private BlockState redirectBlockStateParticles(BlockState original) {
        BlockPos blockpos = this.getOnPosLegacy();
        BlockEntity blockEntity = this.level().getBlockEntity(blockpos);
        return metallurgica$getDisplayedState(blockEntity, original);
    }

    @Unique
    private BlockState metallurgica$getDisplayedState(BlockEntity blockEntity, BlockState original) {
        if (blockEntity instanceof SmartBlockEntity smart) {
            if (smart.getBehaviour(DisplayStateBehaviour.TYPE) != null) {
                DisplayStateBehaviour displayStateBehaviour = smart.getBehaviour(DisplayStateBehaviour.TYPE);
                if (displayStateBehaviour.hasDisplayState()) {
                    return displayStateBehaviour.getDisplayState();
                } else {
                    return original;
                }
            } else {
                return original;
            }
        }
        return original;
    }
}
