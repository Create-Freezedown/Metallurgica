package com.freezedown.metallurgica.registry;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.MetallurgicaRegistrate;
import com.simibubi.create.AllTags;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

public class MetallurgicaItems {
    private static final MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.itemGroup);
    
    public static final ItemEntry<Item> magnetite = registrate.item("magnetite", Item::new)
            .properties(p->p)
            .tag(AllTags.forgeItemTag("raw_materials/magnetite"))
            .tag(AllTags.forgeItemTag("raw_materials"))
            .register();
    public static final ItemEntry<Item> nativeCopper = registrate.item("native_copper", Item::new)
            .properties(p->p)
            .tag(AllTags.forgeItemTag("raw_materials/native_copper"))
            .tag(AllTags.forgeItemTag("raw_materials"))
            .register();
    public static final ItemEntry<Item> bauxite = registrate.item("bauxite", Item::new)
            .properties(p->p)
            .tag(AllTags.forgeItemTag("raw_materials/bauxite"))
            .tag(AllTags.forgeItemTag("raw_materials"))
            .register();
    public static final ItemEntry<Item> nativeGold = registrate.item("native_gold", Item::new)
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
    public static void register() {
    }
}
