package com.freezedown.metallurgica.registry;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.MetallurgicaRegistrate;
import com.freezedown.metallurgica.foundation.item.MetallurgicaItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

public class MetallurgicaItems {
    private static final MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.itemGroup);

    public static final ItemEntry<Item>
            armorPlatingMold =    registrate.item("armor_plating_mold", Item::new, p->p.stacksTo(1).fireResistant(), "advanced_casting_molds/armor_plating", "advanced_casting_molds");
    
    //MISC ITEMS
    public static final ItemEntry<MetallurgicaItem>
            salt =                registrate.metallurgicaItem("salt", "salt"),

    //MAGNETITE PROCESSING
            richMagnetite =       registrate.metallurgicaItem("rich_magnetite", "enriched_materials/magnetite", "enriched_materials"),
            magnetiteLumps =      registrate.metallurgicaItem("magnetite_lumps", "lumps/magnetite", "lumps"),
    
    //BAUXITE PROCESSING
            washedAlumina =       registrate.metallurgicaItem("washed_alumina", "washed_materials/alumina", "washed_materials"),
            alumina =             registrate.metallurgicaItem("alumina", "alumina"),
            aluminumDust =        registrate.metallurgicaItem("aluminum_dust", "dusts/aluminum", "dusts");

    public static final ItemEntry<Item>
            loosenedBauxite =     registrate.simpleItem("loosened_bauxite", "loosened_materials/bauxite", "loosened_materials"); //why is this a normal item???


    public static void register() {
    }
}
