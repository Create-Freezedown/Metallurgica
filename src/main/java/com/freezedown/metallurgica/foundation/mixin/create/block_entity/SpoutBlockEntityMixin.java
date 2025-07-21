package com.freezedown.metallurgica.foundation.mixin.create.block_entity;

import com.freezedown.metallurgica.foundation.block_entity.behaviour.TankLiningBehaviour;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = SpoutBlockEntity.class, remap = false)
public abstract class SpoutBlockEntityMixin extends SmartBlockEntity {

    @Unique
    TankLiningBehaviour metallurgica$tankLiningBehaviour;

    public SpoutBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "addBehaviours(Ljava/util/List;)V", at = @At("HEAD"))
    public void addBehaviours(List<BlockEntityBehaviour> behaviours, CallbackInfo ci) {
        behaviours.add(metallurgica$tankLiningBehaviour = new TankLiningBehaviour(this));
    }

    @Inject(method = "addToGoggleTooltip(Ljava/util/List;Z)Z", at = @At("TAIL"))
    public void addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking, CallbackInfoReturnable<Boolean> cir) {
        if (metallurgica$tankLiningBehaviour != null && metallurgica$tankLiningBehaviour.hasLining()) {
            metallurgica$tankLiningBehaviour.appendToTooltip(tooltip);
        }
    }
}
