package com.freezedown.metallurgica.registry.material;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.block.StorageBlockFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.item.DustFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.item.IngotFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.item.NuggetFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.item.SheetFlag;
import com.freezedown.metallurgica.registry.misc.MetallurgicaElements;

import static com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey.*;
import static com.freezedown.metallurgica.registry.material.MetMaterials.*;

public class CompoundMaterials {
    public static void register() {
        MAGNESIUM_OXIDE = new Material.Builder(Metallurgica.asResource("magnesium_oxide"))
                .composition(MetallurgicaElements.MAGNESIUM, 1, MetallurgicaElements.OXYGEN, 1)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new DustFlag()
                ).buildAndRegister();
        POTASSIUM_NITRATE = new Material.Builder(Metallurgica.asResource("potassium_nitrate"))
                .composition(MetallurgicaElements.POTASSIUM, 1, MetallurgicaElements.NITROGEN, 1, MetallurgicaElements.OXYGEN, 3)
                .noRegister(DUST)
                .existingIds(DUST, "tfmg:nitrate_dust")
                .addFlags(
                        new DustFlag()
                ).buildAndRegister();
        CALCIUM_CARBONATE = new Material.Builder(Metallurgica.asResource("calcium_carbonate"))
                .composition(MetallurgicaElements.CALCIUM, 1, MetallurgicaElements.CARBON, 1, MetallurgicaElements.OXYGEN, 3)
                .noRegister(DUST)
                .existingIds(DUST, "tfmg:limesand")
                .addFlags(
                        new DustFlag()
                ).buildAndRegister();
    }
}
