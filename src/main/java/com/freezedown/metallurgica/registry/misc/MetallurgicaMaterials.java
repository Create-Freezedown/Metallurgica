package com.freezedown.metallurgica.registry.misc;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.items.metals.MagnesiumItem;
import com.freezedown.metallurgica.foundation.item.WireColours;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.BlockFlag;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.registry.MetallurgicaCreativeTab;
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
            .ingot("minecraft")
            .sheet("create")
            .fluid(1538.0))),
    GOLD(material(builder("gold")
            .withNameAlternative(FlagKey.SHEET, "golden")
            .noRegister(INGOT, SHEET)
            .ingot("minecraft")
            .sheet("create")
            .fluid(1064.2))),
    COPPER(material(builder("copper")
            .noRegister(INGOT, SHEET)
            .ingot("minecraft")
            .sheet("create")
            .fluid(1084.6)
            .cable(0.0178, WireColours.copper))),
    NETHERIUM(material(builder("netherium")
            .ingot()
            .storageBlock()
            .sheet()
            .fluid(3962.0)
            .cable(0.0237, WireColours.missing))),
    ALUMINUM(material(builder("aluminum")
            .noRegister(INGOT)
            .ingot("tfmg")
            .sheet()
            .fluid(660.3)
            .cable(0.0276, WireColours.aluminum))),
    SCANDIUM(material(builder("scandium")
            .ingot()
            .storageBlock()
            .sheet()
            .fluid(1541.0)
            .cable(0.0124, WireColours.scandium))),
    LEAD(material(builder("lead")
            .noRegister(INGOT)
            .ingot("tfmg")
            .sheet()
            .fluid(327.5))),
    SILVER(material(builder("silver")
            .ingot()
            .storageBlock()
            .sheet()
            .fluid(961.8))),
    NICKEL(material(builder("nickel")
            .noRegister(INGOT)
            .ingot("tfmg")
            .sheet()
            .fluid(1455.0))),
    TIN(material(builder("tin")
            .ingot()
            .storageBlock()
            .sheet()
            .fluid(231.9))),
    ZINC(material(builder("zinc")
            .noRegister(INGOT)
            .ingot("create")
            .sheet()
            .fluid(419.5))),
    PLATINUM(material(builder("platinum")
            .ingot()
            .storageBlock()
            .sheet()
            .fluid(1768.3))),
    TITANIUM(material(builder("titanium")
            .ingot()
            .storageBlock()
            .sheet(3)
            .fluid(1668.0))),
    URANIUM(material(builder("uranium")
            .ingot()
            .storageBlock()
            .sheet()
            .fluid(1132.3))),
    LITHIUM(material(builder("lithium")
            .noRegister(INGOT)
            .ingot("tfmg")
            .sheet()
            .fluid(180.5))),
    MAGNESIUM(material(builder("magnesium")
            .ingot(MagnesiumItem::createIngot)
            .storageBlock()
            .sheet()
            .fluid(650.0))),
    TUNGSTEN(material(builder("tungsten")
            .ingot()
            .storageBlock()
            .sheet(4)
            .fluid(3422.0))),
    OSMIUM(material(builder("osmium")
            .ingot()
            .storageBlock()
            .sheet()
            .fluid(3033.0))),
    THORIUM(material(builder("thorium")
            .ingot()
            .storageBlock()
            .sheet()
            .fluid(1750.0))),
    TANTALUM(material(builder("tantalum")
            .ingot()
            .storageBlock()
            .sheet(2)
            .fluid(3020.0))),
    //Alloys
    TITANIUM_ALUMINIDE(material(builder("titanium_aluminide")
            .ingot()
            .storageBlock()
            .sheet(3)
            .fluid(1447.0))),
    NETHERITE(material(builder("netherite")
            .noRegister(INGOT)
            .ingot("minecraft")
            .sheet()
            .fluid(3562.0))),
    BRASS(material(builder("brass")
            .noRegister(INGOT, SHEET)
            .ingot("create")
            .sheet("create")
            .fluid(920.0))),
    BRONZE(material(builder("bronze")
            .ingot()
            .storageBlock()
            .sheet()
            .fluid(950.0))),
    ARSENICAL_BRONZE(material(builder("arsenical_bronze")
            .ingot()
            .storageBlock()
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

    private static final MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().setCreativeTab(MetallurgicaCreativeTab.MAIN_TAB);

    public static void register() {
        for (MetallurgicaMaterials material : MetallurgicaMaterials.values()) {
            var fluidFlag = material.getMaterial().getFlag(FlagKey.FLUID);
            if (fluidFlag != null) {
                fluidFlag.registerFluids(material.getMaterial(), registrate);
            }
        }
    }
}
