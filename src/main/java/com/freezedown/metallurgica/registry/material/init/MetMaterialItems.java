package com.freezedown.metallurgica.registry.material.init;

import com.freezedown.metallurgica.foundation.material.item.IMaterialItem;
import com.freezedown.metallurgica.foundation.material.item.MaterialItem;
import com.freezedown.metallurgica.infastructure.material.Material;
import com.freezedown.metallurgica.infastructure.material.registry.flags.FlagKey;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.ItemFlag;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.RegisterElsewhere;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.interfaces.IItemRegistry;
import com.freezedown.metallurgica.registry.material.MetMaterials;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.tterrag.registrate.util.entry.ItemEntry;

public class MetMaterialItems {
    public static ImmutableTable.Builder<FlagKey<?>, Material, ItemEntry<? extends IMaterialItem>> MATERIAL_ITEMS_BUILDER = ImmutableTable.builder();

    public static Table<FlagKey<?>, Material, ItemEntry<? extends IMaterialItem>> MATERIAL_ITEMS;

    public static void generateMaterialItems(MetallurgicaRegistrate registrate) {
        for (Material material : MetMaterials.registeredMaterials.values()) {
            for (FlagKey<?> flagKey : FlagKey.getAllFlags()) {
                var flag = material.getFlag(flagKey);
                if (!material.noRegister(flagKey)) {
                    if (flag instanceof IItemRegistry itemRegistry) {
                        if (flag.getClass().isAnnotationPresent(RegisterElsewhere.class)) continue;
                        registerMaterialItem(material, itemRegistry, flagKey, registrate);
                    }
                }
            }
        }
    }

    public static void registerMaterialItem(Material material, IItemRegistry itemRegistry, FlagKey<?> flagKey, MetallurgicaRegistrate registrate) {
        MATERIAL_ITEMS_BUILDER.put(flagKey, material, itemRegistry.registerItem(material, itemRegistry, registrate));
    }

}
