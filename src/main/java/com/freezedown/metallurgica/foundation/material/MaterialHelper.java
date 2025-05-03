package com.freezedown.metallurgica.foundation.material;

import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

import static com.freezedown.metallurgica.registry.material.MetMaterialItems.MATERIAL_ITEMS;

public class MaterialHelper {

    public static List<ItemEntry<? extends Item>> getAllItems(Material material) {
        List<ItemEntry<? extends Item>> allItems = new ArrayList<>();
        for (var flag : FlagKey.getAllFlags()) {
            var item = MATERIAL_ITEMS.get(flag, material);
            if (item == null) continue;
            allItems.add(item);
        }
        return allItems;
    }

    public static ItemEntry<? extends Item> get(Material material, FlagKey<?> flagKey) {
        return MATERIAL_ITEMS.get(flagKey, material);
    }
}
