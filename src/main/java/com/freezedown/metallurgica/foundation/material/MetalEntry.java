package com.freezedown.metallurgica.foundation.material;

import com.freezedown.metallurgica.content.fluids.types.MoltenMetal;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.tterrag.registrate.util.entry.FluidEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.world.item.Item;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class MetalEntry {
    
    private final String name;
    private final String element;
    public Double meltingPoint;
    public final FluidEntry<MoltenMetal> molten;

    public MetalEntry(MetallurgicaRegistrate reg, String pName, Double meltingPoint, String pElement) {
        name = pName;
        element = pElement;
        this.meltingPoint = meltingPoint;
        molten = reg.moltenMetal(pName, meltingPoint);
    }
    
    public String getName() {
        return name;
    }
    
    public String getElement() {
        return element;
    }
    
    public Double getMeltingPoint() {
        return meltingPoint;
    }
    
    public FluidEntry<MoltenMetal> getMolten() {
        return molten;
    }
    
    public void provideCompositionLang(BiConsumer<String, String> consumer) {
        consumer.accept("metallurgica.material." + getName() + ".composition", getElement());
    }

}
