package dev.metallurgists.metallurgica.registry.material;

import dev.metallurgists.metallurgica.infastructure.material.registry.flags.block.StorageBlockFlag;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.item.DustFlag;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.item.IngotFlag;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.item.NuggetFlag;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.item.SheetFlag;
import dev.metallurgists.metallurgica.registry.misc.MetallurgicaElements;

import static dev.metallurgists.metallurgica.registry.material.MetMaterials.*;
import static dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey.*;


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
