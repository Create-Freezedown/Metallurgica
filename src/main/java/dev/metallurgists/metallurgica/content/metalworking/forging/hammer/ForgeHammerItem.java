package dev.metallurgists.metallurgica.content.metalworking.forging.hammer;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.foundation.util.ClientUtil;
import dev.metallurgists.metallurgica.foundation.util.MetalLang;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ForgeHammerItem extends Item {

    public ForgeHammerItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemstack, world, entity, slot, selected);
        CompoundTag tag = itemstack.getOrCreateTag();
        if (!tag.contains("HammerMode")) {
            Metallurgica.LOGGER.error("HammerMode is missing from hammer");
            tag.putString("HammerMode", RadialHammerMenu.HammerMode.UPSET_DOWN.getSerializedName());
        }
    }

    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

        if (pStack.getOrCreateTag().contains("HammerMode")) {
            pTooltipComponents.add(MetalLang.translate("tooltip.selected_mode").component());
            pTooltipComponents.add(
                    ClientUtil.lang().space()
                    .add(RadialHammerMenu.HammerMode.get(pStack.getOrCreateTag().getString("HammerMode")).getTranslatedName())
                            .component()
            );
        }

    }
}
