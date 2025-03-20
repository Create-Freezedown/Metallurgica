package com.freezedown.metallurgica.foundation.util;

import com.freezedown.metallurgica.foundation.data.custom.composition.tooltip.CompositionManager;
import com.freezedown.metallurgica.foundation.data.custom.composition.Element;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.Objects;

public class TooltipHelpers {

    public void addItemChemicalCompositionTooltip(Item item, List<Component> tooltip) {
        MutableComponent blank = Component.literal("");
        if (CompositionManager.hasComposition(item)) {
            LangBuilder compositionName = ClientUtil.lang();
            int size = CompositionManager.getElements(item).size();
            for (int i = 0; i < size; i++) {
                Element element = Objects.requireNonNull(CompositionManager.getElements(item).get(i));
                Element nextElement = i + 1 < size ? Objects.requireNonNull(CompositionManager.getElements(item).get(i + 1)) : null;
                Element previousElement = i - 1 >= 0 ? Objects.requireNonNull(CompositionManager.getElements(item).get(i - 1)) : null;
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
                MutableComponent groupAmount = groupedAmount > 1 ? (element.numbersUp() ? Component.literal(ClientUtil.toSmallUpNumbers(String.valueOf(groupedAmount))) : Component.literal(ClientUtil.toSmallDownNumbers(String.valueOf(groupedAmount)))) : blank;
                compositionName.add(openBracket).add(Component.literal(element.getDisplay())).add(closeBracket).add(groupAmount).add(dash);
            }
            if (!compositionName.string().isEmpty()) {
                tooltip.add(ClientUtil.lang().space().space().space().space()
                        .add(compositionName)
                        .component());
            }
        }
    }
    
    //public static void addFluidChemicalCompositionTooltip(FluidStack fluidStack, List<Component> tooltips, TooltipFlag flag) {
    //    if (FluidCompositionManager.hasComposition(fluidStack)) {
    //        Metallurgica.LOGGER.info("Adding fluid composition tooltip for fluid: {}, with elements: {}", fluidStack.getDisplayName().getString(), FluidCompositionManager.getElements(fluidStack));
    //        StringBuilder compositionName = new StringBuilder();
    //        int size = FluidCompositionManager.getElements(fluidStack).size();
    //        for (int i = 0; i < size; i++) {
    //            Element element = Objects.requireNonNull(FluidCompositionManager.getElements(fluidStack).get(i));
    //            Element nextElement = i + 1 < size ? Objects.requireNonNull(FluidCompositionManager.getElements(fluidStack).get(i + 1)) : null;
    //            Element previousElement = i - 1 >= 0 ? Objects.requireNonNull(FluidCompositionManager.getElements(fluidStack).get(i - 1)) : null;
    //            String openBracket = element.bracketed() ? "(" : "";
    //            String closeBracket = element.bracketed() ? ")" : "";
    //            int groupedAmount = element.getGroupedAmount();
    //            if (previousElement != null && previousElement.bracketed()) {
    //                openBracket = previousElement.isBracketForceClosed() && element.bracketed() ? "(" : "";
    //            }
    //            if (nextElement != null && nextElement.bracketed()) {
    //                groupedAmount = 1;
    //                closeBracket = (element.isBracketForceClosed() ? ")" : "");
    //            }
    //            String dash = element.hasDash() ? "-" : "";
    //            String groupAmount = groupedAmount > 1 ? (element.numbersUp() ? ClientUtil.toSmallUpNumbers(String.valueOf(groupedAmount)) : ClientUtil.toSmallDownNumbers(String.valueOf(groupedAmount))) : "";
    //            compositionName.append(openBracket).append(element.getDisplay()).append(closeBracket).append(groupAmount).append(dash);
    //        }
    //        if (!compositionName.isEmpty()) {
    //            tooltips.add(1, ClientUtil.lang().space().space().space().space()
    //                    .text(compositionName.toString())
    //                    .color(TooltipHelper.Palette.BLUE.highlight().getColor().getValue())
    //                    .component());
    //        }
    //    } else {
    //        Metallurgica.LOGGER.info("No fluid composition found for fluid: {}", fluidStack.getDisplayName().getString());
    //    }
    //}
}
