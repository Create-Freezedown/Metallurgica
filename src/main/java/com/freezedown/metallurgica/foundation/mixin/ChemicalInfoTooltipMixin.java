package com.freezedown.metallurgica.foundation.mixin;

import com.freezedown.metallurgica.registry.MetallurgicaTags;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
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

@Mixin(Item.class)
public class ChemicalInfoTooltipMixin {
    
    @Inject(method = "appendHoverText", at = @At("HEAD"))
    private void vanillaItemTooltips(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag isAdvanced, CallbackInfo ci) {
        if (stack.is(MetallurgicaTags.AllItemTags.NEEDS_CHEMICAL_FORMULA_TOOLTIP.tag)) {
            tooltip.add(Lang.translateDirect("tooltip.holdForDescription", Lang.translateDirect("tooltip.keyShift").withStyle(Screen.hasShiftDown() ? ChatFormatting.WHITE : ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
            if (Screen.hasShiftDown()) {
                tooltip.add(metallurgica$chemicalFormula(stack.getItem()));
            }
        }
    }
    
    @Unique
    private MutableComponent metallurgica$chemicalFormula(Item item) {
        MutableComponent chemicalFormula = Component.translatable(item.getDescriptionId() + ".tooltip.chemical_formula");
        return Lang.builder()
                .space().space().space().space()
                .add(chemicalFormula)
                .color(TooltipHelper.Palette.BLUE.highlight().getColor().getValue())
                .component();
    }
}
