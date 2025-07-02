package com.freezedown.metallurgica.foundation.material.item;

import com.freezedown.metallurgica.infastructure.material.Material;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.ItemFlag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class MaterialItem extends Item implements IMaterialItem {
    public final Material material;
    public final ItemFlag itemFlag;

    public MaterialItem(Properties properties, Material material, ItemFlag itemFlag) {
        super(properties);
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
    public ItemFlag getFlag() {
        return this.itemFlag;
    }
}
