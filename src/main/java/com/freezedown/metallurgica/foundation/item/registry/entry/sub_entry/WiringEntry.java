package com.freezedown.metallurgica.foundation.item.registry.entry.sub_entry;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.config.TFMGConductor;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.infastructure.conductor.Conductor;
import com.freezedown.metallurgica.infastructure.conductor.ConductorEntry;
import com.freezedown.metallurgica.infastructure.conductor.CableItem;
import com.simibubi.create.AllTags;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;

public class WiringEntry extends SubEntry {

    public final ConductorEntry<Conductor> conductor;
    public final ItemEntry<CableItem> cable;
    public final ItemEntry<Item> wire;

    public double resistivity;
    public int[] color1 = {193, 90, 54, 255};
    public int[] color2 = {156, 78, 49, 255};

    public WiringEntry(MetallurgicaRegistrate registrate, String pName, double resistivity) {
        super(registrate, pName);
        this.resistivity = resistivity;
        this.wire = createWire();
        this.conductor = createConductor();
        this.cable = createCable();
    }

    public WiringEntry(MetallurgicaRegistrate registrate, String pName, double resistivity, int[] color1, int[] color2) {
        super(registrate, pName);
        this.resistivity = resistivity;
        this.wire = createWire();
        this.conductor = createConductor();
        this.cable = createCable();
        this.color1 = color1;
        this.color2 = color2;
    }

    public WiringEntry(MetallurgicaRegistrate registrate, String pName, double resistivity, ItemEntry<Item> existingWire) {
        super(registrate, pName);
        this.resistivity = resistivity;
        this.wire = existingWire;
        this.conductor = createConductor();
        this.cable = createCable();
    }

    public WiringEntry(MetallurgicaRegistrate registrate, String pName, double resistivity, int[] color1, int[] color2, ItemEntry<Item> existingWire) {
        super(registrate, pName);
        this.resistivity = resistivity;
        this.wire = existingWire;
        this.conductor = createConductor();
        this.cable = createCable();
        this.color1 = color1;
        this.color2 = color2;
    }

    private ConductorEntry<Conductor> createConductor() {
        return this.getRegistrate().conductor(getName(), Conductor::new)
                .properties(p -> p.color1(color1).color2(color2))
                .transform(TFMGConductor.setResistivity(this.resistivity))
                .register();
    }

    private ItemEntry<CableItem> createCable() {
        return this.getRegistrate().item(formatName("%s_cable"), (p) -> new CableItem(p, conductor))
                .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                        .pattern(" W ")
                        .pattern("WSW")
                        .pattern(" W ")
                        .define('W', this.wire.asItem())
                        .define('S', Tags.Items.RODS_WOODEN)
                        .unlockedBy("has_" + prov.safeName(this.wire), RegistrateRecipeProvider.has(this.wire))
                        .save(prov, Metallurgica.asResource("crafting/cable/" + getName()))
                )
                .register();
    }

    private ItemEntry<Item> createWire() {
        return this.getRegistrate().item(formatName("%s_wire"), Item::new)
                .tag(AllTags.forgeItemTag(formatName("wires/%s")))
                .recipe((ctx, prov) -> prov.stonecutting(DataIngredient.tag(AllTags.forgeItemTag(formatName("ingots/%s"))), RecipeCategory.MISC, ctx, 2))
                .register();
    }

}
