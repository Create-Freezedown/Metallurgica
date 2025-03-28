package com.freezedown.metallurgica.foundation.item.registry.flags;

import com.freezedown.metallurgica.foundation.item.registry.flags.base.IMaterialFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.ItemFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.MaterialFlags;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.world.item.Item;

public class GemFlag extends ItemFlag {

    public GemFlag() {
        super(Item::new, "%s_gem");
    }

    public GemFlag(NonNullFunction<Item.Properties, ? extends Item> factory) {
        super(factory, "%s_gem");
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }
}
