package com.freezedown.metallurgica.foundation.item.registry.flags.base;

import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.world.item.Item;

public class ItemFlag implements IMaterialFlag {

    private final NonNullFunction<Item.Properties, ? extends Item> factory;
    private final String idPattern;

    public ItemFlag(NonNullFunction<Item.Properties, ? extends Item> factory, String idPattern) {
        this.factory = factory;
        this.idPattern = idPattern;
    }

    public NonNullFunction<Item.Properties, ? extends Item> getFactory() {
        return factory;
    }

    public String getIdPattern() {
        return idPattern;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }
}
