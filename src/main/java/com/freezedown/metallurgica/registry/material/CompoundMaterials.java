package com.freezedown.metallurgica.registry.material;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.material.registry.Material;
import com.freezedown.metallurgica.foundation.material.registry.flags.block.StorageBlockFlag;
import com.freezedown.metallurgica.foundation.material.registry.flags.item.DustFlag;
import com.freezedown.metallurgica.foundation.material.registry.flags.item.IngotFlag;
import com.freezedown.metallurgica.foundation.material.registry.flags.item.NuggetFlag;
import com.freezedown.metallurgica.foundation.material.registry.flags.item.SheetFlag;
import com.freezedown.metallurgica.registry.misc.MetallurgicaElements;

import static com.freezedown.metallurgica.foundation.material.registry.flags.FlagKey.*;
import static com.freezedown.metallurgica.registry.material.MetMaterials.*;

public class CompoundMaterials {
    public static void register() {
        MAGNESIUM_OXIDE = createMaterial("magnesium_oxide", (b) -> b
                .composition(MetallurgicaElements.MAGNESIUM, 1, MetallurgicaElements.OXYGEN, 1)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new DustFlag()
                ));
        POTASSIUM_NITRATE = createMaterial("potassium_nitrate", (b) -> b
                .composition(MetallurgicaElements.POTASSIUM, 1, MetallurgicaElements.NITROGEN, 1, MetallurgicaElements.OXYGEN, 3)
                .noRegister(DUST)
                .existingIds(DUST, "tfmg:nitrate_dust")
                .addFlags(
                        new DustFlag()
                ));
        CALCIUM_CARBONATE = createMaterial("calcium_carbonate", (b) -> b
                .composition(MetallurgicaElements.CALCIUM, 1, MetallurgicaElements.CARBON, 1, MetallurgicaElements.OXYGEN, 3)
                .noRegister(DUST)
                .existingIds(DUST, "tfmg:limesand")
                .addFlags(
                        new DustFlag()
                ));
    }
}
