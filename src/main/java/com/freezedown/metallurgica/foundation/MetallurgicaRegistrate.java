package com.freezedown.metallurgica.foundation;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.fluids.molten_metal.base.MoltenMetal;
import com.simibubi.create.AllFluids;
import com.simibubi.create.content.fluids.VirtualFluid;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidStack;

import javax.sound.midi.MidiFileFormat;

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
    
    public FluidBuilder<VirtualFluid, CreateRegistrate> tintedVirtualDust(String name, int color) {
        return tintedVirtualFluid(name, color, Metallurgica.asResource("fluid/dust_still"), Metallurgica.asResource("fluid/dust_flow"));
    }
    public FluidBuilder<VirtualFluid, CreateRegistrate> tintedVirtualFluid(String name, int color) {
        return tintedVirtualFluid(name, color, Metallurgica.asResource("fluid/thin_fluid_still"), Metallurgica.asResource("fluid/thin_fluid_flow"));
    }
    public FluidBuilder<VirtualFluid, CreateRegistrate> tintedVirtualFluid(String name, int color, ResourceLocation still, ResourceLocation flow) {
        return virtualFluid(name, still, flow, TintedVirtualFluid.create(color), VirtualFluid::new);
    }
    
    public ItemBuilder<Item, Object> item(String name, Object o) {
        return null;
    }
    
    
    public static class TintedVirtualFluid extends AllFluids.TintedFluidType {
        public static final ResourceLocation stillRl = Metallurgica.asResource("fluid/thin_fluid_still");
        public static final ResourceLocation flowRl = Metallurgica.asResource("fluid/thin_fluid_flow");
        public int color;
        
        public TintedVirtualFluid(Properties properties) {
            super(properties, stillRl, flowRl);
        }
        
        public TintedVirtualFluid color(int color) {
            this.color = color;
            return this;
        }
        
        public static FluidBuilder.FluidTypeFactory create(int color) {
            return (properties, still, flow) -> new TintedVirtualFluid(properties).color(color);
        }
        
        @Override
        protected int getTintColor(FluidStack stack) {
            int c = color | 0xFF000000;
            return c;
        }
        
        @Override
        protected int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
            return NO_TINT;
        }
    }
}
