package com.freezedown.metallurgica.foundation.item.registry.flags;

import com.freezedown.metallurgica.foundation.item.registry.flags.base.MaterialFlags;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.NoRegisterFactoryFlag;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import lombok.Getter;
import net.minecraft.world.item.Item;

public class IngotFlag extends NoRegisterFactoryFlag<Item> {
    @Getter
    private boolean requiresCompressing;

    public IngotFlag() {
    }

    public IngotFlag(boolean noRegister) {
        setNoRegister(noRegister);
    }

    public IngotFlag requiresCompressing(boolean requiresCompressing) {
        this.requiresCompressing = requiresCompressing;
        return  this;
    }

    public IngotFlag factory(NonNullFunction<Item.Properties, ? extends Item> factory) {
        setFactory(factory);
        return this;
    }



    @Override
    public void verifyFlag(MaterialFlags flags) {

    }
}
