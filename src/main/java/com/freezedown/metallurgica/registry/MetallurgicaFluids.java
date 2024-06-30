package com.freezedown.metallurgica.registry;

import com.drmangotea.createindustry.CreateTFMG;
import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.fluids.types.Acid;
import com.freezedown.metallurgica.content.fluids.types.ReactiveGas;
import com.freezedown.metallurgica.content.fluids.types.RiverSandFluid.*;
import com.freezedown.metallurgica.content.fluids.types.RiverSandFluid;
import com.freezedown.metallurgica.content.fluids.types.MoltenMetal;
import com.simibubi.create.content.fluids.VirtualFluid;
import com.tterrag.registrate.util.entry.FluidEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MetallurgicaFluids {
    public static final ResourceLocation AIR_RL = CreateTFMG.asResource("fluid/air");
    public static final ResourceLocation NITROGEN_RL = Metallurgica.asResource("fluid/nitrogen");
    public static final ResourceLocation BFG_RL = Metallurgica.asResource("fluid/bfg");
    public static final ResourceLocation RIVER_SAND_RL = Metallurgica.asResource("fluid/river_sand");

    public static final FluidEntry<VirtualFluid> preheatedAir = Metallurgica.registrate.virtualFluid("preheated_air", AIR_RL, AIR_RL).lang("Preheated Air").register();
    public static final FluidEntry<VirtualFluid> nitrogen = Metallurgica.registrate.virtualFluid("nitrogen", NITROGEN_RL, NITROGEN_RL).lang("Nitrogen").register();
    public static final FluidEntry<VirtualFluid> bfg = Metallurgica.registrate.virtualFluid("bfg", AIR_RL, AIR_RL).lang("Blast Furnace Gas").register();
    
    public static final FluidEntry<RiverSandFluid> riverSand = Metallurgica.registrate.virtualFluid("river_sand", RiverSandFluidType::new, RiverSandFluid::new).lang("River Sand").register();
    public static final FluidEntry<VirtualFluid> cryolite = Metallurgica.registrate.tintedVirtualFluid("cryolite", 0x90EE90).lang("Cryolite").register();
    public static final FluidEntry<VirtualFluid> decontaminatedWater = Metallurgica.registrate.tintedVirtualFluid("decontaminated_water", 0x90C6E3).lang("Decontaminated Water").register();
    public static final FluidEntry<Acid> hydrochloricAcid = Metallurgica.registrate.acid("hydrochloric_acid", 0xAAFFAA, 1.1f).lang("Hydrochloric Acid").register();
    public static final FluidEntry<Acid> sulfuricAcid = Metallurgica.registrate.acid("sulfuric_acid", 0xAAAAFF, 0.1f).lang("Sulfuric Acid").register();
    public static final FluidEntry<Acid> sodiumHydroxide = Metallurgica.registrate.acid("sodium_hydroxide", 0xC3D2D5, 14).lang("Sodium Hydroxide").register();
    public static final FluidEntry<Acid> sodiumHypochlorite = Metallurgica.registrate.acid("sodium_hypochlorite", 0xE8f1C7, 1.1f).lang("Sodium Hypochlorite").register();
    
    public static final FluidEntry<ReactiveGas.Flowing> chlorine = Metallurgica.registrate.reactiveGas("chlorine", 0xDBD971).lang("Chlorine").properties((properties ->
            properties
                    .motionScale(1D)
                    .canPushEntity(false)
                    .canSwim(false)
                    .canDrown(true)
                    .density(3)
                    .temperature(0)
                    .viscosity(0)
                    .fallDistanceModifier(1F)
                    .pathType(null)
                    .adjacentPathType(null)
    )).register();
    
    public static final FluidEntry<VirtualFluid> magnetiteFines = Metallurgica.registrate.tintedVirtualDust("magnetite_fines", 0x696A76).lang("Magnetite Fines").register();
    
    public static final FluidEntry<MoltenMetal.Flowing> moltenIron = Metallurgica.registrate.moltenMetal("iron").register();
    public static final FluidEntry<MoltenMetal.Flowing> moltenCopper = Metallurgica.registrate.moltenMetal("copper").register();
    
    public static Collection<RegistryEntry<Fluid>> ALL = Metallurgica.registrate.getAll(ForgeRegistries.FLUIDS.getRegistryKey());
    
    public static List<FluidStack> getVirtualFluidStacks() {
        List<FluidStack> stacks = new ArrayList<>();
        for (RegistryEntry<Fluid> entry : ALL) {
            if (entry instanceof FluidEntry<?> fluidEntry) {
                if (fluidEntry.get() instanceof VirtualFluid virtualFluid) {
                    FluidStack stack = new FluidStack(virtualFluid.getSource(), FluidType.BUCKET_VOLUME);
                    stacks.add(stack);
                }
            }
        }
        return stacks;
    }
    
    
    
    public static List<Acid> getAcids() {
        List<Acid> acids = new ArrayList<>();
        for (RegistryEntry<Fluid> entry : ALL) {
            if (entry instanceof FluidEntry<?> fluidEntry) {
                if (fluidEntry.get() instanceof Acid acid) {
                    acids.add(acid);
                }
            }
        }
        return acids;
    }
    
    public static void register() {
    }
}
