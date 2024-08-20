package com.freezedown.metallurgica.registry;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.MetallurgicaRegistrate;
import com.freezedown.metallurgica.foundation.material.MetalEntry;

public enum MetallurgicaMetals {
    IRON (1538.0),
    GOLD (1064.2),
    COPPER (1084.6),
    NETHERIUM(3962.0),
    NETHERITE(3562.0),
    BRASS(920.0),
    ALUMINUM (660.3),
    LEAD (327.5),
    SILVER (961.8),
    NICKEL (1455.0),
    TIN (231.9),
    ZINC (419.5),
    PLATINUM (1768.3),
    TITANIUM (1668.0),
    URANIUM (1132.3),
    LITHIUM (180.5),
    MAGNESIUM (650.0),
    TUNGSTEN (3422.0),
    OSMIUM (3033.0),
    THORIUM (1750.0);

    public final MetalEntry METAL;

    MetallurgicaMetals(Double meltingPoint) {
        MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.itemGroup);
        METAL = registrate.metal(this.name().toLowerCase(), meltingPoint);
    }
}
