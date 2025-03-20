package com.freezedown.metallurgica.foundation.mixin;

import com.freezedown.metallurgica.foundation.data.custom.composition.tooltip.CompositionManager;
import com.freezedown.metallurgica.foundation.data.custom.composition.Element;
import com.freezedown.metallurgica.foundation.util.ClientUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;

@Mixin(Item.class)
public class ChemicalInfoTooltipMixin {
    
    @Inject(method = "appendHoverText", at = @At("HEAD"))
    private void vanillaItemTooltips(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag isAdvanced, CallbackInfo ci) {
        String blank = "";
        if (CompositionManager.hasComposition(stack.getItem())) {
            StringBuilder compositionName = new StringBuilder();
            int size = CompositionManager.getElements(stack.getItem()).size();
            for (int i = 0; i < size; i++) {
                Element element = Objects.requireNonNull(CompositionManager.getElements(stack.getItem()).get(i));
                Element nextElement = i + 1 < size ? Objects.requireNonNull(CompositionManager.getElements(stack.getItem()).get(i + 1)) : null;
                Element previousElement = i - 1 >= 0 ? Objects.requireNonNull(CompositionManager.getElements(stack.getItem()).get(i - 1)) : null;
                String openBracket = element.bracketed() ? "(" : blank;
                String closeBracket = element.bracketed() ? ")" : blank;
                int groupedAmount = element.getGroupedAmount();
                if (previousElement != null && previousElement.bracketed()) {
                    openBracket = previousElement.isBracketForceClosed() && element.bracketed() ? "(" : blank;
                }
                if (nextElement != null && nextElement.bracketed()) {
                    groupedAmount = 1;
                    closeBracket = (element.isBracketForceClosed() ? ")" : blank);
                }
                String dash = element.hasDash() ? "-" : blank;
                String groupAmount = groupedAmount > 1 ? element.numbersUp() ? ClientUtil.toSmallUpNumbers(String.valueOf(groupedAmount)) : ClientUtil.toSmallDownNumbers(String.valueOf(groupedAmount)) : blank;
                compositionName.append(openBracket).append(element.getDisplay()).append(closeBracket).append(groupAmount).append(dash);
            }
            if (!compositionName.isEmpty()) {
                tooltip.add(ClientUtil.lang().space().space().space().space()
                        .text(compositionName.toString())
                        .component());
            }
        }
    }
}
