package com.freezedown.metallurgica.foundation;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.fluids.types.Acid;
import com.freezedown.metallurgica.content.fluids.types.MoltenMetal;
import com.freezedown.metallurgica.content.fluids.types.ReactiveGas;
import com.freezedown.metallurgica.content.fluids.types.uf_backport.gas.FlowingGas;
import com.freezedown.metallurgica.content.fluids.types.uf_backport.gas.GasBlock;
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
    
    public FluidBuilder<FlowingGas.Flowing, CreateRegistrate> gas(String name, int color) {
        ResourceLocation still = Metallurgica.asResource("fluid/thin_fluid_still");
        ResourceLocation flow = Metallurgica.asResource("fluid/thin_fluid_flow");
        return fluid(name, still, flow, TintedFluid.create(color), FlowingGas.Flowing::new).source(FlowingGas.Source::new).block(GasBlock::new).build();
    }
    
    public FluidBuilder<ReactiveGas.Flowing, CreateRegistrate> reactiveGas(String name, int color) {
        ResourceLocation still = Metallurgica.asResource("fluid/thin_fluid_still");
        ResourceLocation flow = Metallurgica.asResource("fluid/thin_fluid_flow");
        return fluid(name, still, flow, TintedFluid.create(color), ReactiveGas.Flowing::new).source(ReactiveGas.Source::new).block(GasBlock::new).build();
    }
    
    public FluidBuilder<VirtualFluid, CreateRegistrate> tintedVirtualDust(String name, int color) {
        ResourceLocation still = Metallurgica.asResource("fluid/dust_still");
        ResourceLocation flow = Metallurgica.asResource("fluid/dust_flow");
        return tintedVirtualFluid(name, color, still, flow);
    }
    public FluidBuilder<VirtualFluid, CreateRegistrate> tintedVirtualFluid(String name, int color) {
        return tintedVirtualFluid(name, color, Metallurgica.asResource("fluid/thin_fluid_still"), Metallurgica.asResource("fluid/thin_fluid_flow"));
    }
    public FluidBuilder<VirtualFluid, CreateRegistrate> tintedVirtualFluid(String name, int color, ResourceLocation still, ResourceLocation flow) {
        return virtualFluid(name, still, flow, TintedFluid.create(color, still, flow), VirtualFluid::new);
    }
    public FluidBuilder<Acid, CreateRegistrate> acid(String name, int color, float acidity) {
        ResourceLocation still = Metallurgica.asResource("fluid/thin_fluid_still");
        ResourceLocation flow = Metallurgica.asResource("fluid/thin_fluid_flow");
        return acid(name, color, still, flow, acidity);
    }
    
    public FluidBuilder<Acid, CreateRegistrate> acid(String name, int color, ResourceLocation still, ResourceLocation flow, float acidity) {
        if (acidity > 14 || acidity < 0) {
            throw new IllegalArgumentException("Acidity must be between 0 and 14 for " + name);
        }
        return virtualFluid(name, still, flow, TintedFluid.create(color), p -> new Acid(p).acidity(acidity));
    }
    
    public ItemBuilder<Item, Object> item(String name, Object o) {
        return null;
    }
    
    public static class TintedFluid extends AllFluids.TintedFluidType {
        public ResourceLocation still;
        public ResourceLocation flow;
        public int color;
        
        public TintedFluid(Properties properties, ResourceLocation still, ResourceLocation flow) {
            super(properties, still, flow);
        }
        
        public TintedFluid color(int color) {
            this.color = color;
            return this;
        }
        
        public TintedFluid still(ResourceLocation still) {
            this.still = still;
            return this;
        }
        
        public TintedFluid flow(ResourceLocation flow) {
            this.flow = flow;
            return this;
        }
        
        public static FluidBuilder.FluidTypeFactory create(int color) {
            return (properties, still, flow) -> new TintedFluid(properties, still, flow).color(color);
        }
        
        public static FluidBuilder.FluidTypeFactory create(int color, ResourceLocation still, ResourceLocation flow) {
            return (properties, still1, flow1) -> new TintedFluid(properties, still, flow).color(color).still(still).flow(flow);
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
