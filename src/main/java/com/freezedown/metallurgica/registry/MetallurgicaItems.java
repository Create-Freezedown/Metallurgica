package com.freezedown.metallurgica.registry;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.MetallurgicaRegistrate;
import com.freezedown.metallurgica.foundation.item.AlloyItem;
import com.freezedown.metallurgica.foundation.item.MetallurgicaItem;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

import static com.drmangotea.createindustry.CreateTFMG.REGISTRATE;

public class MetallurgicaItems {
    private static final MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.itemGroup);
    
    private static ItemEntry<SequencedAssemblyItem> sequencedIngredient(String name) {
        return registrate.item(name, SequencedAssemblyItem::new)
                .register();
    }

    public static final ItemEntry<Item>
            armorPlatingMold =    registrate.item("armor_plating_mold", Item::new, p->p.stacksTo(1).fireResistant(), "advanced_casting_molds/armor_plating", "advanced_casting_molds");

    //MISC ITEMS
    public static final ItemEntry<MetallurgicaItem>
            salt =                registrate.metallurgicaItem("salt", "salt"),

    //MAGNETITE PROCESSING
            richMagnetite =       registrate.metallurgicaItem("rich_magnetite", "enriched_materials/magnetite", "enriched_materials"),
            magnetiteLumps =      registrate.metallurgicaItem("magnetite_lumps", "lumps/magnetite", "lumps"),

    //TIN PROCESSING
            alluvialCassiterite = registrate.metallurgicaItem("alluvial_cassiterite", "alluvial_materials/cassiterite", "alluvial_materials"),

    //COPPER PROCESSING
            copperOxide = registrate.metallurgicaItem("copper_oxide", "dusts/copper_oxide", "dusts"),
            copperRubble = registrate.metallurgicaItem("copper_rubble", "material_rubble/copper", "material_rubble"),
    //BAUXITE PROCESSING
            washedAlumina =       registrate.metallurgicaItem("washed_alumina", "washed_materials/alumina", "washed_materials"),
            alumina =             registrate.metallurgicaItem("alumina", "alumina")
                    ;
    
    //FLUORITE
    public static final ItemEntry<MetallurgicaItem>
            fluoriteCluster =     registrate.cluster("fluorite_cluster"),
            fluoritePowder =      registrate.powder("fluorite_powder")
                    ;
    //METALS
    public static final ItemEntry<MetallurgicaItem>
            aluminumNugget =      registrate.metallurgicaItem("aluminum_nugget", "nuggets/aluminum", "nuggets"),
            aluminumDust =        registrate.metallurgicaItem("aluminum_dust", "dusts/aluminum", "dusts"),
            aluminumSheet =       registrate.metallurgicaItem("aluminum_sheet", "plates/aluminum", "plates"),
    
            titaniumIngot =       registrate.metallurgicaItem("titanium_ingot", "ingots/titanium", "ingots"),
            titaniumNugget =      registrate.metallurgicaItem("titanium_nugget", "nuggets/titanium", "nuggets"),
            titaniumDust =        registrate.metallurgicaItem("titanium_dust", "dusts/titanium", "dusts"),
            titaniumSheet =       registrate.metallurgicaItem("titanium_sheet", "plates/titanium", "plates")
                    ;
    
    public static final ItemEntry<SequencedAssemblyItem>
            unprocessedTitaniumSheet = sequencedIngredient("unprocessed_titanium_sheet"),
            unprocessedTitaniumAluminideSheet = sequencedIngredient("unprocessed_titanium_aluminide_sheet")
                    ;
    
    //ALLOYS
    
    public static final ItemEntry<AlloyItem>
            bronzeIngot =         registrate.alloyItem("bronze_ingot", "ingots/bronze", "ingots", "alloy_ingots/bronze", "alloy_ingots"),
            bronzeNugget =          registrate.alloyItem("bronze_nugget", "nuggets/bronze", "nuggets", "alloy_nuggets/bronze", "alloy_nuggets"),
            bronzeDust =          registrate.alloyItem("bronze_dust", "dusts/bronze", "dusts", "alloy_dusts/bronze", "alloy_dusts"),
            bronzeSheet =         registrate.alloyItem("bronze_sheet", "plates/bronze", "plates", "alloy_plates/bronze", "alloy_plates"),
            titaniumAluminideIngot = registrate.alloyItem("titanium_aluminide_ingot", "ingots/titanium_aluminide", "ingots", "alloy_ingots/titanium_aluminide", "alloy_ingots"),
            titaniumAluminideNugget = registrate.alloyItem("titanium_aluminide_nugget", "nuggets/titanium_aluminide", "nuggets", "alloy_nuggets/titanium_aluminide", "alloy_nuggets"),
            titaniumAluminideDust = registrate.alloyItem("titanium_aluminide_dust", "dusts/titanium_aluminide", "dusts", "alloy_dusts/titanium_aluminide", "alloy_dusts"),
            titaniumAluminideSheet = registrate.alloyItem("titanium_aluminide_sheet", "plates/titanium_aluminide", "plates", "alloy_plates/titanium_aluminide", "alloy_plates")
                    ;

    public static final ItemEntry<Item>
            loosenedBauxite =     registrate.simpleItem("loosened_bauxite", "loosened_materials/bauxite", "loosened_materials"); //why is this a normal item??? Idk lol

    public static void register() {
    }
}
