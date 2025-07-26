package dev.metallurgists.metallurgica.foundation.mixin.create.block;

import dev.metallurgists.metallurgica.foundation.block_entity.behaviour.TankLiningBehaviour;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = FluidTankBlock.class, remap = false)
public abstract class FluidTankBlockMixin implements IBE<FluidTankBlockEntity> {

    @Inject(method = "onRemove(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Z)V", at = @At("HEAD"))
    private void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving, CallbackInfo ci) {
        if (state.hasBlockEntity() && (state.getBlock() != newState.getBlock() || !newState.hasBlockEntity())) {
            withBlockEntityDo(world, pos, be -> be.getBehaviour(TankLiningBehaviour.TYPE).destroy());
        }
    }
}
