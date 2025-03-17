package com.freezedown.metallurgica.foundation.item.registry.flags;

import com.freezedown.metallurgica.foundation.item.registry.flags.base.MaterialFlags;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.NoRegisterFactoryFlag;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import lombok.Getter;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class SheetFlag extends NoRegisterFactoryFlag<Item> {

    @Getter
    private int pressTimes;

    @Getter
    public boolean needsTransitional = false;

    public SheetFlag(int pressTimes) {
        this.pressTimes = pressTimes;
        this.needsTransitional = pressTimes > 1;
    }

    public SheetFlag(int pressTimes, boolean noRegister) {
        this.pressTimes = pressTimes;
        this.needsTransitional = pressTimes > 1;
        setNoRegister(noRegister);
    }

    public SheetFlag factory(NonNullFunction<Item.Properties, ? extends Item> factory) {
        setFactory(factory);
        return this;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {
        flags.ensureSet(FlagKey.INGOT, false);
    }
}
