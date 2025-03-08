package com.freezedown.metallurgica.registry;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.items.metals.MagnesiumItem;
import com.freezedown.metallurgica.content.items.metals.MagnesiumOxideItem;
import com.freezedown.metallurgica.content.items.sealed_storage.SealedBundleItem;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.foundation.item.AlloyItem;
import com.freezedown.metallurgica.foundation.item.MetallurgicaItem;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

public class MetallurgicaItems {
    private static final MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().setCreativeTab(MetallurgicaCreativeTab.MAIN_TAB);;
    
    private static ItemEntry<SequencedAssemblyItem> sequencedIngredient(String name) {
        return registrate.item(name, SequencedAssemblyItem::new)
                .register();
    }

    public static final ItemEntry<Item>
            armorPlatingMold =    registrate.item("armor_plating_mold", Item::new, p->p.stacksTo(1).fireResistant(), "advanced_casting_molds/armor_plating", "advanced_casting_molds");

    public static final ItemEntry<SealedBundleItem>
            sealedBundle = registrate.item("sealed_bundle", SealedBundleItem::new, p->p.stacksTo(1))
                    ;
    //MISC ITEMS
    public static final ItemEntry<MetallurgicaItem>
            salt =                registrate.metallurgicaItem("salt", "salt"),
    //IRON PROCESSING
            pigIron =      registrate.metallurgicaItem("pig_iron_ingot", "ingots/pig_iron_ingot", "ingot"), //I REFUSE TO REGISTER THIS ANYWHERE OTHER THAN IRON PROCESSING
            impureIronBloom =      registrate.metallurgicaItem("impure_iron_bloom"),
            pureIronBloom =      registrate.metallurgicaItem("pure_iron_bloom"),
            ironSinter =      registrate.metallurgicaItem("iron_sinter"),

    //MAGNETITE PROCESSING
            magnetiteLumps =      registrate.metallurgicaItem("magnetite_lumps", "lumps/magnetite", "lumps"),

    //TIN PROCESSING
            alluvialCassiterite = registrate.metallurgicaItem("alluvial_cassiterite", "alluvial_materials/cassiterite", "alluvial_materials"),

    //COPPER PROCESSING
            copperRubble = registrate.metallurgicaItem("copper_rubble", "material_rubble/copper", "material_rubble"),
            copperOxide = registrate.metallurgicaItem("copper_oxide", "dusts/copper_oxide", "dusts"),

    //BAUXITE PROCESSING
            washedAlumina =       registrate.metallurgicaItem("washed_alumina", "washed_materials/alumina", "washed_materials"),
            alumina =             registrate.metallurgicaItem("alumina", "alumina")
                    ;
    
    //FLUORITE
    public static final ItemEntry<MetallurgicaItem>
            fluoriteCluster =     registrate.cluster("fluorite_cluster"),
            fluoritePowder =      registrate.powder("fluorite_powder")
                    ;
    //RUTILE
    public static final ItemEntry<MetallurgicaItem>
            rutilePowder =        registrate.powder("rutile_powder")
                    ;
    
    //MAGNESIUM
    public static final ItemEntry<MetallurgicaItem>
            magnesiumChloride =   registrate.metallurgicaItem("magnesium_chloride" )
                    ;
    //METALS
    public static final ItemEntry<MetallurgicaItem>
            aluminumNugget =      registrate.metallurgicaItem("aluminum_nugget", "nuggets/aluminum", "nuggets"),
            aluminumDust =        registrate.metallurgicaItem("aluminum_dust", "dusts/aluminum", "dusts"),
            aluminumSheet =       registrate.metallurgicaItem("aluminum_sheet", "plates/aluminum", "plates"),
    
            titaniumIngot =       registrate.metallurgicaItem("titanium_ingot", "ingots/titanium", "ingots"),
            titaniumNugget =      registrate.metallurgicaItem("titanium_nugget", "nuggets/titanium", "nuggets"),
            titaniumDust =        registrate.metallurgicaItem("titanium_dust", "dusts/titanium", "dusts"),
            titaniumSheet =       registrate.metallurgicaItem("titanium_sheet", "plates/titanium", "plates"),

            //Isn't this technically an alloy? where would we put steel?
            wroughtIronIngot =       registrate.metallurgicaItem("wrought_iron_ingot", "ingots/wrought_iron", "ingots")
                    ;

