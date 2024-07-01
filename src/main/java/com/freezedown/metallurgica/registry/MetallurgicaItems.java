package com.freezedown.metallurgica.registry;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.MetallurgicaRegistrate;
import com.freezedown.metallurgica.foundation.item.MetallurgicaItem;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.apache.logging.log4j.util.LambdaUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MetallurgicaItems {
    private static final MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.itemGroup);
    
    public static final ItemEntry<MetallurgicaItem>
            nativeCopper = metallurgicaItem("native_copper",  "raw_materials/native_copper", "raw_materials"),
            nativeGold = metallurgicaItem("native_gold", "raw_materials/native_gold", "raw_materials");
    
    public static final ItemEntry<Item>
            magnetiteRubble = simpleItem("magnetite_rubble", "material_rubble/magnetite", "material_rubble"),
            nativeCopperRubble = simpleItem("native_copper_rubble", "material_rubble/native_copper", "material_rubble"),
            bauxiteRubble = simpleItem("bauxite_rubble", "material_rubble/bauxite", "material_rubble"),
            nativeGoldRubble = simpleItem("native_gold_rubble", "material_rubble/native_gold", "material_rubble"),
            armorPlatingMold = item("armor_plating_mold", Item::new, p->p.stacksTo(1).fireResistant(), "advanced_casting_molds/armor_plating", "advanced_casting_molds");
    
    //MISC ITEMS
    public static final ItemEntry<MetallurgicaItem>
            salt = metallurgicaItem("salt", "salt"),

    //MAGNETITE PROCESSING
            magnetite = metallurgicaItem("magnetite", "raw_materials/magnetite", "raw_materials"),
            richMagnetite = metallurgicaItem("rich_magnetite", "enriched_materials/magnetite", "enriched_materials"),
            magnetiteLumps = metallurgicaItem("magnetite_lumps", "lumps/magnetite", "lumps"),
    
    //BAUXITE PROCESSING
            bauxite = metallurgicaItem("bauxite", "raw_materials/bauxite",  "raw_materials");

    public static final ItemEntry<Item>
            loosenedBauxite = simpleItem("loosened_bauxite", "loosened_materials/bauxite", "loosened_materials"); //why is this a normal item???

    public static final ItemEntry<MetallurgicaItem>
            washedAlumina = metallurgicaItem("washed_alumina", "washed_materials/alumina", "washed_materials"),
            alumina = metallurgicaItem("alumina", "alumina"),
            aluminumDust = metallurgicaItem("aluminum_dust", "dusts/aluminum", "dusts");

    private static <T extends Item> ItemEntry<T> item(String name, NonNullFunction<Item.Properties, T> factory, NonNullUnaryOperator<Item.Properties> properties, String... tags) {
        ItemBuilder<T, ?> builder = registrate.item(name, factory).properties(properties);
        for(String tag : tags) {
            builder.tag(AllTags.forgeItemTag(tag));
        }
        return builder.register();
    }

    private static ItemEntry<Item> simpleItem(String name, String... tags) {
        return item(name, Item::new, p->p, tags);
    }

    private static ItemEntry<MetallurgicaItem> metallurgicaItem(String name, String... tags) {
        return item(name, p -> new MetallurgicaItem(p).showElementComposition(), p->p, tags);
    }

    public static void register() {
    }
}
