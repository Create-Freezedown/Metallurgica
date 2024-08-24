package com.freezedown.metallurgica.registry;

import com.freezedown.metallurgica.foundation.item.composition.Element;

import java.util.function.BiConsumer;

public enum MetallurgicaElements {
    HYDROGEN("H"),
    HELIUM("He"),
    LITHIUM("Li"),
    BERYLLIUM("Be"),
    BORON("B"),
    CARBON("C"),
    NITROGEN("N"),
    OXYGEN("O"),
    FLUORINE("F"),
    NEON("Ne"),
    SODIUM("Na"),
    MAGNESIUM("Mg"),
    ALUMINUM("Al"),
    SILICON("Si"),
    PHOSPHORUS("P"),
    SULFUR("S"),
    CHLORINE("Cl"),
    ARGON("Ar"),
    POTASSIUM("K"),
    CALCIUM("Ca"),
    SCANDIUM("Sc"),
    TITANIUM("Ti"),
    VANADIUM("V"),
    CHROMIUM("Cr"),
    MANGANESE("Mn"),
    IRON("Fe"),
    COBALT("Co"),
    NICKEL("Ni"),
    COPPER("Cu"),
    ZINC("Zn"),
    GALLIUM("Ga"),
    GERMANIUM("Ge"),
    ARSENIC("As"),
    SELENIUM("Se"),
    BROMINE("Br"),
    KRYPTON("Kr"),
    RUBIDIUM("Rb"),
    STRONTIUM("Sr"),
    YTTRIUM("Y"),
    ZIRCONIUM("Zr"),
    NIOBIUM("Nb"),
    MOLYBDENUM("Mo"),
    TECHNETIUM("Tc"),
    RUTHENIUM("Ru"),
    RHODIUM("Rh"),
    PALLADIUM("Pd"),
    SILVER("Ag"),
    CADMIUM("Cd"),
    INDIUM("In"),
    TIN("Sn"),
    ANTIMONY("Sb"),
    TELLURIUM("Te"),
    IODINE("I"),
    XENON("Xe"),
    CESIUM("Cs"),
    BARIUM("Ba"),
    LANTHANUM("La"),
    CERIUM("Ce"),
    PRASEODYMIUM("Pr"),
    NEODYMIUM("Nd"),
    PROMETHIUM("Pm"),
    SAMARIUM("Sm"),
    EUROPIUM("Eu"),
    GADOLINIUM("Gd"),
    TERBIUM("Tb"),
    DYSPROSIUM("Dy"),
    HOLMIUM("Ho"),
    ERBIUM("Er"),
    THULIUM("Tm"),
    YTTERBIUM("Yb"),
    LUTETIUM("Lu"),
    HAFNIUM("Hf"),
    TANTALUM("Ta"),
    TUNGSTEN("W"),
    RHENIUM("Re"),
    OSMIUM("Os"),
    IRIDIUM("Ir"),
    PLATINUM("Pt"),
    GOLD("Au"),
    MERCURY("Hg"),
    THALLIUM("Tl"),
    LEAD("Pb"),
    BISMUTH("Bi"),
    POLONIUM("Po"),
    ASTATINE("At"),
    RADON("Rn"),
    FRANCIUM("Fr"),
    RADIUM("Ra"),
    ACTINIUM("Ac"),
    THORIUM("Th"),
    PROTACTINIUM("Pa"),
    URANIUM("U"),
    NEPTUNIUM("Np"),
    PLUTONIUM("Pu"),
    AMERICIUM("Am"),
    CURIUM("Cm"),
    BERKELIUM("Bk"),
    CALIFORNIUM("Cf"),
    EINSTEINIUM("Es"),
    FERMIUM("Fm"),
    MENDELEVIUM("Md"),
    NOBELIUM("No"),
    LAWRENCIUM("Lr"),
    RUTHERFORDIUM("Rf"),
    DUBNIUM("Db"),
    SEABORGIUM("Sg"),
    BOHRIUM("Bh"),
    HASSIUM("Hs"),
    MEITNERIUM("Mt"),
    DARMSTADTIUM("Ds"),
    ROENTGENIUM("Rg"),
    COPERNICIUM("Cn"),
    NIHONIUM("Nh"),
    FLEROVIUM("Fl"),
    MOSCOVIUM("Mc"),
    LIVERMORIUM("Lv"),
    TENNESSINE("Ts"),
    OGANESSON("Og"),
    NETHERIUM("Nt"),
    ;
    
    public final Element ELEMENT;
    public final String symbol;
    
    MetallurgicaElements(String pSymbol) {
        ELEMENT = Element.create(name().toLowerCase());
        symbol = pSymbol;
    }
    
    public static void provideElementLang(BiConsumer<String, String> consumer) {
        for (MetallurgicaElements element : values()) {
            consumer.accept("metallurgica.element." + element.ELEMENT.getName(), getSymbol(element.ELEMENT.getName()));
        }
    }
    
    public Element getElement(String name) {
        for (MetallurgicaElements element : values()) {
            if (element.ELEMENT.getName().equals(name)) {
                return element.ELEMENT;
            }
        }
        return null;
    }
    
    public static String getSymbol(String name) {
        for (MetallurgicaElements element : values()) {
            if (element.ELEMENT.getName().toLowerCase().equals(name)) {
                return element.symbol;
            }
        }
        return null;
    }
}
