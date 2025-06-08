package com.freezedown.metallurgica.foundation.material;

import com.freezedown.metallurgica.foundation.block.MaterialBlock;
import com.freezedown.metallurgica.foundation.item.MaterialItem;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.BlockFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.ItemFlag;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;

import java.util.ArrayList;
import java.util.List;

import static com.freezedown.metallurgica.registry.material.MetMaterialBlocks.MATERIAL_BLOCKS;
import static com.freezedown.metallurgica.registry.material.MetMaterialItems.MATERIAL_ITEMS;

public class MaterialHelper {

    public static List<ItemEntry<? extends MaterialItem>> getAllItems(Material material) {
        List<ItemEntry<? extends MaterialItem>> allItems = new ArrayList<>();
        for (var flagKey : material.getFlags().getFlagKeys()) {
            var flag = material.getFlag(flagKey);
            if (flag instanceof ItemFlag) {
                var item = MATERIAL_ITEMS.get(flagKey, material);
                if (item == null) continue;
                allItems.add(item);
            }
        }
        return allItems;
    }

    public static ItemEntry<? extends MaterialItem> getItem(Material material, FlagKey<?> flagKey) {
        var flag = material.getFlag(flagKey);
        if (flag instanceof ItemFlag) {
            return MATERIAL_ITEMS.get(flagKey, material);
        } else {
            throw new IllegalArgumentException("Flag " + flagKey + " is not an ItemFlag for material: " + material.getName());
        }
    }

    public static BlockEntry<? extends MaterialBlock> getBlock(Material material, FlagKey<?> flagKey) {
        var flag = material.getFlag(flagKey);
        if (flag instanceof BlockFlag) {
            return MATERIAL_BLOCKS.get(flagKey, material);
        } else {
            throw new IllegalArgumentException("Flag " + flagKey + " is not a BlockFlag for material: " + material.getName());
        }
    }
}
