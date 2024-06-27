package com.freezedown.metallurgica.events;

import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.drmangotea.createindustry.registry.TFMGItems;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {
    List<Item> needsChemicalFormulaTooltip = List.of(TFMGItems.SULFUR_DUST.get(), TFMGBlocks.SULFUR.get().asItem(), TFMGItems.NITRATE_DUST.get(), TFMGItems.LIMESAND.get());
    
    @SubscribeEvent
    public void addTooltips(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        for (Item item : needsChemicalFormulaTooltip) {
            if (stack.getItem() == item) {
                event.getToolTip().add(Lang.translateDirect("tooltip.holdForDescription", Lang.translateDirect("tooltip.keyShift").withStyle(Screen.hasShiftDown() ? ChatFormatting.WHITE : ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
                if (Screen.hasShiftDown()) {
                    event.getToolTip().add(chemicalFormula(item));
                }
            }
        }
    }
    
    private MutableComponent chemicalFormula(Item item) {
        MutableComponent chemicalFormula = Component.translatable(item.getDescriptionId() + ".tooltip.chemical_formula");
        return Lang.builder()
                .space().space().space().space()
                .add(chemicalFormula)
                .color(TooltipHelper.Palette.BLUE.highlight().getColor().getValue())
                .component();
    }
}
