package com.freezedown.metallurgica.registry.material;

import com.freezedown.metallurgica.infastructure.material.registry.flags.block.SheetmetalFlag;
import com.freezedown.metallurgica.infastructure.material.registry.flags.block.StorageBlockFlag;
import com.freezedown.metallurgica.infastructure.material.registry.flags.fluid.MoltenFlag;
import com.freezedown.metallurgica.infastructure.material.registry.flags.item.DustFlag;
import com.freezedown.metallurgica.infastructure.material.registry.flags.item.IngotFlag;
import com.freezedown.metallurgica.infastructure.material.registry.flags.item.NuggetFlag;
import com.freezedown.metallurgica.infastructure.material.registry.flags.item.SheetFlag;
import com.freezedown.metallurgica.registry.misc.MetallurgicaElements;

import static com.freezedown.metallurgica.registry.material.MetMaterials.*;
import static com.freezedown.metallurgica.infastructure.material.registry.flags.FlagKey.*;


public class AlloyMaterials {
    public static void register() {
        TITANIUM_ALUMINIDE = createMaterial("titanium_aluminide", (b) -> b
                .composition(MetallurgicaElements.TITANIUM, 2, MetallurgicaElements.ALUMINUM, 1)
                .addFlags(
                        new NuggetFlag().requiresCompacting(),
                        new IngotFlag().requiresCompacting(),
                        new StorageBlockFlag().requiresDecompacting(),
                        new SheetFlag().pressTimes(3),
                        new MoltenFlag(1447.0),
                        new DustFlag(),
                        new SheetmetalFlag().requiresCompacting()
                ));
        NETHERITE = createMaterial("netherite", (b) -> b
                .composition(MetallurgicaElements.NETHERIUM, 1, MetallurgicaElements.GOLD, 1)
                .noRegister(INGOT, STORAGE_BLOCK)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag("minecraft"),
                        new StorageBlockFlag("minecraft"),
                        new SheetFlag().pressTimes(2),
                        new MoltenFlag(3562.0)
                ));
        BRASS = createMaterial("brass", (b) -> b
                .composition(MetallurgicaElements.COPPER, 3, MetallurgicaElements.ZINC, 1)
                .noRegister(NUGGET, INGOT, STORAGE_BLOCK, SHEET)
                .addFlags(
                        new NuggetFlag("create"),
                        new IngotFlag("create"),
                        new StorageBlockFlag("create"),
                        new SheetFlag("create"),
                        new MoltenFlag(920.0),
                        new DustFlag()
                ));
        BRONZE = createMaterial("bronze", (b) -> b
                .composition(MetallurgicaElements.COPPER, 7, MetallurgicaElements.TIN, 2)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new MoltenFlag(950.0),
                        new DustFlag()
                ));
        ARSENICAL_BRONZE = createMaterial("arsenical_bronze", (b) -> b
                .composition(MetallurgicaElements.COPPER, 4, MetallurgicaElements.TIN, 1, MetallurgicaElements.ARSENIC, 3)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new MoltenFlag(685.0),
                        new DustFlag()
                ));
        WROUGHT_IRON = createMaterial("wrought_iron", (b) -> b
                .composition(MetallurgicaElements.IRON, 3, MetallurgicaElements.CARBON, 1)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag().useColumnModel(),
                        new SheetFlag(),
                        new MoltenFlag(1482.0)
                ));
    }
}
