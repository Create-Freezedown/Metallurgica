package com.freezedown.metallurgica.foundation.mixin;

import net.minecraft.server.level.WorldGenRegion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldGenRegion.class)
public class WorldGenRegionMixin {
    @Inject(method = "ensureCanWrite", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/Util;logAndPauseIfInIde(Ljava/lang/String;)V",
            ordinal = 0), cancellable = true)
    private void onEnsureCanWrite(CallbackInfoReturnable<Boolean> ci) {
        ci.setReturnValue(false);
    }
}
