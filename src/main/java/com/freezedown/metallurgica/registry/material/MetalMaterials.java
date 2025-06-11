package com.freezedown.metallurgica.registry.material;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.item.WireColours;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.*;
import com.freezedown.metallurgica.registry.misc.MetallurgicaElements;

import static com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey.*;
import static com.freezedown.metallurgica.registry.material.MetMaterials.*;
public class MetalMaterials {
    public static void register() {
        IRON = new Material.Builder(Metallurgica.asResource("iron"))
                .element(MetallurgicaElements.IRON)
                .noRegister(NUGGET, INGOT, SHEET, STORAGE_BLOCK)
                .addFlags(
                        new NuggetFlag("minecraft"),
                        new IngotFlag("minecraft"),
                        new StorageBlockFlag("minecraft"),
                        new SheetFlag("create"),
                        new FluidFlag(1538.0),
                        new DustFlag()
                ).buildAndRegister();
        GOLD = new Material.Builder(Metallurgica.asResource("gold"))
                .element(MetallurgicaElements.GOLD)
                .nameAlternatives(FlagKey.SHEET, "golden")
                .noRegister(NUGGET, INGOT, SHEET, STORAGE_BLOCK)
                .addFlags(
                        new NuggetFlag("minecraft"),
                        new IngotFlag("minecraft"),
                        new StorageBlockFlag("minecraft"),
                        new SheetFlag("create"),
                        new FluidFlag(1064.2),
                        new DustFlag()
                ).buildAndRegister();
        COPPER = new Material.Builder(Metallurgica.asResource("copper"))
                .element(MetallurgicaElements.COPPER)
                .noRegister(NUGGET, INGOT, SHEET, STORAGE_BLOCK)
                .addFlags(
                        new NuggetFlag("create"),
                        new IngotFlag("minecraft"),
                        new StorageBlockFlag("minecraft"),
                        new SheetFlag("create"),
                        new FluidFlag(1084.6),
                        new DustFlag(),
                        new CableFlag(0.0178, WireColours.copper),
                        new MineralFlag(true),
                        new RubbleFlag().crushing().bonusChance(0.15f)
                ).buildAndRegister();
        NETHERIUM = new Material.Builder(Metallurgica.asResource("netherium"))
                .element(MetallurgicaElements.NETHERIUM)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new FluidFlag(3962.0),
                        new DustFlag(),
                        new CableFlag(0.0237, WireColours.missing)
                ).buildAndRegister();
        ALUMINUM = new Material.Builder(Metallurgica.asResource("aluminum"))
                .element(MetallurgicaElements.ALUMINUM)
                .noRegister(INGOT, STORAGE_BLOCK)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag("tfmg"),
                        new StorageBlockFlag("tfmg"),
                        new SheetFlag(),
                        new FluidFlag(660.3),
                        new DustFlag(),
                        new CableFlag(0.0276, WireColours.aluminum)
                ).buildAndRegister();
        SCANDIUM = new Material.Builder(Metallurgica.asResource("scandium"))
                .element(MetallurgicaElements.SCANDIUM)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new FluidFlag(1541.0),
                        new DustFlag(),
                        new CableFlag(0.0124, WireColours.scandium)
                ).buildAndRegister();
        LEAD = new Material.Builder(Metallurgica.asResource("lead"))
                .element(MetallurgicaElements.LEAD)
                .noRegister(INGOT, STORAGE_BLOCK)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag("tfmg"),
                        new StorageBlockFlag("tfmg"),
                        new SheetFlag(),
                        new FluidFlag(327.5),
                        new DustFlag()
                ).buildAndRegister();
        SILVER = new Material.Builder(Metallurgica.asResource("silver"))
                .element(MetallurgicaElements.SILVER)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new FluidFlag(961.8),
                        new DustFlag()
                ).buildAndRegister();
        NICKEL = new Material.Builder(Metallurgica.asResource("nickel"))
                .element(MetallurgicaElements.NICKEL)
                .noRegister(INGOT, STORAGE_BLOCK)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag("tfmg"),
                        new StorageBlockFlag("tfmg"),
                        new SheetFlag(),
                        new FluidFlag(1455.0),
                        new DustFlag()
                ).buildAndRegister();
        TIN = new Material.Builder(Metallurgica.asResource("tin"))
                .element(MetallurgicaElements.TIN)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new FluidFlag(231.9),
                        new DustFlag()
                ).buildAndRegister();
        ZINC = new Material.Builder(Metallurgica.asResource("zinc"))
                .element(MetallurgicaElements.ZINC)
                .noRegister(NUGGET, INGOT, STORAGE_BLOCK)
                .addFlags(
                        new NuggetFlag("create"),
                        new IngotFlag("create"),
                        new StorageBlockFlag("create"),
                        new SheetFlag(),
                        new FluidFlag(419.5),
                        new DustFlag()
                ).buildAndRegister();
        PLATINUM = new Material.Builder(Metallurgica.asResource("platinum"))
                .element(MetallurgicaElements.PLATINUM)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new FluidFlag(1768.3),
                        new DustFlag()
                ).buildAndRegister();
        TITANIUM = new Material.Builder(Metallurgica.asResource("titanium"))
                .element(MetallurgicaElements.TITANIUM)
                .addFlags(
                        new NuggetFlag().requiresCompacting(),
                        new IngotFlag().requiresCompacting(),
                        new StorageBlockFlag().requiresDecompacting(),
                        new SheetFlag().pressTimes(3),
                        new FluidFlag(1668.0),
                        new DustFlag()
                ).buildAndRegister();
        URANIUM = new Material.Builder(Metallurgica.asResource("uranium"))
                .element(MetallurgicaElements.URANIUM)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new FluidFlag(1132.3),
                        new DustFlag()
                ).buildAndRegister();
        LITHIUM = new Material.Builder(Metallurgica.asResource("lithium"))
                .element(MetallurgicaElements.LITHIUM)
                .noRegister(INGOT, STORAGE_BLOCK)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag("tfmg"),
                        new StorageBlockFlag("tfmg"),
                        new SheetFlag(),
                        new FluidFlag(180.5),
                        new DustFlag()
                ).buildAndRegister();
        MAGNESIUM = new Material.Builder(Metallurgica.asResource("magnesium"))
                .element(MetallurgicaElements.MAGNESIUM)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new FluidFlag(650.0),
                        new DustFlag()
                ).buildAndRegister();
        TUNGSTEN = new Material.Builder(Metallurgica.asResource("tungsten"))
                .element(MetallurgicaElements.TUNGSTEN)
                .addFlags(
                        new NuggetFlag().requiresCompacting(),
                        new IngotFlag().requiresCompacting(),
                        new StorageBlockFlag().requiresDecompacting(),
                        new SheetFlag().pressTimes(4),
                        new FluidFlag(3422.0),
                        new DustFlag()
                ).buildAndRegister();
        OSMIUM = new Material.Builder(Metallurgica.asResource("osmium"))
                .element(MetallurgicaElements.OSMIUM)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new FluidFlag(3033.0),
                        new DustFlag()
                ).buildAndRegister();
        THORIUM = new Material.Builder(Metallurgica.asResource("thorium"))
                .element(MetallurgicaElements.THORIUM)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new FluidFlag(1750.0),
                        new DustFlag()
                ).buildAndRegister();
        TANTALUM = new Material.Builder(Metallurgica.asResource("tantalum"))
                .element(MetallurgicaElements.TANTALUM)
                .addFlags(
                        new NuggetFlag().requiresCompacting(),
                        new IngotFlag().requiresCompacting(),
                        new StorageBlockFlag().requiresDecompacting(),
                        new SheetFlag().pressTimes(2),
                        new FluidFlag(3020.0),
                        new DustFlag()
                ).buildAndRegister();
        SODIUM = new Material.Builder(Metallurgica.asResource("sodium"))
                .element(MetallurgicaElements.SODIUM)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag().useColumnModel(),
                        new SheetFlag(),
                        new DustFlag()
                ).buildAndRegister();
        CHROMIUM = new Material.Builder(Metallurgica.asResource("chromium"))
                .element(MetallurgicaElements.CHROMIUM)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new FluidFlag(1907.0),
                        new DustFlag()
                ).buildAndRegister();
        VANADIUM = new Material.Builder(Metallurgica.asResource("vanadium"))
                .element(MetallurgicaElements.VANADIUM)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new FluidFlag(1910.0),
                        new DustFlag()
                ).buildAndRegister();
        MANGANESE = new Material.Builder(Metallurgica.asResource("manganese"))
                .element(MetallurgicaElements.MANGANESE)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new FluidFlag(1246.0),
                        new DustFlag()
                ).buildAndRegister();
    }
}
