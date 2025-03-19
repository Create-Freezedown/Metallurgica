package com.freezedown.metallurgica.foundation.item.registry.flags;

import com.freezedown.metallurgica.foundation.item.registry.flags.base.ItemFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.MaterialFlags;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.NoRegisterFactoryFlag;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import lombok.Getter;
import net.minecraft.world.item.Item;

public class IngotFlag extends ItemFlag {
    @Getter
    private boolean requiresCompressing;

    public IngotFlag() {
        super(Item::new, "%s_ingot");
    }

    public IngotFlag(NonNullFunction<Item.Properties, ? extends Item> factory) {
        super(factory, "%s_ingot");
    }

    public IngotFlag requiresCompressing(boolean requiresCompressing) {
        this.requiresCompressing = requiresCompressing;
        return  this;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }
}
