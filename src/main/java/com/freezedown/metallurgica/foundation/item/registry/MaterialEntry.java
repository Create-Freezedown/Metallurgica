package com.freezedown.metallurgica.foundation.item.registry;

import com.freezedown.metallurgica.foundation.item.registry.entry.sub_entry.MoltenEntry;
import com.freezedown.metallurgica.foundation.item.registry.entry.sub_entry.SubEntry;
import com.freezedown.metallurgica.foundation.item.registry.entry.sub_entry.WiringEntry;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import lombok.Getter;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class MaterialEntry {

    private final MetallurgicaRegistrate registrate;
    private final String name;

    private final Map<String, SubEntry> subEntries = new HashMap<>();


    public MaterialEntry(MetallurgicaRegistrate registrate, String name) {
        this.registrate = registrate;
        this.name = name;
    }

    public MaterialEntry wiring(double resistivity) {
        this.subEntries.put("wiring", new WiringEntry(registrate, name, resistivity));
        return this;
    }
    public MaterialEntry wiring(double resistivity, ItemEntry<Item> existingWire) {
        this.subEntries.put("wiring", new WiringEntry(registrate, name, resistivity, existingWire));
        return this;
    }
    /**
     * @param resistivity The resistivity of the {@link com.freezedown.metallurgica.infastructure.conductor.Conductor Conductor}
     * @param color1 The first colour of the {@link com.freezedown.metallurgica.infastructure.conductor.Conductor Conductor's} Cable
     * @param color2 The second colour of the {@link com.freezedown.metallurgica.infastructure.conductor.Conductor Conductor's} Cable
     * @return Itself (optional constructor piece)
     */
    public MaterialEntry wiring(double resistivity, int[] color1, int[] color2) {
        this.subEntries.put("wiring", new WiringEntry(registrate, name, resistivity, color1, color2));
        return this;
    }
    public MaterialEntry wiring(double resistivity, int[] color1, int[] color2, ItemEntry<Item> existingWire) {
        this.subEntries.put("wiring", new WiringEntry(registrate, name, resistivity, color1, color2, existingWire));
        return this;
    }
    /**
     * Only use if a {@link WiringEntry} has been specified for the material.
     * <p>
     *     A {@link WiringEntry} is used to define a {@link com.freezedown.metallurgica.infastructure.conductor.Conductor Conductor}, {@link WiringEntry#createWire Wire} and {@link com.freezedown.metallurgica.infastructure.conductor.CableItem Cable}
     * </p>
     * @return The {@link WiringEntry} (if specified) or else null
     */
    public WiringEntry wiring() {
        return (WiringEntry) subEntries.getOrDefault("wiring", null);
    }

    public MaterialEntry molten(double meltingPoint) {
        this.subEntries.put("molten", new MoltenEntry(registrate, name, meltingPoint));
        return this;
    }

    /**
     * Only use if a {@link MoltenEntry} has been specified for the material.
     * <p>
     *     A {@link MoltenEntry} is used to define a Molten fluid for a material (mainly metals)
     * </p>
     * @return The {@link MoltenEntry} (if specified) or else null
     */
    public MoltenEntry molten() {
        return (MoltenEntry) subEntries.getOrDefault("molten", null);
    }


}
