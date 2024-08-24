package com.freezedown.metallurgica.foundation.mixin;

import com.freezedown.metallurgica.foundation.item.composition.CompositionManager;
import com.freezedown.metallurgica.foundation.item.composition.Element;
import com.freezedown.metallurgica.foundation.util.ClientUtil;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.LangBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("deprecation")
@Mixin(Item.class)
public class ChemicalInfoTooltipMixin {
    
    @Inject(method = "appendHoverText", at = @At("HEAD"))
    private void vanillaItemTooltips(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag isAdvanced, CallbackInfo ci) {
        if (CompositionManager.hasComposition(stack.getItem())) {
            StringBuilder compositionName = new StringBuilder();
            int size = CompositionManager.getElements(stack.getItem()).size();
            for (int i = 0; i < size; i++) {
                Element element = Objects.requireNonNull(CompositionManager.getElements(stack.getItem()).get(i));
                Element nextElement = i + 1 < size ? Objects.requireNonNull(CompositionManager.getElements(stack.getItem()).get(i + 1)) : null;
                Element previousElement = i - 1 >= 0 ? Objects.requireNonNull(CompositionManager.getElements(stack.getItem()).get(i - 1)) : null;
                String openBracket = element.bracketed() ? "(" : "";
                String closeBracket = element.bracketed() ? ")" : "";
                if (previousElement != null && previousElement.bracketed()) {
                    openBracket = "";
                }
                if (nextElement != null && nextElement.bracketed()) {
                    closeBracket = "";
                }
                compositionName.append(openBracket).append(element.getDisplay()).append(closeBracket);
            }
            if (!compositionName.isEmpty()) {
                tooltip.add(ClientUtil.lang().space().space().space().space()
                        .text(compositionName.toString())
                        .color(TooltipHelper.Palette.BLUE.highlight().getColor().getValue())
                        .component());
            }
        }
    }
}
