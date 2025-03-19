package com.freezedown.metallurgica.foundation.item.registry.flags;

import com.freezedown.metallurgica.foundation.item.registry.flags.base.ItemFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.MaterialFlags;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.world.item.Item;

public class WireFlag extends ItemFlag {
    public WireFlag() {
        super(Item::new, "%s_wire");
    }

    public WireFlag(NonNullFunction<Item.Properties, ? extends Item> factory) {
        super(factory, "%s_wire");
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {
        flags.ensureSet(FlagKey.SHEET, true);
    }
}
