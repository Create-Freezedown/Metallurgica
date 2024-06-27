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
    
    
    public static final ItemEntry<MetallurgicaItem> nativeCopper = registrate.item("native_copper", p -> new MetallurgicaItem(p).showElementComposition())
            .properties(p->p)
            .tag(AllTags.forgeItemTag("raw_materials/native_copper"))
            .tag(AllTags.forgeItemTag("raw_materials"))
            .register();
    
    public static final ItemEntry<MetallurgicaItem> nativeGold = registrate.item("native_gold", p -> new MetallurgicaItem(p).showElementComposition())
            .properties(p->p)
            .tag(AllTags.forgeItemTag("raw_materials/native_gold"))
            .tag(AllTags.forgeItemTag("raw_materials"))
            .register();
    
    public static final ItemEntry<Item> magnetiteRubble = registrate.item("magnetite_rubble", Item::new)
            .properties(p->p)
            .tag(AllTags.forgeItemTag("material_rubble/magnetite"))
            .tag(AllTags.forgeItemTag("material_rubble"))
            .register();
    public static final ItemEntry<Item> nativeCopperRubble = registrate.item("native_copper_rubble", Item::new)
            .properties(p->p)
            .tag(AllTags.forgeItemTag("material_rubble/native_copper"))
            .tag(AllTags.forgeItemTag("material_rubble"))
            .register();
    public static final ItemEntry<Item> bauxiteRubble = registrate.item("bauxite_rubble", Item::new)
            .properties(p->p)
            .tag(AllTags.forgeItemTag("material_rubble/bauxite"))
            .tag(AllTags.forgeItemTag("material_rubble"))
            .register();
    public static final ItemEntry<Item> nativeGoldRubble = registrate.item("native_gold_rubble", Item::new)
            .properties(p->p)
            .tag(AllTags.forgeItemTag("material_rubble/native_gold"))
            .tag(AllTags.forgeItemTag("material_rubble"))
            .register();
    
    public static final ItemEntry<Item> armorPlatingMold = registrate.item("armor_plating_mold", Item::new)
            .properties(p->p.stacksTo(1).fireResistant())
            .tag(AllTags.forgeItemTag("advanced_casting_molds/armor_plating"))
            .tag(AllTags.forgeItemTag("advanced_casting_molds"))
            .register();
    
    //MAGNETITE PROCESSING
    public static final ItemEntry<MetallurgicaItem> magnetite = registrate.item("magnetite", p -> new MetallurgicaItem(p).showElementComposition())
            .properties(p->p)
            .tag(AllTags.forgeItemTag("raw_materials/magnetite"))
            .tag(AllTags.forgeItemTag("raw_materials"))
            .register();
    public static final ItemEntry<MetallurgicaItem> richMagnetite = registrate.item("rich_magnetite", p -> new MetallurgicaItem(p).showElementComposition())
            .properties(p->p)
            .tag(AllTags.forgeItemTag("enriched_materials/magnetite"))
            .tag(AllTags.forgeItemTag("enriched_materials"))
            .register();
    public static final ItemEntry<MetallurgicaItem> magnetiteLumps = registrate.item("magnetite_lumps", p -> new MetallurgicaItem(p).showElementComposition())
            .properties(p->p)
            .tag(AllTags.forgeItemTag("lumps/magnetite"))
            .tag(AllTags.forgeItemTag("lumps"))
            .register();
    
    //BAUXITE PROCESSING
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
    public static void register() {
    }
}