    public static final ItemEntry<MagnesiumOxideItem>
            magnesiumOxide = registrate.item("magnesium_oxide", MagnesiumOxideItem::new, p->p.stacksTo(1), "dusts/magnesium_oxide", "dusts"),
            oxidisedMagnesiumIngot = registrate.item("oxidised_magnesium_ingot", MagnesiumOxideItem::new, p->p.stacksTo(1), "ingots/oxidised_magnesium", "ingots")
                    ;
    public static final ItemEntry<MagnesiumItem>
            magnesiumIngot = registrate.item("magnesium_ingot", MagnesiumItem::createIngot, p->p.stacksTo(1), "ingots/magnesium", "ingots")
                    ;
    
    public static final ItemEntry<SequencedAssemblyItem>
            semiPressedTitaniumSheet = sequencedIngredient("semi_pressed_titanium_sheet"),
            semiPressedTitaniumAluminideSheet = sequencedIngredient("semi_pressed_titanium_aluminide_sheet")
                    ;
    
    //ALLOYS
    
    public static final ItemEntry<AlloyItem>
            bronzeIngot =         registrate.alloyItem("bronze_ingot", "ingots/bronze", "ingots", "alloy_ingots/bronze", "alloy_ingots"),
            bronzeNugget =          registrate.alloyItem("bronze_nugget", "nuggets/bronze", "nuggets", "alloy_nuggets/bronze", "alloy_nuggets"),
            bronzeDust =          registrate.alloyItem("bronze_dust", "dusts/bronze", "dusts", "alloy_dusts/bronze", "alloy_dusts"),
            bronzeSheet =         registrate.alloyItem("bronze_sheet", "plates/bronze", "plates", "alloy_plates/bronze", "alloy_plates"),
            arsenicalbronzeSheet =         registrate.alloyItem("arsenical_bronze_sheet", "plates/arsenical_bronze", "plates", "alloy_plates/arsenical_bronze", "alloy_plates"),
            arsenicalBronzeIngot =         registrate.alloyItem("arsenical_bronze_ingot", "ingots/arsenical_bronze", "ingots", "alloy_ingots/arsenical_bronze", "alloy_ingots"),
            arsenicalBronzeNugget =          registrate.alloyItem("arsenical_bronze_nugget", "nuggets/arsenical_bronze", "nuggets", "alloy_nuggets/arsenical_bronze", "alloy_nuggets"),
            titaniumAluminideIngot = registrate.alloyItem("titanium_aluminide_ingot", "ingots/titanium_aluminide", "ingots", "alloy_ingots/titanium_aluminide", "alloy_ingots"),
            titaniumAluminideNugget = registrate.alloyItem("titanium_aluminide_nugget", "nuggets/titanium_aluminide", "nuggets", "alloy_nuggets/titanium_aluminide", "alloy_nuggets"),
            titaniumAluminideDust = registrate.alloyItem("titanium_aluminide_dust", "dusts/titanium_aluminide", "dusts", "alloy_dusts/titanium_aluminide", "alloy_dusts"),
            titaniumAluminideSheet = registrate.alloyItem("titanium_aluminide_sheet", "plates/titanium_aluminide", "plates", "alloy_plates/titanium_aluminide", "alloy_plates")
                    ;
    
    public static final ItemEntry<MetallurgicaItem>
            hornblendeShard =     registrate.metallurgicaItem("hornblende_shard", "shards/hornblende", "shards", "rock_shards"),
            plagioclaseShard =    registrate.metallurgicaItem("plagioclase_shard", "shards/plagioclase", "shards", "rock_shards"),
            biotiteShard =        registrate.metallurgicaItem("biotite_shard", "shards/biotite", "shards", "rock_shards"),
            clinopyroxeneShard =  registrate.metallurgicaItem("clinopyroxene_shard", "shards/clinopyroxene", "shards", "rock_shards"),
            orthopyroxeneShard =  registrate.metallurgicaItem("orthopyroxene_shard", "shards/orthopyroxene", "shards", "rock_shards"),
            quartzShard =         registrate.metallurgicaItem("quartz_shard", "shards/quartz", "shards", "rock_shards"),
            amphiboleShard =      registrate.metallurgicaItem("amphibole_shard", "shards/amphibole", "shards", "rock_shards")
                    ;
    
    //PRIMITIVE
    public static final ItemEntry<Item> dirtyClayBall = registrate.simpleItem("dirty_clay_ball", "dirty_clay_balls", "primitive_materials");

    public static final ItemEntry<Item>
            loosenedBauxite =     registrate.simpleItem("loosened_bauxite", "loosened_materials/bauxite", "loosened_materials"); //why is this a normal item??? Idk lol

    public static void register() {
    }
}
