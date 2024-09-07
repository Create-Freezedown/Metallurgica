package com.freezedown.metallurgica.foundation.mixin.jei;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.data.custom.composition.Element;
import com.freezedown.metallurgica.foundation.data.custom.composition.fluid.ClientFluidCompositions;
import com.freezedown.metallurgica.foundation.data.custom.composition.fluid.FluidCompositionManager;
import com.freezedown.metallurgica.foundation.util.ClientUtil;
import mezz.jei.forge.platform.FluidHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;

@Mixin(FluidHelper.class)
public class FluidHelperMixin {
    
    @Inject(method = "getTooltip*", at = @At("TAIL"), remap = false)
    private void metallurgica$injectFluidTooltips(FluidStack ingredient, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir) {
        var tooltip = cir.getReturnValue();
        MutableComponent blank = Component.literal("");
        if (ClientFluidCompositions.getInstance().hasComposition(ingredient)) {
            Metallurgica.LOGGER.info("Adding fluid composition tooltip for fluid: {}, with elements: {}", ingredient.getDisplayName().getString(), FluidCompositionManager.getElements(ingredient));
            StringBuilder compositionName = new StringBuilder();
            int size = ClientFluidCompositions.getInstance().getElements(ingredient).size();
            for (int i = 0; i < size; i++) {
                Element element = Objects.requireNonNull(ClientFluidCompositions.getInstance().getElements(ingredient).get(i));
                Element nextElement = i + 1 < size ? Objects.requireNonNull(ClientFluidCompositions.getInstance().getElements(ingredient).get(i + 1)) : null;
                Element previousElement = i - 1 >= 0 ? Objects.requireNonNull(ClientFluidCompositions.getInstance().getElements(ingredient).get(i - 1)) : null;
                MutableComponent openBracket = element.bracketed() ? Component.literal("(") : blank;
                MutableComponent closeBracket = element.bracketed() ? Component.literal(")") : blank;
                int groupedAmount = element.getGroupedAmount();
                if (previousElement != null && previousElement.bracketed()) {
                    openBracket = previousElement.isBracketForceClosed() && element.bracketed() ? Component.literal("(") : blank;
                }
                if (nextElement != null && nextElement.bracketed()) {
                    groupedAmount = 1;
                    closeBracket = (element.isBracketForceClosed() ? Component.literal(")") : blank);
                }
                MutableComponent dash = element.hasDash() ? Component.literal("-") : blank;
                MutableComponent groupAmount = groupedAmount > 1 ? (element.areNumbersUp() ? Component.literal(ClientUtil.toSmallUpNumbers(String.valueOf(groupedAmount))) : Component.literal(ClientUtil.toSmallDownNumbers(String.valueOf(groupedAmount)))) : blank;
                compositionName.append(openBracket).append(element.getDisplay()).append(closeBracket).append(groupAmount).append(dash);
            }
            if (!compositionName.isEmpty()) {
                tooltip.add(2, ClientUtil.lang().space().space().space().space()
                        .text(compositionName.toString())
                        .component());
            }
        }
    }
}
