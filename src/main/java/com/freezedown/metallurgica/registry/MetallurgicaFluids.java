package com.freezedown.metallurgica.registry;

import com.drmangotea.createindustry.CreateTFMG;
import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.fluids.types.Acid;
import com.freezedown.metallurgica.content.fluids.types.ReactiveGas;
import com.freezedown.metallurgica.content.fluids.types.RiverSandFluid.*;
import com.freezedown.metallurgica.content.fluids.types.RiverSandFluid;
import com.freezedown.metallurgica.content.fluids.types.MoltenMetal;
import com.freezedown.metallurgica.content.fluids.types.uf_backport.gas.FlowingGas;
import com.freezedown.metallurgica.foundation.MetallurgicaRegistrate;
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

import static com.freezedown.metallurgica.Metallurgica.registrate;

public class MetallurgicaFluids {
    public static final ResourceLocation AIR_RL = CreateTFMG.asResource("fluid/air");
    public static final ResourceLocation NITROGEN_RL = Metallurgica.asResource("fluid/nitrogen");
    public static final ResourceLocation BFG_RL = Metallurgica.asResource("fluid/bfg");
    public static final ResourceLocation RIVER_SAND_RL = Metallurgica.asResource("fluid/river_sand");

    public static final FluidEntry<VirtualFluid>
            preheatedAir =             registrate.virtualFluid("preheated_air", AIR_RL, AIR_RL).lang("Preheated Air").register(),
            nitrogen =                 registrate.virtualFluid("nitrogen", NITROGEN_RL, NITROGEN_RL).lang("Nitrogen").register(),
            bfg =                      registrate.virtualFluid("bfg", AIR_RL, AIR_RL).lang("Blast Furnace Gas").register(),
            cryolite =                 registrate.tintedVirtualFluid("cryolite", 0x90EE90).lang("Cryolite").register(),
            decontaminatedWater =      registrate.tintedVirtualFluid("decontaminated_water", 0x90C6E3).lang("Decontaminated Water").register(),
            magnetiteFines =           registrate.tintedVirtualDust("magnetite_fines", 0x696A76).lang("Magnetite Fines").register();

    public static final FluidEntry<Acid>
            hydrochloricAcid =         registrate.acid("hydrochloric_acid", 0xAAFFAA, 1.1f, "Hydrochloric Acid"),
            sulfuricAcid =             registrate.acid("sulfuric_acid", 0xAAAAFF, 0.1f, "Sulfuric Acid"),
            sodiumHydroxide =          registrate.acid("sodium_hydroxide", 0xC3D2D5, 14, "Sodium Hydroxide"),
            sodiumHypochlorite =       registrate.acid("sodium_hypochlorite", 0xE8f1C7, 1.1f, "Sodium Hypochlorite");

    public static final FluidEntry<RiverSandFluid> riverSand =
            registrate.virtualFluid("river_sand", RiverSandFluidType::new, RiverSandFluid::new).lang("River Sand").register();

    public static final FluidEntry<ReactiveGas.Flowing> chlorine =
            registrate.reactiveGas("chlorine", 0xDBD971).lang("Chlorine").properties((properties ->
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
    
    public static final FluidEntry<MoltenMetal.Flowing> moltenIron = MetallurgicaRegistrate.createMoltenMetal("iron");

    public static final FluidEntry<MoltenMetal.Flowing> moltenCopper = MetallurgicaRegistrate.createMoltenMetal("copper");

    public static Collection<RegistryEntry<Fluid>> ALL = registrate.getAll(ForgeRegistries.FLUIDS.getRegistryKey());

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
