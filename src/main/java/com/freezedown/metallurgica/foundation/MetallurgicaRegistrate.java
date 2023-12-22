package com.freezedown.metallurgica.foundation;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.VirtualFluidBuilder;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.ForgeFlowingFluid;

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
    
    public <T extends ForgeFlowingFluid> FluidBuilder<T, CreateRegistrate> moltenMetal(String name, FluidBuilder.FluidTypeFactory typeFactory, NonNullFunction<ForgeFlowingFluid.Properties, T> factory) {
        return entry(name,
                c -> new VirtualFluidBuilder<>(self(), self(), name, c, new ResourceLocation(getModid(), "fluid/" + name + "_still"),
                        new ResourceLocation(getModid(), "fluid/" + name + "_flow"), typeFactory, factory));
    }
}
