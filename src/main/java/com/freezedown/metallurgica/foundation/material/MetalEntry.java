package com.freezedown.metallurgica.foundation.material;

import com.freezedown.metallurgica.content.fluids.types.MoltenMetal;
import com.freezedown.metallurgica.foundation.MetallurgicaRegistrate;
import com.tterrag.registrate.util.entry.FluidEntry;

public class MetalEntry {
    
    private final String name;
    public Double meltingPoint;
    public final FluidEntry<MoltenMetal> molten;

    public MetalEntry(MetallurgicaRegistrate reg, String pName, Double meltingPoint) {
        name = pName;
        this.meltingPoint = meltingPoint;
        molten = reg.moltenMetal(pName, meltingPoint);
    }
    
    public String getName() {
        return name;
    }
    
    public Double getMeltingPoint() {
        return meltingPoint;
    }
    
    public FluidEntry<MoltenMetal> getMolten() {
        return molten;
    }

}
