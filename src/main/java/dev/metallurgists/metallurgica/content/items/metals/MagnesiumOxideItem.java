package dev.metallurgists.metallurgica.content.items.metals;

import dev.metallurgists.metallurgica.foundation.item.ReactiveItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;

import java.util.Optional;

public class MagnesiumOxideItem extends ReactiveItem {
    public MagnesiumOxideItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void onReaction(ItemStack stack, Level world, Entity entity, Optional<Fluid> fluid, ReactionType type) {

    }
}
