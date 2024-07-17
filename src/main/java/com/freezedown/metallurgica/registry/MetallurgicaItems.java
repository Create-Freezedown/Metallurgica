package com.freezedown.metallurgica.registry;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.MetallurgicaRegistrate;
import com.freezedown.metallurgica.foundation.item.MetallurgicaItem;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
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


    public static final ItemEntry<MetallurgicaItem> bauxite = registrate.item("bauxite", p -> new MetallurgicaItem(p).showElementComposition())
            .properties(p->p)
            .tag(AllTags.forgeItemTag("raw_materials/bauxite"))
            .tag(AllTags.forgeItemTag("raw_materials"))
            .register();
    public static final ItemEntry<Item> loosenedBauxite = registrate.item("loosened_bauxite", Item::new)
            .properties(p->p)
            .tag(AllTags.forgeItemTag("loosened_materials/bauxite"))
            .tag(AllTags.forgeItemTag("loosened_materials"))
            .register();
    public static final ItemEntry<MetallurgicaItem> washedAlumina = registrate.item("washed_alumina", p -> new MetallurgicaItem(p).showElementComposition())
            .properties(p->p)
            .tag(AllTags.forgeItemTag("washed_materials/alumina"))
            .tag(AllTags.forgeItemTag("washed_materials"))
            .register();
    public static final ItemEntry<MetallurgicaItem> alumina = registrate.item("alumina", p -> new MetallurgicaItem(p).showElementComposition())
            .properties(p->p)
            .tag(AllTags.forgeItemTag("alumina"))
            .register();
    public static final ItemEntry<MetallurgicaItem> aluminumDust = registrate.item("aluminum_dust", p -> new MetallurgicaItem(p).showElementComposition())
            .properties(p->p)
            .tag(AllTags.forgeItemTag("dusts/aluminum"))
            .tag(AllTags.forgeItemTag("dusts"))
            .register();

    //TIN PROCESSING
    public static final ItemEntry<MetallurgicaItem> cassiterite = registrate.item("cassiterite", p -> new MetallurgicaItem(p).showElementComposition())
            .properties(p->p)
            .tag(AllTags.forgeItemTag("raw_materials/cassiterite"))
            .tag(AllTags.forgeItemTag("raw_materials"))
            .register();
    public static final ItemEntry<MetallurgicaItem> alluvialCassiterite = registrate.item("alluvial_cassiterite", p -> new MetallurgicaItem(p).showElementComposition())
            .properties(p->p)
            .tag(AllTags.forgeItemTag("alluvial_materials/cassiterite"))
            .tag(AllTags.forgeItemTag("alluvial_materials"))
            .register();

    //COPPER PROCESSING
    public static final ItemEntry<MetallurgicaItem> malachite = registrate.item("malachite", p -> new MetallurgicaItem(p).showElementComposition())
            .properties(p->p)
            .tag(AllTags.forgeItemTag("raw_materials/malachite"))
            .tag(AllTags.forgeItemTag("raw_materials"))
            .register();
    public static final ItemEntry<MetallurgicaItem> richMalachite = registrate.item("rich_malachite", p -> new MetallurgicaItem(p).showElementComposition())
            .properties(p->p)
            .tag(AllTags.forgeItemTag("enriched_materials/malachite"))
            .tag(AllTags.forgeItemTag("enriched_materials"))
            .register();

    public static final ItemEntry<MetallurgicaItem> copperOxide = registrate.item("copper_oxide", p -> new MetallurgicaItem(p).showElementComposition())
            .properties(p->p)
            .tag(AllTags.forgeItemTag("dusts/copper_oxide"))
            .tag(AllTags.forgeItemTag("dusts"))
            .register();
    public static final ItemEntry<MetallurgicaItem> copperRubble = registrate.item("copper_rubble", p -> new MetallurgicaItem(p).showElementComposition())
            .properties(p->p)
            .tag(AllTags.forgeItemTag("material_rubble/copper"))
            .tag(AllTags.forgeItemTag("material_rubble"))
            .register();

    public static void register() {
    }
}
