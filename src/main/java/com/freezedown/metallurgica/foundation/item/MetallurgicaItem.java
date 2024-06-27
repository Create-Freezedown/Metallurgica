package com.freezedown.metallurgica.foundation.item;

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

import javax.annotation.Nullable;
import java.util.List;

public class MetallurgicaItem extends Item {
    public boolean showElementComposition;
    public boolean showPun;
    public MetallurgicaItem(Properties pProperties) {
        super(pProperties);
        this.showElementComposition = false;
        this.showPun = false;
    }
    public MetallurgicaItem showElementComposition() {
        showElementComposition = true;
        return this;
    }
    
    public MetallurgicaItem showPun() {
        showPun = true;
        return this;
    }
    
    
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Lang.translateDirect("tooltip.holdForDescription", Lang.translateDirect("tooltip.keyShift").withStyle(Screen.hasShiftDown() ? ChatFormatting.WHITE : ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
        if (Screen.hasShiftDown()) {
            if (this.showElementComposition) {
                pTooltipComponents.add(chemicalFormula());
            }
            if (this.showPun) {
                pTooltipComponents.add(pun());
            }
        }
    }
    
    private MutableComponent chemicalFormula() {
        MutableComponent chemicalFormula = Component.translatable(this.getDescriptionId() + ".tooltip.chemical_formula");
        return Lang.builder()
                .space().space().space().space()
                .add(chemicalFormula)
                .color(TooltipHelper.Palette.BLUE.highlight().getColor().getValue())
                .component();
    }
    
    private MutableComponent pun() {
        MutableComponent pun = Component.translatable(this.getDescriptionId() + ".tooltip.pun");
        return Lang.builder()
                .space().space()
                .add(pun)
                .color(TooltipHelper.Palette.STANDARD_CREATE.highlight().getColor().getValue())
                .component();
    }
    
}
