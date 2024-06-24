package com.freezedown.metallurgica.foundation;

import com.freezedown.metallurgica.content.fluids.molten_metal.base.MoltenMetal;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.FluidBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;

public class MetallurgicaRegistrate extends CreateRegistrate {
    /**
     * Construct a new Registrate for the given mod ID.
     *
     * @param modid The mod ID for which objects will be registered
     */
    protected MetallurgicaRegistrate(String modid) {
        super(modid);
    }
    
    public static MetallurgicaRegistrate create(String modid) {
        return new MetallurgicaRegistrate(modid);
    }
    
    public FluidBuilder<MoltenMetal.Flowing, CreateRegistrate> moltenMetal(String name) {
        return fluid(name, new ResourceLocation(getModid(), "fluid/" + name + "_still"), new ResourceLocation(getModid(), "fluid/" + name + "_flow"), MoltenMetal.MoltenMetalFluidType::new, MoltenMetal.Flowing::new);
    }
    
    
    
    
}
