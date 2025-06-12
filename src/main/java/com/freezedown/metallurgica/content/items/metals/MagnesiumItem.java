package com.freezedown.metallurgica.content.items.metals;

import com.freezedown.metallurgica.foundation.item.ReactiveItem;
import com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey;
import com.freezedown.metallurgica.foundation.material.MaterialHelper;
import com.freezedown.metallurgica.registry.MetallurgicaItems;
import com.freezedown.metallurgica.registry.material.MetMaterials;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;

import java.util.Optional;
import java.util.function.Supplier;

public class MagnesiumItem extends ReactiveItem {

    public MagnesiumItem(Properties pProperties) {
        super(pProperties);
    }

    public static MagnesiumItem createIngot(Properties pProperties) {
        return (MagnesiumItem) new MagnesiumItem(pProperties).sensitiveToAir(50).withResult(MaterialHelper.getItem(MetMaterials.MAGNESIUM_OXIDE, FlagKey.INGOT));
    }



    @Override
    public void onReaction(ItemStack stack, Level world, Entity entity, Optional<Fluid> fluid, ReactionType type) {

    }
}
