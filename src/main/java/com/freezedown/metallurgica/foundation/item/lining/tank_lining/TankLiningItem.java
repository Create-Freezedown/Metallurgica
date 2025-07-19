package com.freezedown.metallurgica.foundation.item.lining.tank_lining;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class TankLiningItem extends Item {

    public TankLiningStats defaultStats;

    public TankLiningItem(Properties properties, TankLiningStats defaultStats) {
        super(properties);
        this.defaultStats = defaultStats;
    }

    public TankLiningStats getStats(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.contains("liningStats") ? TankLiningStats.readFromNBT(tag) : defaultStats;
    }

    public void setStats(ItemStack stack, TankLiningStats stats) {
        CompoundTag tag = stack.getOrCreateTag();
        stats.writeToNBT(tag);
        stack.setTag(tag);
    }

    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        setStats(stack, getStats(stack));
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        getStats(stack).addToTooltip(tooltipComponents);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(13.0F - getStats(stack).getDamage() * 13.0F / getStats(stack).getMaxDurability());
    }

    @Override
    public int getBarColor(ItemStack stack) {
        float stackMaxDamage = (float)getStats(stack).getMaxDurability();
        float f = Math.max(0.0F, (stackMaxDamage - getStats(stack).getDamage()) / stackMaxDamage);
        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }
}
