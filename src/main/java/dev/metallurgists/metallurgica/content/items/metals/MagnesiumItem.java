package dev.metallurgists.metallurgica.content.items.metals;

import dev.metallurgists.metallurgica.foundation.item.ReactiveItem;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey;
import dev.metallurgists.metallurgica.infastructure.material.MaterialHelper;
import dev.metallurgists.metallurgica.registry.material.MetMaterials;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;

import java.util.Optional;

public class MagnesiumItem extends ReactiveItem {

    public MagnesiumItem(Properties pProperties) {
        super(pProperties);
    }

    public static MagnesiumItem createIngot(Properties pProperties) {
        return (MagnesiumItem) new MagnesiumItem(pProperties).sensitiveToAir(50).withResult(MaterialHelper.getItem(MetMaterials.MAGNESIUM_OXIDE.get(), FlagKey.INGOT));
    }



    @Override
    public void onReaction(ItemStack stack, Level world, Entity entity, Optional<Fluid> fluid, ReactionType type) {

    }
}
