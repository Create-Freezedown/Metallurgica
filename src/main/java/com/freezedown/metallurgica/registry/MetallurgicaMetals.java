package com.freezedown.metallurgica.registry;

import com.freezedown.metallurgica.content.fluids.types.MoltenMetal;
import com.freezedown.metallurgica.foundation.material.MetalEntry;
import com.tterrag.registrate.util.entry.FluidEntry;

import static com.freezedown.metallurgica.Metallurgica.registrate;

public enum MetallurgicaMetals {
    IRON (1538.0),
    GOLD (1064.2),
    COPPER (1084.6),
    NETHERITE(100000.0),
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
    public final FluidEntry<MoltenMetal.Flowing> MOLTEN;

    MetallurgicaMetals(Double meltingPoint) {
        METAL = new MetalEntry(meltingPoint);
        MOLTEN = registrate.moltenMetal(this.name().toLowerCase());
    }
}
