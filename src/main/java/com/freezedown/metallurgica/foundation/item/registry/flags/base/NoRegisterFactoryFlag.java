package com.freezedown.metallurgica.foundation.item.registry.flags.base;

import com.tterrag.registrate.util.nullness.NonNullFunction;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

public class NoRegisterFactoryFlag<T> implements IMaterialFlag {
    @Getter
    @Setter
    boolean noRegister = false;

    @Getter
    @Setter
    @Nullable NonNullFunction<Item.Properties, ? extends Item> factory;

    public boolean hasFactory() {
        return getFactory() != null;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }
}
