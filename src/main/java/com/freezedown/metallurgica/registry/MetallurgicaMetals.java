package com.freezedown.metallurgica.registry;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.foundation.material.MetalEntry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public enum MetallurgicaMetals {
    IRON (1538.0, "Fe"),
    GOLD (1064.2, "Au"),
    COPPER (1084.6, "Cu"),
    NETHERIUM(3962.0, "Nt"),
    NETHERITE(3562.0, "NtAu"),
    BRASS(920.0, "CuZn"),
    ALUMINUM (660.3, "Al"),
    LEAD (327.5, "Pb"),
    SILVER (961.8, "Ag"),
    NICKEL (1455.0, "Ni"),
    TIN (231.9, "Sn"),
    ZINC (419.5, "Zn"),
    PLATINUM (1768.3, "Pt"),
    TITANIUM (1668.0, "Ti"),
    URANIUM (1132.3, "U"),
    LITHIUM (180.5, "Li"),
    MAGNESIUM (650.0, "Mg"),
    TUNGSTEN (3422.0, "W"),
    OSMIUM (3033.0, "Os"),
    THORIUM (1750.0, "Th"),;

    public final MetalEntry METAL;

    MetallurgicaMetals(Double meltingPoint, String element) {
        MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.itemGroup);
        METAL = registrate.metal(this.name().toLowerCase(), meltingPoint, element);
        Map<String, String> composition = new HashMap<>();
        composition.put(this.name().toLowerCase(), element);
        MetallurgicaRegistrate.COMPOSITIONS.add(composition);
    }
    
    public MetalEntry getMetal() {
        return METAL;
    }
    
    public static void provideElementLang(BiConsumer<String, String> consumer) {
        for (MetallurgicaMetals metal : values()) {
            metal.getMetal().provideCompositionLang(consumer);
        }
    }
}
