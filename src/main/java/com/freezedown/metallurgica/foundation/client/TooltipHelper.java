package com.freezedown.metallurgica.foundation.client;

import com.freezedown.metallurgica.foundation.data.custom.composition.data.SubComposition;
import com.freezedown.metallurgica.foundation.data.custom.composition.fluid.ClientFluidCompositions;
import com.freezedown.metallurgica.foundation.util.ClientUtil;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.network.chat.Component;
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

        if (ClientFluidCompositions.getInstance().hasComposition(fluidStack)) {
            LangBuilder compositionName = ClientUtil.lang();
            int size = ClientFluidCompositions.getInstance().getSubCompositions(fluidStack).size();
            for (int i = 0; i < size; i++) {
                if (ClientFluidCompositions.getInstance().getSubCompositions(fluidStack).get(i) == null) continue;
                LangBuilder subComp = ClientUtil.lang();
                SubComposition subComposition = Objects.requireNonNull(ClientFluidCompositions.getInstance().getSubCompositions(fluidStack).get(i));
                int elementsSize = subComposition.getElements().size();
                if (elementsSize > 0) {
                    subComp.add(subComp.text("("));
                    for (int j = 0; j < elementsSize; j++) {
                        if (subComposition.getElements().get(j) == null) continue;
                        subComp.add(Component.literal(subComposition.getElement(j).getDisplay()));
                    }
                    subComp.add(subComp.text(")"));
                } else {
                    subComp.add(Component.literal(subComposition.getElement(0).getDisplay()));
                }
                compositionName.add(subComp);
            }
            if (!compositionName.string().isEmpty()) {
                tooltips.accept(ClientUtil.lang().space().space().space()
                        .add(compositionName)
                        .component());
            }
        }
    }
}
