package com.freezedown.metallurgica.registry.material;

import com.freezedown.metallurgica.infastructure.material.registry.flags.block.*;
import com.freezedown.metallurgica.infastructure.material.registry.flags.fluid.MoltenFlag;
import com.freezedown.metallurgica.infastructure.material.registry.flags.item.*;
import com.freezedown.metallurgica.registry.misc.MetallurgicaElements;

import static com.freezedown.metallurgica.registry.material.MetMaterials.*;
import static com.freezedown.metallurgica.infastructure.material.registry.flags.FlagKey.*;


public class AlloyMaterials {
    public static void register() {
        TITANIUM_ALUMINIDE = createMaterial("titanium_aluminide", (b) -> b
                .composition(MetallurgicaElements.TITANIUM, 2, MetallurgicaElements.ALUMINUM, 1)
                .meltingPoint(1447.0)
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
                .meltingPoint(3562.0)
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
                .meltingPoint(920.0)
                .noRegister(NUGGET, INGOT, CASING, STORAGE_BLOCK, SHEET)
                .addFlags(
                        new NuggetFlag("create"),
                        new IngotFlag("create"),
                        new StorageBlockFlag("create"),
                        new SheetFlag("create"),
                        new CasingFlag("create"),
                        new MoltenFlag(920.0),
                        new DustFlag()
                ));
        BRONZE = createMaterial("bronze", (b) -> b
                .composition(MetallurgicaElements.COPPER, 7, MetallurgicaElements.TIN, 2)
                .meltingPoint(950.0)
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
                .meltingPoint(685.0)
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
                .meltingPoint(1482.0)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag().useColumnModel(),
                        new SheetFlag(),
                        new MoltenFlag(1482.0)
                ));
        CAST_IRON = createMaterial("cast_iron",  (b) -> b
                .element(MetallurgicaElements.IRON)
                .noRegister(INGOT, NUGGET, SHEET, STORAGE_BLOCK)
                .addFlags(
                        new IngotFlag("tfmg"),
                        new StorageBlockFlag("tfmg"),
                        new NuggetFlag("tfmg"),
                        new SheetFlag("tfmg"),
                        new DustFlag()
                ));
        STEEL = createMaterial("steel", (b) -> b
                .element(MetallurgicaElements.IRON)
                .noRegister(INGOT, NUGGET, SEMI_PRESSED_SHEET, SHEET, STORAGE_BLOCK, COG_WHEEL, LARGE_COG_WHEEL, CASING)
                .existingIds(SEMI_PRESSED_SHEET, "tfmg:unprocessed_heavy_plate", SHEET, "tfmg:heavy_plate")
                .addFlags(
                        new NuggetFlag("tfmg"),
                        new IngotFlag("tfmg"),
                        new SemiPressedSheetFlag("tfmg"),
                        new SheetFlag("tfmg").pressTimes(3),
                        new StorageBlockFlag("tfmg"),
                        new CasingFlag("tfmg"),
                        new CogWheelFlag("tfmg"), new LargeCogWheelFlag("tfmg"),
                        new DustFlag()
                ));
        CONSTANTAN = createMaterial("constantan", (b) -> b
                .composition(MetallurgicaElements.COPPER, 1, MetallurgicaElements.NICKEL, 1)
                .noRegister(INGOT, NUGGET, STORAGE_BLOCK)
                .addFlags(
                        new IngotFlag("tfmg"),
                        new StorageBlockFlag("tfmg"),
                        new NuggetFlag("tfmg"),
                        new SheetFlag(),
                        new DustFlag()
                ));
    }
}
