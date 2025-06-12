package com.freezedown.metallurgica.registry.material;

import com.freezedown.metallurgica.foundation.item.MaterialItem;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.ItemFlag;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.registry.MetallurgicaCreativeTab;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.createmod.catnip.data.Pair;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.freezedown.metallurgica.Metallurgica.registrate;

public class MetMaterialItems {
    public static ImmutableTable.Builder<FlagKey<?>, Material, ItemEntry<? extends MaterialItem>> MATERIAL_ITEMS_BUILDER = ImmutableTable.builder();

    public static Table<FlagKey<?>, Material, ItemEntry<? extends MaterialItem>> MATERIAL_ITEMS;

    public static void generateMaterialItems(MetallurgicaRegistrate registrate) {
        for (Material material : MetMaterials.registeredMaterials.values()) {
            for (FlagKey<?> flagKey : FlagKey.getAllFlags()) {
                var flag = material.getFlag(flagKey);
                if (!material.noRegister(flagKey)) {
                    if (flag instanceof ItemFlag itemFlag) {
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
