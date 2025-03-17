package com.freezedown.metallurgica.registry.misc;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.items.metals.MagnesiumItem;
import com.freezedown.metallurgica.foundation.item.WireColours;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.MaterialEntry;
import com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.registry.MetallurgicaCreativeTab;
import com.tterrag.registrate.util.entry.FluidEntry;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public enum MetallurgicaMaterials {
    //Metals
    //(entry(builder("").fluid())),
    IRON(entry(builder("iron")
            .ingot(true)
            .sheet(true)
            .fluid(1538.0))),
    GOLD(entry(builder("gold")
            .withNameAlternative(FlagKey.SHEET, "golden")
            .ingot(true)
            .sheet(true)
            .fluid(1064.2))),
    COPPER(entry(builder("copper")
            .ingot(true)
            .sheet(true)
            .fluid(1084.6)
            .conductor(0.0178, WireColours.copper))),
    NETHERIUM(entry(builder("netherium")
            .ingot()
            .sheet()
            .fluid(3962.0))),
    ALUMINUM(entry(builder("aluminum")
            .ingot(true)
            .sheet()
            .fluid(660.3)
            .conductor(0.0276, WireColours.aluminum))),
    SCANDIUM(entry(builder("scandium")
            .ingot()
            .sheet()
            .fluid(1541.0)
            .conductor(0.0124, WireColours.scandium))),
    LEAD(entry(builder("lead")
            .ingot(true)
            .sheet()
            .fluid(327.5))),
    SILVER(entry(builder("silver")
            .ingot()
            .sheet()
            .fluid(961.8))),
    NICKEL(entry(builder("nickel")
            .ingot(true)
            .sheet()
            .fluid(1455.0))),
    TIN(entry(builder("tin")
            .ingot()
            .sheet()
            .fluid(231.9))),
    ZINC(entry(builder("zinc")
            .ingot(true)
            .sheet()
            .fluid(419.5))),
    PLATINUM(entry(builder("platinum")
            .ingot()
            .sheet()
            .fluid(1768.3))),
    TITANIUM(entry(builder("titanium")
            .ingot()
            .sheet(3)
            .fluid(1668.0))),
    URANIUM(entry(builder("uranium")
            .ingot()
            .sheet()
            .fluid(1132.3))),
    LITHIUM(entry(builder("lithium")
            .ingot(true)
            .sheet()
            .fluid(180.5))),
    MAGNESIUM(entry(builder("magnesium")
            .ingot(MagnesiumItem::createIngot)
            .sheet()
            .fluid(650.0))),
    TUNGSTEN(entry(builder("tungsten")
            .ingot()
            .sheet()
            .fluid(3422.0))),
    OSMIUM(entry(builder("osmium")
            .ingot()
            .sheet()
            .fluid(3033.0))),
    THORIUM(entry(builder("thorium")
            .ingot()
            .sheet()
            .fluid(1750.0))),
    TANTALUM(entry(builder("tantalum")
            .ingot()
            .sheet(2)
            .fluid(3020.0))),
    //Alloys
    TITANIUM_ALUMINIDE(entry(builder("titanium_aluminide")
            .ingot()
            .sheet(3)
            .fluid(1447.0))),
    NETHERITE(entry(builder("netherite")
            .ingot(true)
            .sheet()
            .fluid(3562.0))),
    BRASS(entry(builder("brass")
            .ingot(true)
            .sheet(true)
            .fluid(920.0))),
    ;

    public static final Map<Material, List<FluidEntry<?>>> materialFluids = new HashMap<>();
    public final MaterialEntry materialEntry;

    MetallurgicaMaterials(MaterialEntry materialEntry) {
        this.materialEntry = materialEntry;
    }

    private static MaterialEntry entry(Material.Builder builder) {
        MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().setCreativeTab(MetallurgicaCreativeTab.MAIN_TAB);
        return new MaterialEntry(registrate, builder.build());
    }

    private static Material.Builder builder(String name) {
        return new Material.Builder(Metallurgica.asResource(name));
    }

    public static void register() {
        for (MetallurgicaMaterials metallurgicaMaterials : values()) {
            metallurgicaMaterials.getMaterialEntry().registerEverything();
        }
    }
}
