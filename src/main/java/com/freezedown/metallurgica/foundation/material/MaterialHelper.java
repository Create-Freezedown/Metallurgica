package com.freezedown.metallurgica.foundation.material;

import com.freezedown.metallurgica.foundation.config.TFMGConductor;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.CableFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.ItemFlag;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.infastructure.conductor.Conductor;
import com.freezedown.metallurgica.registry.MetallurgicaCreativeTab;
import com.freezedown.metallurgica.registry.misc.MetallurgicaMaterials;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.createmod.catnip.data.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.freezedown.metallurgica.Metallurgica.registrate;

@SuppressWarnings("unchecked")
public class MaterialHelper {
    public static final Map<Material, List<Pair<FlagKey<?>, Supplier<? extends ItemLike>>>> items = new HashMap<>();
    static ImmutableTable.Builder<FlagKey<? extends ItemFlag>, Material, ItemEntry<? extends Item>> MATERIAL_ITEMS_BUILDER = ImmutableTable.builder();
    public static Table<FlagKey<? extends ItemFlag>, Material, ItemEntry<? extends Item>> MATERIAL_ITEMS;

    public static void registerMaterialItems() {
        registrate.setCreativeTab(MetallurgicaCreativeTab.MAIN_TAB);
        for (var flag : FlagKey.getAllFlags()) {
            if (flag != FlagKey.EMPTY) {
                for (MetallurgicaMaterials material : MetallurgicaMaterials.values()) {
                    Material mat = material.getMaterial();
                    if (!mat.hasFlag(flag)) continue;
                    if (!mat.noRegister(flag)) {
                        if (flag.constructDefault() instanceof ItemFlag) {
                            registerMaterialItem(material.getMaterial(), (FlagKey<? extends ItemFlag>) flag, registrate);
                        }
                    }
                }
            }
        }
        MATERIAL_ITEMS = MATERIAL_ITEMS_BUILDER.build();
    }

    public static void registerMaterialItem(Material material, FlagKey<? extends ItemFlag> itemFlag, MetallurgicaRegistrate registrate) {
        MATERIAL_ITEMS_BUILDER.put(itemFlag, material, registrate
                .item(itemFlag.constructDefault().getIdPattern().formatted(material.getName()), itemFlag.constructDefault().getFactory())
                .model((ctx, prov) -> prov.generated(ctx, matAssetLoc(material, itemFlag.toString())))
                .register()
        );
    }

    public static List<ItemEntry<? extends Item>> getAllItems(Material material) {
        List<ItemEntry<? extends Item>> allItems = new ArrayList<>();
        for (var flag : FlagKey.getAllFlags()) {
            allItems.add(MATERIAL_ITEMS.get(flag, material));
        }
        return allItems;
    }

    public static ItemEntry<? extends Item> get(Material material, FlagKey<?> flagKey) {
        return MATERIAL_ITEMS.get(flagKey, material);
    }

    public static ResourceLocation matAssetLoc(Material material, String name) {
        return new ResourceLocation(material.getModid(), "item/materials/"+material.getName()+"/" + name);
    }
}
