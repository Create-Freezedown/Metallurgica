package com.freezedown.metallurgica.foundation.item.registry.flags;

import com.freezedown.metallurgica.foundation.item.registry.flags.base.ItemFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.MaterialFlags;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import net.minecraft.world.item.Item;

public class SemiPressedSheetFlag extends ItemFlag {

    public SemiPressedSheetFlag() {
        super(SequencedAssemblyItem::new, "semi_pressed_%s_sheet");
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {
        flags.ensureSet(FlagKey.SHEET, true);
    }
}
