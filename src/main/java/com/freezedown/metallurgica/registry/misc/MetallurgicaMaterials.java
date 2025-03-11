package com.freezedown.metallurgica.registry.misc;

import com.drmangotea.tfmg.registry.TFMGItems;
import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.item.registry.MaterialEntry;
import com.freezedown.metallurgica.foundation.material.MetalEntry;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.registry.MetallurgicaCreativeTab;
import lombok.Getter;

@Getter
public enum MetallurgicaMaterials {
    //Metals
    IRON(entry("iron")          .molten(1538.0) ),
    GOLD(entry("gold")          .molten(1064.2) ),
    COPPER(entry("copper")      .molten(1084.6) .wiring(0.0178, TFMGItems.COPPER_WIRE)),
    NETHERIUM(entry("netherium").molten(3962.0) ),
    ALUMINUM(entry("aluminum")  .molten(660.3)  .wiring(0.0276, new int[]{211, 215, 215, 255}, new int[]{176, 182, 186, 255}, TFMGItems.ALUMINUM_WIRE)),
    SCANDIUM(entry("scandium")  .molten(1541.0) .wiring(0.0124, new int[]{237, 235, 216, 255}, new int[]{218, 209, 198, 255})),
    LEAD(entry("lead")          .molten(327.5)  ),
    SILVER(entry("silver")      .molten(961.8)  ),
    NICKEL(entry("nickel")      .molten(1455.0) ),
    TIN(entry("tin")            .molten(231.9)  ),
    ZINC(entry("zinc")          .molten(419.5)  ),
    PLATINUM(entry("platinum")  .molten(1768.3) ),
    TITANIUM(entry("titanium")  .molten(1668.0) ),
    URANIUM(entry("uranium")    .molten(1132.3) ),
    LITHIUM(entry("lithium")    .molten(180.5)  ),
    MAGNESIUM(entry("magnesium").molten(650.0)  ),
    TUNGSTEN(entry("tungsten")  .molten(3422.0) ),
    OSMIUM(entry("osmium")      .molten(3033.0) ),
    THORIUM(entry("thorium")    .molten(1750.0) ),
    //Alloys
    NETHERITE(entry("netherite").molten(3562.0)),
    BRASS(entry("brass").molten(920.0)),
    ;

    public final MaterialEntry materialEntry;

    MetallurgicaMaterials(MaterialEntry materialEntry) {
        this.materialEntry = materialEntry;
    }

    private static MaterialEntry entry(String name) {
        MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().setCreativeTab(MetallurgicaCreativeTab.MAIN_TAB);
        return new MaterialEntry(registrate, name);
    }
}
