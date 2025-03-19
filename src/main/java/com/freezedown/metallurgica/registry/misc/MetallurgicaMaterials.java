package com.freezedown.metallurgica.registry.misc;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.items.metals.MagnesiumItem;
import com.freezedown.metallurgica.foundation.item.WireColours;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey;
import com.tterrag.registrate.util.entry.FluidEntry;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey.*;

@Getter
public enum MetallurgicaMaterials {
    //Metals
    //(entry(builder("").fluid())),
    IRON(material(builder("iron")
            .noRegister(INGOT, SHEET)
            .ingot()
            .sheet()
            .fluid(1538.0))),
    GOLD(material(builder("gold")
            .withNameAlternative(FlagKey.SHEET, "golden")
            .noRegister(INGOT, SHEET)
            .ingot()
            .sheet()
            .fluid(1064.2))),
    COPPER(material(builder("copper")
            .noRegister(INGOT, SHEET)
            .ingot()
            .sheet()
            .fluid(1084.6)
            .cable(0.0178, WireColours.copper))),
    NETHERIUM(material(builder("netherium")
            .ingot()
            .sheet()
            .fluid(3962.0)
            .cable(0.0237, WireColours.missing))),
    ALUMINUM(material(builder("aluminum")
            .noRegister(INGOT)
            .ingot()
            .sheet()
            .fluid(660.3)
            .cable(0.0276, WireColours.aluminum))),
    SCANDIUM(material(builder("scandium")
            .ingot()
            .sheet()
            .fluid(1541.0)
            .cable(0.0124, WireColours.scandium))),
    LEAD(material(builder("lead")
            .noRegister(INGOT)
            .ingot()
            .sheet()
            .fluid(327.5))),
    SILVER(material(builder("silver")
            .ingot()
            .sheet()
            .fluid(961.8))),
    NICKEL(material(builder("nickel")
            .noRegister(INGOT)
            .ingot()
            .sheet()
            .fluid(1455.0))),
    TIN(material(builder("tin")
            .ingot()
            .sheet()
            .fluid(231.9))),
    ZINC(material(builder("zinc")
            .noRegister(INGOT)
            .ingot()
            .sheet()
            .fluid(419.5))),
    PLATINUM(material(builder("platinum")
            .ingot()
            .sheet()
            .fluid(1768.3))),
    TITANIUM(material(builder("titanium")
            .ingot()
            .sheet(3)
            .fluid(1668.0))),
    URANIUM(material(builder("uranium")
            .ingot()
            .sheet()
            .fluid(1132.3))),
    LITHIUM(material(builder("lithium")
            .noRegister(INGOT)
            .ingot()
            .sheet()
            .fluid(180.5))),
    MAGNESIUM(material(builder("magnesium")
            .ingot(MagnesiumItem::createIngot)
            .sheet()
            .fluid(650.0))),
    TUNGSTEN(material(builder("tungsten")
            .ingot()
            .sheet()
            .fluid(3422.0))),
    OSMIUM(material(builder("osmium")
            .ingot()
            .sheet()
            .fluid(3033.0))),
    THORIUM(material(builder("thorium")
            .ingot()
            .sheet()
            .fluid(1750.0))),
    TANTALUM(material(builder("tantalum")
            .ingot()
            .sheet(2)
            .fluid(3020.0))),
    //Alloys
    TITANIUM_ALUMINIDE(material(builder("titanium_aluminide")
            .ingot()
            .sheet(3)
            .fluid(1447.0))),
    NETHERITE(material(builder("netherite")
            .noRegister(INGOT)
            .ingot()
            .sheet()
            .fluid(3562.0))),
    BRASS(material(builder("brass")
            .noRegister(INGOT, SHEET)
            .ingot()
            .sheet()
            .fluid(920.0))),
    BRONZE(material(builder("bronze")
            .ingot()
            .sheet()
            .fluid(950.0))),
    ARSENICAL_BRONZE(material(builder("arsenical_bronze")
            .ingot()
            .sheet()
            .fluid(685.0))),
    ;

    public static final Map<Material, List<FluidEntry<?>>> materialFluids = new HashMap<>();
    public final Material material;

    MetallurgicaMaterials(Material material) {
        this.material = material;
    }

    private static Material material(Material.Builder builder) {
        return builder.build();
    }

    private static Material.Builder builder(String name) {
        return new Material.Builder(Metallurgica.asResource(name));
    }

    public static void register() {
        //for (MetallurgicaMaterials metallurgicaMaterials : values()) {
        //    metallurgicaMaterials.getMaterialEntry().registerEverything();
        //}
    }
}
