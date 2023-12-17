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
    public static void register() {
    }
}
