package dev.metallurgists.metallurgica.foundation.item.lining.tank_lining;

import com.simibubi.create.foundation.blockEntity.IMultiBlockEntityContainer;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

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

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        setStats(stack, getStats(stack));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        getStats(stack).addToTooltip(tooltipComponents);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        TankLiningStats stats = getStats(stack);
        if (stats != null) {
            BlockEntity blockEntity = context.getLevel().getBlockEntity(context.getClickedPos());
            if (blockEntity instanceof SmartBlockEntity smartBlockEntity) {
                if (smartBlockEntity instanceof IMultiBlockEntityContainer multiBlockEntityContainer) {
                    return stats.applyToBE(multiBlockEntityContainer.getControllerBE(), context.getPlayer());
                }
                return stats.applyToBE(smartBlockEntity, context.getPlayer());
            }
        }
        return super.useOn(context);
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
