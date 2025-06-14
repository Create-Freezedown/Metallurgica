package com.freezedown.metallurgica.registry.material;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.item.WireColours;
import com.freezedown.metallurgica.foundation.material.registry.Material;
import com.freezedown.metallurgica.foundation.material.registry.flags.FlagKey;
import com.freezedown.metallurgica.foundation.material.registry.flags.block.CasingFlag;
import com.freezedown.metallurgica.foundation.material.registry.flags.block.SheetmetalFlag;
import com.freezedown.metallurgica.foundation.material.registry.flags.block.StorageBlockFlag;
import com.freezedown.metallurgica.foundation.material.registry.flags.fluid.MoltenFlag;
import com.freezedown.metallurgica.foundation.material.registry.flags.item.*;
import com.freezedown.metallurgica.foundation.material.registry.flags.item.CableFlag;
import com.freezedown.metallurgica.registry.MetallurgicaTags;
import com.freezedown.metallurgica.registry.misc.MetallurgicaElements;

import static com.freezedown.metallurgica.registry.material.MetMaterials.*;
import static com.freezedown.metallurgica.foundation.material.registry.flags.FlagKey.*;

public class MetalMaterials {
    public static void register() {
        IRON = createMaterial("iron", (b) -> b
                .element(MetallurgicaElements.IRON)
                .noRegister(NUGGET, INGOT, SHEET, STORAGE_BLOCK)
                .addFlags(
                        new NuggetFlag("minecraft"),
                        new IngotFlag("minecraft"),
                        new StorageBlockFlag("minecraft"),
                        new SheetFlag("create"),
                        new MoltenFlag(1538.0),
                        new DustFlag()
                ));
        GOLD = createMaterial("gold", (b) -> b
                .element(MetallurgicaElements.GOLD)
                .nameAlternatives(FlagKey.SHEET, "golden")
                .noRegister(NUGGET, INGOT, SHEET, STORAGE_BLOCK)
                .addFlags(
                        new NuggetFlag("minecraft"),
                        new IngotFlag("minecraft"),
                        new StorageBlockFlag("minecraft"),
                        new SheetFlag("create"),
                        new MoltenFlag(1064.2),
                        new DustFlag(),
                        new MineralFlag(true),
                        new RubbleFlag().crushing().bonusChance(0.15f)
                ));
        COPPER = createMaterial("copper", (b) -> b
                .element(MetallurgicaElements.COPPER)
                .noRegister(NUGGET, INGOT, SHEET, STORAGE_BLOCK, WIRE)
                .addFlags(
                        new NuggetFlag("create"),
                        new IngotFlag("minecraft"),
                        new StorageBlockFlag("minecraft"),
                        new SheetFlag("create"),
                        new MoltenFlag(1084.6),
                        new DustFlag(),
                        new WireFlag("tfmg"),
                        new CableFlag(0.0178, WireColours.copper),
                        new MineralFlag(true),
                        new RubbleFlag().crushing().bonusChance(0.15f)
                ));
        NETHERIUM = createMaterial("netherium", (b) -> b
                .element(MetallurgicaElements.NETHERIUM)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new WireFlag(),
                        new MoltenFlag(3962.0),
                        new DustFlag()
                ));
        ALUMINUM = createMaterial("aluminum", (b) -> b
                .element(MetallurgicaElements.ALUMINUM)
                .noRegister(INGOT, STORAGE_BLOCK, WIRE)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag("tfmg"),
                        new StorageBlockFlag("tfmg"),
                        new SheetFlag(),
                        new MoltenFlag(660.3),
                        new DustFlag(),
                        new SheetmetalFlag(),
                        new WireFlag("tfmg"),
                        new CableFlag(0.0276, WireColours.aluminum)
                ));
        SCANDIUM = createMaterial("scandium", (b) -> b
                .element(MetallurgicaElements.SCANDIUM)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new MoltenFlag(1541.0),
                        new DustFlag(),
                        new CableFlag(0.0124, WireColours.scandium)
                ));
        LEAD = createMaterial("lead", (b) -> b
                .element(MetallurgicaElements.LEAD)
                .noRegister(INGOT, STORAGE_BLOCK)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag("tfmg"),
                        new StorageBlockFlag("tfmg"),
                        new SheetFlag(),
                        new MoltenFlag(327.5),
                        new DustFlag()
                ));
        SILVER = createMaterial("silver", (b) -> b
                .element(MetallurgicaElements.SILVER)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new MoltenFlag(961.8),
                        new DustFlag()
                ));
        NICKEL = createMaterial("nickel", (b) -> b
                .element(MetallurgicaElements.NICKEL)
                .noRegister(INGOT, STORAGE_BLOCK)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag("tfmg"),
                        new StorageBlockFlag("tfmg"),
                        new SheetFlag(),
                        new MoltenFlag(1455.0),
                        new DustFlag()
                ));
        TIN = createMaterial("tin", (b) -> b
                .element(MetallurgicaElements.TIN)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new MoltenFlag(231.9),
                        new DustFlag()
                ));
        ZINC = createMaterial("zinc", (b) -> b
                .element(MetallurgicaElements.ZINC)
                .noRegister(NUGGET, INGOT, STORAGE_BLOCK)
                .addFlags(
                        new NuggetFlag("create"),
                        new IngotFlag("create"),
                        new StorageBlockFlag("create"),
                        new SheetFlag(),
                        new MoltenFlag(419.5),
                        new DustFlag()
                ));
        PLATINUM = createMaterial("platinum", (b) -> b
                .element(MetallurgicaElements.PLATINUM)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new MoltenFlag(1768.3),
                        new DustFlag()
                ));
        TITANIUM = createMaterial("titanium", (b) -> b
                .element(MetallurgicaElements.TITANIUM)
                .addFlags(
                        new NuggetFlag().requiresCompacting(),
                        new IngotFlag().requiresCompacting(),
                        new StorageBlockFlag().requiresDecompacting(),
                        new SheetFlag().pressTimes(3),
                        new MoltenFlag(1668.0),
                        new DustFlag(),
                        new SheetmetalFlag().requiresCompacting()
                ));
        URANIUM = createMaterial("uranium", (b) -> b
                .element(MetallurgicaElements.URANIUM)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new MoltenFlag(1132.3),
                        new DustFlag()
                ));
        LITHIUM = createMaterial("lithium", (b) -> b
                .element(MetallurgicaElements.LITHIUM)
                .noRegister(INGOT, STORAGE_BLOCK)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag("tfmg"),
                        new StorageBlockFlag("tfmg"),
                        new SheetFlag(),
                        new MoltenFlag(180.5),
                        new DustFlag()
                ));
        MAGNESIUM = createMaterial("magnesium", (b) -> b
                .element(MetallurgicaElements.MAGNESIUM)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new MoltenFlag(650.0),
                        new DustFlag()
                ));
        TUNGSTEN = createMaterial("tungsten", (b) -> b
                .element(MetallurgicaElements.TUNGSTEN)
                .addFlags(
                        new NuggetFlag().requiresCompacting(),
                        new IngotFlag().requiresCompacting(),
                        new StorageBlockFlag().requiresDecompacting(),
                        new SheetFlag().pressTimes(4),
                        new MoltenFlag(3422.0),
                        new DustFlag(),
                        new SheetmetalFlag().requiresCompacting()
                ));
        OSMIUM = createMaterial("osmium", (b) -> b
                .element(MetallurgicaElements.OSMIUM)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new MoltenFlag(3033.0),
                        new DustFlag()
                ));
        THORIUM = createMaterial("thorium", (b) -> b
                .element(MetallurgicaElements.THORIUM)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new MoltenFlag(1750.0),
                        new DustFlag()
                ));
        TANTALUM = createMaterial("tantalum", (b) -> b
                .element(MetallurgicaElements.TANTALUM)
                .addFlags(
                        new NuggetFlag().requiresCompacting(),
                        new IngotFlag().requiresCompacting(),
                        new StorageBlockFlag().requiresDecompacting(),
                        new SheetFlag().pressTimes(2),
                        new MoltenFlag(3020.0),
                        new DustFlag()
                ));
        SODIUM = createMaterial("sodium", (b) -> b
                .element(MetallurgicaElements.SODIUM)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag().useColumnModel(),
                        new CasingFlag().appliesOn(MetallurgicaTags.forgeItemTag("storage_blocks/plastic")),
                        new SheetFlag(),
                        new DustFlag()
                ));
        CHROMIUM = createMaterial("chromium", (b) -> b
                .element(MetallurgicaElements.CHROMIUM)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new MoltenFlag(1907.0),
                        new DustFlag()
                ));
        VANADIUM = createMaterial("vanadium", (b) -> b
                .element(MetallurgicaElements.VANADIUM)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new MoltenFlag(1910.0),
                        new DustFlag()
                ));
        MANGANESE = createMaterial("manganese", (b) -> b
                .element(MetallurgicaElements.MANGANESE)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new MoltenFlag(1246.0),
                        new DustFlag()
                ));
    }
}
