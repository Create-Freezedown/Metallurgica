package com.freezedown.metallurgica.foundation.mixin;

import com.freezedown.metallurgica.content.fluids.types.uf_backport.gas.FlowingGas;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LiquidBlockRenderer.class)
public abstract class MixinLiquidBlockRenderer {
    @Inject(method = "tesselate", at = @At("HEAD"), cancellable = true)
    private void tesselate(BlockAndTintGetter p_234370_, BlockPos p_234371_, VertexConsumer p_234372_, BlockState p_234373_, FluidState pFluidState, CallbackInfo ci) {
        if (!(pFluidState.getType() instanceof FlowingGas))
            return;
        
        ci.cancel();
    }
}
