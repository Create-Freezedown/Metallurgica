package com.freezedown.metallurgica.foundation.client;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.data.custom.composition.Element;
import com.freezedown.metallurgica.foundation.data.custom.composition.fluid.ClientFluidCompositions;
import com.freezedown.metallurgica.foundation.data.custom.composition.fluid.FluidCompositionManager;
import com.freezedown.metallurgica.foundation.util.ClientUtil;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;

import java.util.Objects;
import java.util.function.Consumer;

public class TooltipHelper {

    public static void appendFluidTooltips(FluidStack fluidStack, Consumer<Component> tooltips) {
        Fluid fluid = fluidStack.getFluid();
        int amount = fluidStack.getAmount();
        FluidType fluidType = fluid.getFluidType();

        MutableComponent blank = Component.literal("");
        if (ClientFluidCompositions.getInstance().hasComposition(fluidStack)) {
            Metallurgica.LOGGER.info("Adding fluid composition tooltip for fluid: {}, with elements: {}", fluidStack.getDisplayName().getString(), FluidCompositionManager.getElements(fluidStack));
            StringBuilder compositionName = new StringBuilder();
            int size = ClientFluidCompositions.getInstance().getElements(fluidStack).size();
            for (int i = 0; i < size; i++) {
                Element element = Objects.requireNonNull(ClientFluidCompositions.getInstance().getElements(fluidStack).get(i));
                Element nextElement = i + 1 < size ? Objects.requireNonNull(ClientFluidCompositions.getInstance().getElements(fluidStack).get(i + 1)) : null;
                Element previousElement = i - 1 >= 0 ? Objects.requireNonNull(ClientFluidCompositions.getInstance().getElements(fluidStack).get(i - 1)) : null;
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
                tooltips.accept(Component.literal(compositionName.toString()).withStyle(Metallurgica.METALLURGICA_PALETTE.primary()));
            }
        }
    }
}
