package com.freezedown.metallurgica.foundation.mixin;

import com.freezedown.metallurgica.foundation.data.custom.composition.data.SubComposition;
import com.freezedown.metallurgica.foundation.data.custom.composition.tooltip.CompositionManager;
import com.freezedown.metallurgica.foundation.util.ClientUtil;
import net.createmod.catnip.lang.LangBuilder;
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
        if (CompositionManager.hasComposition(stack.getItem())) {
            LangBuilder compositionName = ClientUtil.lang();
            int size = CompositionManager.getSubCompositions(stack.getItem()).size();
            for (int i = 0; i < size; i++) {
                if (CompositionManager.getSubCompositions(stack.getItem()).get(i) == null) continue;
                LangBuilder subComp = ClientUtil.lang();
                SubComposition subComposition = Objects.requireNonNull(CompositionManager.getSubCompositions(stack.getItem()).get(i));
                int elementsSize = subComposition.getElements().size();
                if (elementsSize > 1) {
                    subComp.add(Component.literal("("));
                    for (int j = 0; j < elementsSize; j++) {
                        if (subComposition.getElements().get(j) == null) continue;
                        subComp.add(Component.literal(subComposition.getElement(j).getDisplay()));
                    }
                    subComp.add(Component.literal(")"));
                } else {
                    subComp.add(Component.literal(subComposition.getElement(0).getDisplay()));
                }
                compositionName.add(subComp);
            }
            if (!compositionName.string().isEmpty()) {
                tooltip.add(ClientUtil.lang().space().space().space()
                        .add(compositionName)
                        .component());
            }
        }
    }
}
