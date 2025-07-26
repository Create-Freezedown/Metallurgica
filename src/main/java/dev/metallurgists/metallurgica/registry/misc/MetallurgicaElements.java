package dev.metallurgists.metallurgica.registry.misc;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import dev.metallurgists.metallurgica.infastructure.element.Element;
import dev.metallurgists.metallurgica.infastructure.element.ElementEntry;
import dev.metallurgists.metallurgica.registry.MCreativeTabs;

public class MetallurgicaElements {
    private static final MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().setCreativeTab(MCreativeTabs.MAIN);

    public static final ElementEntry<Element>
            NULL = createElement("null", "?", 0xff171a2d),
            HYDROGEN = createElement("hydrogen", "H", 0xff9175dc),
            HELIUM = createElement("helium", "He", 0xfffcc6f7),
            LITHIUM = createElement("lithium", "Li", 0xff989890),
            BERYLLIUM = createElement("beryllium", "Be", 0xff838489),
            BORON = createElement("boron", "B", 0xff6d7079),
            CARBON = createElement("carbon", "C", 0xff626061),
            NITROGEN = createElement("nitrogen", "N", 0xffdfd9d9),
            OXYGEN = createElement("oxygen", "O", 0xffc7e4f6),
            FLUORINE = createElement("fluorine", "F", 0xffd0d97e),
            NEON = createElement("neon", "Ne", 0xffdb608f),
            SODIUM = createElement("sodium", "Na", 0xffc9bfbe),
            MAGNESIUM = createElement("magnesium", "Mg", 0xffb7b7b7),
            ALUMINUM = createElement("aluminum", "Al", 0xffcccfd4),
            SILICON = createElement("silicon", "Si", 0xff81848d),
            PHOSPHORUS = createElement("phosphorus", "P", 0xffa16567),
            SULFUR = createElement("sulfur", "S", 0xffdae096),
            CHLORINE = createElement("chlorine", "Cl", 0xffc1bb1f),
            ARGON = createElement("argon", "Ar", 0xffb93de9),
            POTASSIUM = createElement("potassium", "K", 0xff9aa3a2),
            CALCIUM = createElement("calcium", "Ca", 0xffb4ad9d),
            SCANDIUM = createElement("scandium", "Sc", 0xffa8a095),
            TITANIUM = createElement("titanium", "Ti", 0xffacada5),
            VANADIUM = createElement("vanadium", "V", 0xff99a1a4),
            CHROMIUM = createElement("chromium", "Cr", 0xffb4b7c0),
            MANGANESE = createElement("manganese", "Mn", 0xff746f6c),
            IRON = createElement("iron", "Fe", 0xff949496),
            COBALT = createElement("cobalt", "Co", 0xffb0afb5),
            NICKEL = createElement("nickel", "Ni", 0xffa3a29e),
            COPPER = createElement("copper", "Cu", 0xffdcb491),
            ZINC = createElement("zinc", "Zn", 0xffb5bdc0),
            GALLIUM = createElement("gallium", "Ga", 0xffb5c1cd),
            GERMANIUM = createElement("germanium", "Ge", 0xff7d8379),
            ARSENIC = createElement("arsenic", "As", 0xff92948f),
            SELENIUM = createElement("selenium", "Se", 0xff5f676a),
            BROMINE = createElement("bromine", "Br", 0xffd39131),
            KRYPTON = createElement("krypton", "Kr", 0xffc6b4e8),
            RUBIDIUM = createElement("rubidium", "Rb", 0xff9b9b93),
            STRONTIUM = createElement("strontium", "Sr", 0xff868782),
            YTTRIUM = createElement("yttrium", "Y", 0xffa8a095),
            ZIRCONIUM = createElement("zirconium", "Zr", 0xffafaaa7),
            NIOBIUM = createElement("niobium", "Nb", 0xff91908c),
            MOLYBDENUM = createElement("molybdenum", "Mo", 0xff878791),
            TECHNETIUM = createElement("technetium", "Tc", 0xff796f66),
            RUTHENIUM = createElement("ruthenium", "Ru", 0xffa2a2a4),
            RHODIUM = createElement("rhodium", "Rh", 0xffc7c2bf),
            PALLADIUM = createElement("palladium", "Pd", 0xffadacaa),
            SILVER = createElement("silver", "Ag", 0xffdddfda),
            CADMIUM = createElement("cadmium", "Cd", 0xffb4b4b4),
            INDIUM = createElement("indium", "In", 0xffd5d0cd),
            TIN = createElement("tin", "Sn", 0xffc2c2c2),
            ANTIMONY = createElement("antimony", "Sb", 0xffa7afb2),
            TELLURIUM = createElement("tellurium", "Te", 0xff827d7a),
            IODINE = createElement("iodine", "I", 0xff983087),
            XENON = createElement("xenon", "Xe", 0xff7299f6),
            CESIUM = createElement("cesium", "Cs", 0xffb2aa7c),
            BARIUM = createElement("barium", "Ba", 0xff676964),
            LANTHANUM = createElement("lanthanum", "La", 0xff8c8d92),
            CERIUM = createElement("cerium", "Ce", 0xff7b7c74),
            PRASEODYMIUM = createElement("praseodymium", "Pr", 0xff99989e),
            NEODYMIUM = createElement("neodymium", "Nd", 0xff727473),
            PROMETHIUM = createElement("promethium", "Pm", 0xff32323c),
            SAMARIUM = createElement("samarium", "Sm", 0xff515257),
            EUROPIUM = createElement("europium", "Eu", 0xff838b8e),
            GADOLINIUM = createElement("gadolinium", "Gd", 0xff959589),
            TERBIUM = createElement("terbium", "Tb", 0xffa4a3a1),
            DYSPROSIUM = createElement("dysprosium", "Dy", 0xff8e8986),
            HOLMIUM = createElement("holmium", "Ho", 0xff9a9a92),
            ERBIUM = createElement("erbium", "Er", 0xff9e9f97),
            THULIUM = createElement("thulium", "Tm", 0xff8e8c8d),
            YTTERBIUM = createElement("ytterbium", "Yb", 0xff979799),
            LUTETIUM = createElement("lutetium", "Lu", 0xffa6a6a4),
            HAFNIUM = createElement("hafnium", "Hf", 0xffa19c99),
            TANTALUM = createElement("tantalum", "Ta", 0xff8d9695),
            TUNGSTEN = createElement("tungsten", "W", 0xff797876),
            RHENIUM = createElement("rhenium", "Re", 0xffa19fac),
            OSMIUM = createElement("osmium", "Os", 0xff95a6ad),
            IRIDIUM = createElement("iridium", "Ir", 0xffaba1a0),
            PLATINUM = createElement("platinum", "Pt", 0xffc5c4c0),
            GOLD = createElement("gold", "Au", 0xffd1c186),
            MERCURY = createElement("mercury", "Hg", 0xff898a8c),
            THALLIUM = createElement("thallium", "Tl", 0xff7d7a81),
            LEAD = createElement("lead", "Pb", 0xff8f929b),
            BISMUTH = createElement("bismuth", "Bi", 0xffbcb6b6),
            POLONIUM = createElement("polonium", "Po", 0xff30333c),
            ASTATINE = createElement("astatine", "At", 0xff2a2a2a),
            RADON = createElement("radon", "Rn", 0xff2e313a),
            FRANCIUM = createElement("francium", "Fr", 0xff262626),
            RADAIUM = createElement("radium", "Ra", 0xffa79a87),
            ACTINIUM = createElement("actinium", "Ac", 0xff2b2d39),
            THORIUM = createElement("thorium", "Th", 0xff7e807d),
            PROTACTINIUM = createElement("protactinium", "Pa", 0xff48525e),
            URANIUM = createElement("uranium", "U", 0xff85807d),
            NEPTUNIUM = createElement("neptunium", "Np", 0xff9d9892),
            PLUTONIUM = createElement("plutonium", "Pu", 0xff6f3d40),
            AMERICIUM = createElement("americium", "Am", 0xff606166),
            CURIUM = createElement("curium", "Cm", 0xff949085),
            BERKELIUM = createElement("berkelium", "Bk", 0xff787775),
            CALIFORNIUM = createElement("californium", "Cf", 0xff8c8686),
            EINSTEINIUM = createElement("einsteinium", "Es", 0xff333439),
            FERMIUM = createElement("fermium", "Fm", 0xff292c35),
            MENDELEVIUM = createElement("mendelevium", "Md", 0xff2e2d3d),
            NOBELIUM = createElement("nobelium", "No", 0xff242424),
            LAWRENCIUM = createElement("lawrencium", "Lr", 0xff242424),
            RUTHERFORDIUM = createElement("rutherfordium", "Rf", 0xff2a2529),
            DUBNIUM = createElement("dubnium", "Db", 0xff2b2f38),
            SEABORGIUM = createElement("seaborgium", "Sg", 0xff262626),
            BOHRIUM = createElement("bohrum", "Bh", 0xff282629),
            HASSIUM = createElement("hassium", "Hs", 0xff282828),
            MEITNERIUM = createElement("meitnerium", "Mt", 0xff262628),
            DARMSTADTIUM = createElement("darmstadtium", "Ds", 0xff262626),
            ROENTGENIUM = createElement("roentgenium", "Rg", 0xff262427),
            COPERNICIUM = createElement("copernicium", "Cn", 0xff262626),
            NIHONIUM = createElement("nihonium", "Nh", 0xff252328),
            FLEROVIUM = createElement("flerovium", "Fl", 0xff2b2b2b),
            MOSCOVIUM = createElement("moscovium", "Mc", 0xff242426),
            LIVERMORIUM = createElement("livermorium", "Lv", 0xff292728),
            TENNESSINE = createElement("tennessine", "Ts", 0xff262628),
            OGANESSON = createElement("oganesson", "Og", 0xff2a2a2a),
            NETHERIUM = createElement("netherium", "Nt", 0xff333756);



    private static ElementEntry<Element> createElement(String name, String symbol) {
        return registrate.element(name, symbol, Element::new).register();
    }

    private static ElementEntry<Element> createElement(String name, String symbol, int color) {
        return registrate.element(name, symbol, Element::new).properties(p -> p.color(color)).register();
    }


    public static void register() {

    }
}
