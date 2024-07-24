package com.freezedown.metallurgica.foundation.item;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.config.MetallurgicaConfigs;
import net.minecraft.core.NonNullList;
import net.minecraft.locale.Language;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class AlloyItem extends MetallurgicaItem{
    private boolean hasImpurities;
    
    public AlloyItem(Properties pProperties) {
        super(pProperties);
    }
    
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        
        if (pStack.getOrCreateTag().contains("Impurities")) {
            pTooltipComponents.add(Component.translatable(Metallurgica.ID + ".tooltips.impurities"));
            ListTag impurities = pStack.getOrCreateTag().getList("Impurities", 10);
            for (int i = 0; i < impurities.size(); i++) {
                CompoundTag impurity = impurities.getCompound(i);
                for (String key : impurity.getAllKeys()) {
                    pTooltipComponents.add(Component.literal(" > ").append(getImpurityName(key)).append(Component.literal(": " + impurityPercentage(impurity.getFloat(key)) + "%")));
                }
            }
        }
        
    }
    
    @Override
    public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemstack, world, entity, slot, selected);
        CompoundTag tag = itemstack.getOrCreateTag();
        ListTag impurities = tag.getList("Impurities", 10);
        for (int i = 0; i < impurities.size(); i++) {
            CompoundTag impurity = impurities.getCompound(i);
            for (String key : impurity.getAllKeys()) {
                if (impurity.getFloat(key) <= 0) {
                    impurities.remove(i);
                }
            }
        }
        for (int i = 0; i <= impurities.size(); i++) {
            CompoundTag impurity = impurities.getCompound(i);
            for (String key : impurity.getAllKeys()) {
                if (impurity.getFloat(key) <= 0) {
                    impurities.remove(i);
                }
            }
        }
    }
    
    public Component getItemName(ItemStack itemStack) {
        Component impure = Component.translatable(Metallurgica.ID + ".item_appendages.impure");
        if (MetallurgicaConfigs.client().appendTextToItems.get()) {
            if (itemStack.getOrCreateTag().contains("Impurities")) {
                return impure.copy().append(" ").append(Component.translatable(this.getDescriptionId(itemStack)));
            } else {
                return Component.translatable(this.getDescriptionId(itemStack));
            }
        }
        return Component.translatable(this.getDescriptionId(itemStack));
    }
    
    public int impurityPercentage(float impurity) {
        return (int) (impurity * 100);
    }
    
    public String getImpurityDescription(String impurity) {
        return Metallurgica.ID + ".impurity." + impurity;
    }
    
    public Component getImpurityName(String impurity) {
        Language language = Language.getInstance();
        if (!language.has(getImpurityDescription(impurity))) {
            return Component.translatable(Metallurgica.ID + ".impurity.unknown", impurity);
        }
        return Component.translatable(getImpurityDescription(impurity));
    }
    
    @Override
    public Component getName(ItemStack stack) {
        return getItemName(stack);
    }
    
    public ItemStack addImpurity(ItemStack itemStack, String impurity, float percentage) {
        CompoundTag tag = itemStack.getOrCreateTag();
        ListTag impurities = tag.getList("Impurities", 8);
        CompoundTag impurityTag = new CompoundTag();
        impurityTag.putFloat(impurity, percentage);
        impurities.add(impurityTag);
        tag.put("Impurities", impurities);
        return itemStack;
    }
}
