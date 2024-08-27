package com.freezedown.metallurgica.foundation.mixin.jei;

import com.freezedown.metallurgica.foundation.util.TooltipHelpers;
import mezz.jei.forge.platform.FluidHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(FluidHelper.class)
public class FluidHelperMixin {
    
    @Inject(method = "getTooltip*", at = @At("TAIL"), remap = false)
    private void metallurgica$injectFluidTooltips(FluidStack ingredient, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir) {
        var tooltip = cir.getReturnValue();
        TooltipHelpers.addFluidChemicalCompositionTooltip(ingredient, tooltip, tooltipFlag);
    }
}
