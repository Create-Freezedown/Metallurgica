package com.freezedown.metallurgica.foundation.mixin;

import com.freezedown.metallurgica.content.fluids.types.uf_backport.gas.FlowingGas;
import com.simibubi.create.content.fluids.OpenEndedPipe;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "com.simibubi.create.content.fluids.OpenEndedPipe$OpenEndFluidHandler", remap = false)
public abstract class MixinOpenEndFluidHandler extends FluidTank {
    
    @Shadow
    @Final
    OpenEndedPipe this$0;
    
    public MixinOpenEndFluidHandler(int capacity) {
        super(capacity);
    }
    
    @Inject(method = "fill", at = @At("TAIL"))
    private void fill(FluidStack inputStack, IFluidHandler.FluidAction action, CallbackInfoReturnable<Integer> cir) {
        FluidStack resource = getFluid();
        if (action.simulate()) {
            return;
        }
        
        if (!(resource.getFluid() instanceof FlowingGas flowingGas))
            return;
        
        int amount = resource.getAmount();
        
        if (this$0.getWorld().getBlockState(this$0.getOutputPos()).canBeReplaced(resource.getFluid()) &&
                (
                        this$0.getWorld().getBlockState(this$0.getOutputPos()).getFluidState().isEmpty() ^ this$0.getWorld().getBlockState(this$0.getOutputPos()).getFluidState().is(resource.getFluid())
                )
        )
        {
            if (amount * FlowingGas.MAX_DENSITY / 1000 == 0) {
                return;
            }
            this$0.getWorld().setBlock(this$0.getOutputPos(),
                    flowingGas.defaultFluidState().createLegacyBlock().setValue(FlowingGas.DENSITY, amount * FlowingGas.MAX_DENSITY / 1000) , 3);
            int overflowAmount = amount - amount * FlowingGas.MAX_DENSITY / 1000 * 1000 / FlowingGas.MAX_DENSITY;
            FluidStack remainingStack = new FluidStack(resource.getFluid(), overflowAmount);
            setFluid(remainingStack);
            return;
        }
        
        int currentAmount = this$0.getWorld().getFluidState(this$0.getOutputPos()).getAmount() * 1000 / FlowingGas.MAX_DENSITY;
        
        int remaining = 1000 - currentAmount;
        
        if (amount <= remaining) {
            this$0.getWorld().setBlock(this$0.getOutputPos(),
                    flowingGas.defaultFluidState().createLegacyBlock().setValue(FlowingGas.DENSITY, (currentAmount + amount) * FlowingGas.MAX_DENSITY / 1000) , 3);
            FluidStack remainingStack = new FluidStack(resource.getFluid(), (currentAmount + amount) - (currentAmount + amount) * FlowingGas.MAX_DENSITY / 1000 * 1000 / FlowingGas.MAX_DENSITY);
            setFluid(remainingStack);
        } else {
            this$0.getWorld().setBlock(this$0.getOutputPos(),
                    flowingGas.defaultFluidState().createLegacyBlock().setValue(FlowingGas.DENSITY, FlowingGas.MAX_DENSITY) , 3);
            
            int overflowAmount = amount - remaining;
            FluidStack remainingStack = new FluidStack(resource.getFluid(), overflowAmount);
            setFluid(remainingStack);
        }
        
    }
    
    @Redirect(method = "drainInner", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fluids/FluidStack;setAmount(I)V"))
    private void mixinDrainInner(FluidStack instance, int amount) {
        if (amount > 128)
            instance.setAmount(amount);
    }
}
