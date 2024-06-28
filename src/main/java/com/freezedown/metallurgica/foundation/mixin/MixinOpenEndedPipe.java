package com.freezedown.metallurgica.foundation.mixin;

import com.freezedown.metallurgica.content.fluids.types.uf_backport.gas.FlowingGas;
import com.simibubi.create.content.fluids.OpenEndedPipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = OpenEndedPipe.class, remap = false)
public class MixinOpenEndedPipe {
    @Shadow
    private BlockPos outputPos;
    
    @Shadow private Level world;
    
    
    @Inject(method = "provideFluidToSpace", at = @At("HEAD"), cancellable = true)
    private void provideFluidToSpace(FluidStack fluid, boolean simulate, CallbackInfoReturnable<Boolean> cir) {
        if (!(fluid.getFluid() instanceof FlowingGas))
            return;
        
        if (world.getBlockState(outputPos).canBeReplaced(fluid.getFluid()) && world.getFluidState(outputPos).isEmpty()) {
            cir.setReturnValue(true);
            return;
        }
        
        if (!(world.getFluidState(outputPos).getType() instanceof ForgeFlowingFluid forgeFlowingFluid)) {
            cir.setReturnValue(false);
            return;
        }
        
        if (forgeFlowingFluid.getSource() != fluid.getFluid()) {
            cir.setReturnValue(false);
            return;
        }
        
        cir.setReturnValue(world.getFluidState(outputPos).getAmount() != FlowingGas.MAX_DENSITY);
    }
}
