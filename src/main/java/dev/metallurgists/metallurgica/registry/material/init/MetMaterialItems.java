package dev.metallurgists.metallurgica.registry.material.init;

import dev.metallurgists.metallurgica.foundation.material.item.IMaterialItem;
import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.RegisterElsewhere;
import dev.metallurgists.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.interfaces.IItemRegistry;
import dev.metallurgists.metallurgica.registry.material.MetMaterials;
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
