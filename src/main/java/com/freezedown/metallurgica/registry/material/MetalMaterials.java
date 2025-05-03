package com.freezedown.metallurgica.registry.material;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.item.WireColours;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey;
import com.freezedown.metallurgica.registry.misc.MetallurgicaElements;

import static com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey.*;
import static com.freezedown.metallurgica.registry.material.MetMaterials.*;
public class MetalMaterials {
    public static void register() {
        IRON = new Material.Builder(Metallurgica.asResource("iron"))
                .element(MetallurgicaElements.IRON)
                .noRegister(INGOT, SHEET)
                .ingot("minecraft")
                .sheet("create")
                .fluid(1538.0)
                .buildAndRegister();
        GOLD = new Material.Builder(Metallurgica.asResource("gold"))
                .element(MetallurgicaElements.GOLD)
                .withNameAlternative(FlagKey.SHEET, "golden")
                .noRegister(INGOT, SHEET)
                .ingot("minecraft")
                .sheet("create")
                .fluid(1064.2).buildAndRegister();
        COPPER = new Material.Builder(Metallurgica.asResource("copper"))
                .element(MetallurgicaElements.COPPER)
                .noRegister(INGOT, SHEET)
                .ingot("minecraft")
                .sheet("create")
                .fluid(1084.6)
                .cable(0.0178, WireColours.copper).buildAndRegister();
        NETHERIUM = new Material.Builder(Metallurgica.asResource("netherium"))
                .element(MetallurgicaElements.NETHERIUM)
                .ingot()
                .storageBlock()
                .sheet()
                .fluid(3962.0)
                .cable(0.0237, WireColours.missing).buildAndRegister();
        ALUMINUM = new Material.Builder(Metallurgica.asResource("aluminum"))
                .element(MetallurgicaElements.ALUMINUM)
                .noRegister(INGOT)
                .ingot("tfmg")
                .sheet()
                .fluid(660.3)
                .cable(0.0276, WireColours.aluminum).buildAndRegister();
        SCANDIUM = new Material.Builder(Metallurgica.asResource("scandium"))
                .element(MetallurgicaElements.SCANDIUM)
                .ingot()
                .storageBlock()
                .sheet()
                .fluid(1541.0)
                .cable(0.0124, WireColours.scandium).buildAndRegister();
        LEAD = new Material.Builder(Metallurgica.asResource("lead"))
                .element(MetallurgicaElements.LEAD)
                .noRegister(INGOT)
                .ingot("tfmg")
                .sheet()
                .fluid(327.5).buildAndRegister();
        SILVER = new Material.Builder(Metallurgica.asResource("silver"))
                .element(MetallurgicaElements.SILVER)
                .ingot()
                .storageBlock()
                .sheet()
                .fluid(961.8).buildAndRegister();
        NICKEL = new Material.Builder(Metallurgica.asResource("nickel"))
                .element(MetallurgicaElements.NICKEL)
                .noRegister(INGOT)
                .ingot("tfmg")
                .sheet()
                .fluid(1455.0).buildAndRegister();
        TIN = new Material.Builder(Metallurgica.asResource("tin"))
                .element(MetallurgicaElements.TIN)
                .ingot()
                .storageBlock()
                .sheet()
                .fluid(231.9).buildAndRegister();
        ZINC = new Material.Builder(Metallurgica.asResource("zinc"))
                .element(MetallurgicaElements.ZINC)
                .noRegister(INGOT)
                .ingot("create")
                .sheet()
                .fluid(419.5).buildAndRegister();
        PLATINUM = new Material.Builder(Metallurgica.asResource("platinum"))
                .element(MetallurgicaElements.PLATINUM)
                .ingot()
                .storageBlock()
                .sheet()
                .fluid(1768.3).buildAndRegister();
        TITANIUM = new Material.Builder(Metallurgica.asResource("titanium"))
                .element(MetallurgicaElements.TITANIUM)
                .ingot()
                .storageBlock()
                .sheet(3)
                .fluid(1668.0).buildAndRegister();
        URANIUM = new Material.Builder(Metallurgica.asResource("uranium"))
                .element(MetallurgicaElements.URANIUM)
                .ingot()
                .storageBlock()
                .sheet()
                .fluid(1132.3).buildAndRegister();
        LITHIUM = new Material.Builder(Metallurgica.asResource("lithium"))
                .element(MetallurgicaElements.LITHIUM)
                .noRegister(INGOT)
                .ingot("tfmg")
                .sheet()
                .fluid(180.5).buildAndRegister();
        MAGNESIUM = new Material.Builder(Metallurgica.asResource("magnesium"))
                .element(MetallurgicaElements.MAGNESIUM)
                .ingot()
                .storageBlock()
                .sheet()
                .fluid(650.0).buildAndRegister();
        TUNGSTEN = new Material.Builder(Metallurgica.asResource("tungsten"))
                .element(MetallurgicaElements.TUNGSTEN)
                .ingot()
                .storageBlock()
                .sheet(4)
                .fluid(3422.0).buildAndRegister();
        OSMIUM = new Material.Builder(Metallurgica.asResource("osmium"))
                .element(MetallurgicaElements.OSMIUM)
                .ingot()
                .storageBlock()
                .sheet()
                .fluid(3033.0).buildAndRegister();
        THORIUM = new Material.Builder(Metallurgica.asResource("thorium"))
                .element(MetallurgicaElements.THORIUM)
                .ingot()
                .storageBlock()
                .sheet()
                .fluid(1750.0).buildAndRegister();
        TANTALUM = new Material.Builder(Metallurgica.asResource("tantalum"))
                .element(MetallurgicaElements.TANTALUM)
                .ingot()
                .storageBlock()
                .sheet(2)
                .fluid(3020.0).buildAndRegister();
    }
}
