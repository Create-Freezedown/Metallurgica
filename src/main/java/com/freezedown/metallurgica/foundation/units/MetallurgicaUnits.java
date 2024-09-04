package com.freezedown.metallurgica.foundation.units;

import com.simibubi.create.foundation.utility.Couple;

import java.util.function.BiConsumer;

public enum MetallurgicaUnits {
    MM(createCouple("millimeters", "mm"), createCouple("inches", "in"), 25.4),
    M(createCouple("meters", "m"), createCouple("feet", "ft"), 0.305),
    KG(createCouple("kilograms", "kg"), createCouple("pounds", "lbs"), 0.454),
    G(createCouple("grams", "g"), createCouple("ounces", "oz"), 28.35),
    C(createCouple("celsius", "°C"), createCouple("fahrenheit", "°F"), (double) 9 / 5 + 32),
    K(createCouple("kelvin", "K"), createCouple("rankine", "°R"), 1.8),
    L(createCouple("liters", "L"), createCouple("pints", "pt"), 1.76)
    ;
    
    public final UnitEntry UNIT;
    
    MetallurgicaUnits(Couple<String> metric, Couple<String> imperial, double toImperial) {
        UNIT = new UnitEntry(metric, imperial, toImperial);
    }
    
    public static Couple<String> createCouple(String first, String second) {
        return Couple.create(first, second);
    }
    
    public static void provideUnitLang(BiConsumer<String, String> consumer) {
        for (MetallurgicaUnits unit : values()) {
            unit.UNIT.provideLang(consumer);
        }
    }
}
