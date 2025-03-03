package com.freezedown.metallurgica.foundation.mixin.emi;

import com.freezedown.metallurgica.foundation.client.TooltipHelper;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.FluidEmiStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(FluidEmiStack.class)
public abstract class FluidHelperMixin extends EmiStack {

    @Shadow
    @Final
    private Fluid fluid;
    @Shadow
    @Final
    private CompoundTag nbt;


    @Inject(method = "getTooltipText",
            at = @At("TAIL"),
            remap = false,
            require = 0)
    private void metallurgica$getTooltipText(CallbackInfoReturnable<List<Component>> cir) {
        List<Component> tooltip = cir.getReturnValue();
        FluidStack ingredient = new FluidStack(fluid, 1, nbt);
        TooltipHelper.appendFluidTooltips(ingredient, tooltip::add);
    }
}
