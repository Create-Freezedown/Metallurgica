package com.freezedown.metallurgica.registry.material;

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
    public static final Map<Material, List<Pair<FlagKey<?>, Supplier<? extends ItemLike>>>> items = new HashMap<>();
    public static ImmutableTable.Builder<FlagKey<? extends ItemFlag>, Material, ItemEntry<? extends Item>> MATERIAL_ITEMS_BUILDER = ImmutableTable.builder();


    public static Table<FlagKey<? extends ItemFlag>, Material, ItemEntry<? extends Item>> MATERIAL_ITEMS;

    public static void generateMaterialItems(MetallurgicaRegistrate registrate) {
        for (Material material : MetMaterials.registeredMaterials.values()) {
            for (FlagKey<?> flag : FlagKey.getAllFlags()) {
                if (!material.hasFlag(flag)) continue;
                if (!material.noRegister(flag)) {
                    if (flag.constructDefault() instanceof ItemFlag) {
                        registerMaterialItem(material, (FlagKey<? extends ItemFlag>) flag, registrate);
                    }
                }
            }
        }
    }

    public static void registerMaterialItem(Material material, FlagKey<? extends ItemFlag> itemFlag, MetallurgicaRegistrate registrate) {
        MATERIAL_ITEMS_BUILDER.put(itemFlag, material, registrate
                .item(itemFlag.constructDefault().getIdPattern().formatted(material.getName()), itemFlag.constructDefault().getFactory())
                .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
                .register()
        );
    }

}
