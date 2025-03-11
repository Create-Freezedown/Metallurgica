package com.freezedown.metallurgica.foundation.item.registry.entry.sub_entry;

import com.freezedown.metallurgica.content.fluids.types.MoltenMetal;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.tterrag.registrate.util.entry.FluidEntry;

public class MoltenEntry extends SubEntry{
    public final double meltingPoint;
    public final FluidEntry<MoltenMetal> molten;

    public MoltenEntry(MetallurgicaRegistrate registrate, String name, double meltingPoint) {
        super(registrate, name);
        this.meltingPoint = meltingPoint;
        this.molten = createMolten();
    }

    private FluidEntry<MoltenMetal> createMolten() {
        return getRegistrate().moltenMetal(getName(), meltingPoint);
    }
}
