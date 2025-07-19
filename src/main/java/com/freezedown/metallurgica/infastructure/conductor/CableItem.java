package com.freezedown.metallurgica.infastructure.conductor;

import com.freezedown.metallurgica.foundation.material.item.IMaterialItem;
import com.freezedown.metallurgica.infastructure.material.Material;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.ItemFlag;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.interfaces.IItemRegistry;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CableItem extends Item implements IMaterialItem {
    public ConductorEntry<?> conductorEntry;
    public final Material material;
    public final IItemRegistry itemFlag;

    public CableItem(Properties properties, ConductorEntry<?> conductorEntry, Material material, IItemRegistry itemFlag) {
        super(properties);
        this.conductorEntry = conductorEntry;
        this.material = material;
        this.itemFlag = itemFlag;
    }

    @Override
    public String getDescriptionId() {
        return itemFlag.getUnlocalizedName(material);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        return getDescriptionId();
    }

    @Override
    public MutableComponent getDescription() {
        return itemFlag.getLocalizedName(material);
    }

    @Override
    public MutableComponent getName(ItemStack stack) {
        return getDescription();
    }

    @Override
    public Material getMaterial() {
        return this.material;
    }

    @Override
    public IItemRegistry getFlag() {
        return this.itemFlag;
    }
}
