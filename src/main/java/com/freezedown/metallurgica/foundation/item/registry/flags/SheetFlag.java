package com.freezedown.metallurgica.foundation.item.registry.flags;

import com.freezedown.metallurgica.foundation.item.registry.flags.base.ItemFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.MaterialFlags;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import lombok.Getter;
import net.minecraft.world.item.Item;

public class SheetFlag extends ItemFlag {

    @Getter
    private int pressTimes = 1;

    @Getter
    public boolean needsTransitional = false;

    public SheetFlag() {
        super(Item::new, "%s_sheet");
    }

    public SheetFlag(NonNullFunction<Item.Properties, ? extends Item> factory) {
        super(factory, "%s_sheet");
    }

    public SheetFlag(String existingNamespace) {
        super("%s_sheet", existingNamespace);
    }

    public SheetFlag pressTimes(int pressTimes) {
        this.pressTimes = pressTimes;
        this.needsTransitional = pressTimes > 1;
        return this;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {
        if (needsTransitional) {
            flags.ensureSet(FlagKey.SEMI_PRESSED_SHEET, true);
        }
        flags.ensureSet(FlagKey.INGOT, true);
    }
}
