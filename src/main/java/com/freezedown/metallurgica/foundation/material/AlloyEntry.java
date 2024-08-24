package com.freezedown.metallurgica.foundation.material;

import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.foundation.item.AlloyItem;
import com.tterrag.registrate.util.entry.ItemEntry;

public class AlloyEntry {
    private final String name;
    private final ItemEntry<AlloyItem> ingot;
    private final ItemEntry<AlloyItem> nugget;

    public AlloyEntry(MetallurgicaRegistrate reg, String pName) {
        name = pName;
        ingot = reg.alloyItem(pName + "_ingot");
        nugget = reg.alloyItem(pName + "_nugget");
    }
    
    public String getName() {
        return name;
    }
    
    public ItemEntry<AlloyItem> getIngot() {
        return ingot;
    }
    
    public ItemEntry<AlloyItem> getNugget() {
        return nugget;
    }
}
