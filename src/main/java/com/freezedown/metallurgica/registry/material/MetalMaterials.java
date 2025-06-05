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
                .noRegister(INGOT, SHEET, STORAGE_BLOCK)
                .addFlags(
                        new NuggetFlag("minecraft"),
                        new IngotFlag("minecraft"),
                        new StorageBlockFlag("minecraft"),
                        new SheetFlag("create"),
                        new FluidFlag(1538.0)
                ).buildAndRegister();
        GOLD = new Material.Builder(Metallurgica.asResource("gold"))
                .element(MetallurgicaElements.GOLD)
                .withNameAlternative(FlagKey.SHEET, "golden")
                .noRegister(INGOT, SHEET, STORAGE_BLOCK)
                .addFlags(
                        new NuggetFlag("minecraft"),
                        new IngotFlag("minecraft"),
                        new StorageBlockFlag("minecraft"),
                        new SheetFlag("create"),
                        new FluidFlag(1064.2)
                ).buildAndRegister();
        COPPER = new Material.Builder(Metallurgica.asResource("copper"))
                .element(MetallurgicaElements.COPPER)
                .noRegister(INGOT, SHEET, STORAGE_BLOCK)
                .addFlags(
                        new NuggetFlag("minecraft"),
                        new IngotFlag("minecraft"),
                        new StorageBlockFlag("minecraft"),
                        new SheetFlag("create"),
                        new FluidFlag(1084.6),
                        new CableFlag(0.0178, WireColours.copper)
                ).buildAndRegister();
        NETHERIUM = new Material.Builder(Metallurgica.asResource("netherium"))
                .element(MetallurgicaElements.NETHERIUM)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new FluidFlag(3962.0),
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
                        new FluidFlag(327.5)
                ).buildAndRegister();
        SILVER = new Material.Builder(Metallurgica.asResource("silver"))
                .element(MetallurgicaElements.SILVER)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new FluidFlag(961.8)
                ).buildAndRegister();
        NICKEL = new Material.Builder(Metallurgica.asResource("nickel"))
                .element(MetallurgicaElements.NICKEL)
                .noRegister(INGOT, STORAGE_BLOCK)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag("tfmg"),
                        new StorageBlockFlag("tfmg"),
                        new SheetFlag(),
                        new FluidFlag(1455.0)
                ).buildAndRegister();
        TIN = new Material.Builder(Metallurgica.asResource("tin"))
                .element(MetallurgicaElements.TIN)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new FluidFlag(231.9)
                ).buildAndRegister();
        ZINC = new Material.Builder(Metallurgica.asResource("zinc"))
                .element(MetallurgicaElements.ZINC)
                .noRegister(INGOT, STORAGE_BLOCK)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag("create"),
                        new StorageBlockFlag("create"),
                        new SheetFlag(),
                        new FluidFlag(419.5)
                ).buildAndRegister();
        PLATINUM = new Material.Builder(Metallurgica.asResource("platinum"))
                .element(MetallurgicaElements.PLATINUM)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new FluidFlag(1768.3)
                ).buildAndRegister();
        TITANIUM = new Material.Builder(Metallurgica.asResource("titanium"))
                .element(MetallurgicaElements.TITANIUM)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag().pressTimes(3),
                        new FluidFlag(1668.0)
                ).buildAndRegister();
        URANIUM = new Material.Builder(Metallurgica.asResource("uranium"))
                .element(MetallurgicaElements.URANIUM)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new FluidFlag(1132.3)
                ).buildAndRegister();
        LITHIUM = new Material.Builder(Metallurgica.asResource("lithium"))
                .element(MetallurgicaElements.LITHIUM)
                .noRegister(INGOT, STORAGE_BLOCK)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag("tfmg"),
                        new StorageBlockFlag("tfmg"),
                        new SheetFlag(),
                        new FluidFlag(180.5)
                ).buildAndRegister();
        MAGNESIUM = new Material.Builder(Metallurgica.asResource("magnesium"))
                .element(MetallurgicaElements.MAGNESIUM)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new FluidFlag(650.0)
                ).buildAndRegister();
        TUNGSTEN = new Material.Builder(Metallurgica.asResource("tungsten"))
                .element(MetallurgicaElements.TUNGSTEN)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag().pressTimes(4),
                        new FluidFlag(3422.0)
                ).buildAndRegister();
        OSMIUM = new Material.Builder(Metallurgica.asResource("osmium"))
                .element(MetallurgicaElements.OSMIUM)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new FluidFlag(3033.0)
                ).buildAndRegister();
        THORIUM = new Material.Builder(Metallurgica.asResource("thorium"))
                .element(MetallurgicaElements.THORIUM)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new FluidFlag(1750.0)
                ).buildAndRegister();
        TANTALUM = new Material.Builder(Metallurgica.asResource("tantalum"))
                .element(MetallurgicaElements.TANTALUM)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag().pressTimes(2),
                        new FluidFlag(3020.0)
                ).buildAndRegister();
    }
}
