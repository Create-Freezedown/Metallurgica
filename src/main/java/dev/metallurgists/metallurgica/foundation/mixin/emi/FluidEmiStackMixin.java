package dev.metallurgists.metallurgica.foundation.mixin.emi;

import dev.metallurgists.metallurgica.foundation.client.TooltipHelper;
import com.llamalad7.mixinextras.sugar.Local;
import dev.emi.emi.api.render.EmiTooltipComponents;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.FluidEmiStack;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = FluidEmiStack.class, remap = false)
public abstract class FluidEmiStackMixin {

    @Shadow
    @Final
    private Fluid fluid;
    @Shadow
    @Final
    private CompoundTag nbt;

    @Inject(method = "getTooltip",
            at = @At(value = "INVOKE", target = "Ldev/emi/emi/EmiPort;getFluidRegistry()Lnet/minecraft/core/Registry;"),
            remap = false,
            require = 0)
    private void metallurgica$addFluidTooltip(CallbackInfoReturnable<List<ClientTooltipComponent>> cir, @Local(ordinal = 0) List<ClientTooltipComponent> list) {
        TooltipHelper.appendFluidTooltips(new FluidStack(this.fluid, Math.max(saturatedCast(((EmiStack) (Object) this).getAmount()), 1), nbt), text -> list.add(EmiTooltipComponents.of(text)));
    }

    private static int saturatedCast(long value) {
        if (value > 2147483647L) {
            return Integer.MAX_VALUE;
        } else {
            return value < -2147483648L ? Integer.MIN_VALUE : (int) value;
        }
    }
}
