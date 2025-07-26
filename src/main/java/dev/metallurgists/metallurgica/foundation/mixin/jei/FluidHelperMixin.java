package dev.metallurgists.metallurgica.foundation.mixin.jei;

import dev.metallurgists.metallurgica.foundation.client.TooltipHelper;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.forge.platform.FluidHelper;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FluidHelper.class)
public abstract class FluidHelperMixin {

    @Inject(method = "getTooltip(Lmezz/jei/api/gui/builder/ITooltipBuilder;Lnet/minecraftforge/fluids/FluidStack;Lnet/minecraft/world/item/TooltipFlag;)V",
            at = @At("TAIL"),
            remap = false,
            require = 0)
    private void metallurgica$injectFluidTooltips(ITooltipBuilder tooltip, FluidStack ingredient, TooltipFlag tooltipFlag, CallbackInfo ci) {
        TooltipHelper.appendFluidTooltips(ingredient, tooltip::add);
    }
}
