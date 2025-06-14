package com.freezedown.metallurgica.registry.material.init;

import com.freezedown.metallurgica.foundation.item.MaterialItem;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.ItemFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.RegisterElsewhere;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.registry.material.MetMaterials;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.tterrag.registrate.util.entry.ItemEntry;

public class MetMaterialItems {
    public static ImmutableTable.Builder<FlagKey<?>, Material, ItemEntry<? extends MaterialItem>> MATERIAL_ITEMS_BUILDER = ImmutableTable.builder();

    public static Table<FlagKey<?>, Material, ItemEntry<? extends MaterialItem>> MATERIAL_ITEMS;

    public static void generateMaterialItems(MetallurgicaRegistrate registrate) {
        for (Material material : MetMaterials.registeredMaterials.values()) {
            for (FlagKey<?> flagKey : FlagKey.getAllFlags()) {
                var flag = material.getFlag(flagKey);
                if (!material.noRegister(flagKey)) {
                    if (flag instanceof ItemFlag itemFlag) {
                        if (itemFlag.getClass().isAnnotationPresent(RegisterElsewhere.class)) continue;
                        registerMaterialItem(material, itemFlag, flagKey, registrate);
                    }
                }
            }
        }
    }

    public static void registerMaterialItem(Material material, ItemFlag flag, FlagKey<?> flagKey, MetallurgicaRegistrate registrate) {
        MATERIAL_ITEMS_BUILDER.put(flagKey, material, flag.registerItem(material, flag, registrate));
    }

}
