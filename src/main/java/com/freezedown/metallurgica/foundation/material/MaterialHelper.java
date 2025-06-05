package com.freezedown.metallurgica.foundation.material;

import com.freezedown.metallurgica.foundation.item.MaterialItem;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.ItemFlag;
import com.tterrag.registrate.util.entry.ItemEntry;

import java.util.ArrayList;
import java.util.List;

import static com.freezedown.metallurgica.registry.material.MetMaterialItems.MATERIAL_ITEMS;

public class MaterialHelper {

    public static List<ItemEntry<? extends MaterialItem>> getAllItems(Material material) {
        List<ItemEntry<? extends MaterialItem>> allItems = new ArrayList<>();
        for (var flagKey : material.getFlags().getFlagKeys()) {
            var flag = material.getFlag(flagKey);
            if (flag instanceof ItemFlag itemFlag) {
                var item = MATERIAL_ITEMS.get(itemFlag, material);
                if (item == null) continue;
                allItems.add(item);
            }
        }
        return allItems;
    }

    public static ItemEntry<? extends MaterialItem> get(Material material, ItemFlag flag) {
        return MATERIAL_ITEMS.get(flag, material);
    }
}
