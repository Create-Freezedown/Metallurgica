package com.freezedown.metallurgica.registry;

import com.drmangotea.createindustry.CreateTFMG;
import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.fluids.RiverSandFluid.*;
import com.freezedown.metallurgica.content.fluids.RiverSandFluid;
import com.freezedown.metallurgica.content.fluids.molten_metal.base.MoltenMetal;
import com.simibubi.create.content.fluids.VirtualFluid;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.minecraft.resources.ResourceLocation;

public class MetallurgicaFluids {
    public static final ResourceLocation AIR_RL = CreateTFMG.asResource("fluid/air");
    public static final ResourceLocation NITROGEN_RL = Metallurgica.asResource("fluid/nitrogen");
    public static final ResourceLocation BFG_RL = Metallurgica.asResource("fluid/bfg");
    public static final ResourceLocation RIVER_SAND_RL = Metallurgica.asResource("fluid/river_sand");

    public static final FluidEntry<VirtualFluid> preheatedAir = Metallurgica.registrate.virtualFluid("preheated_air", AIR_RL, AIR_RL).lang("Preheated Air").register();
    public static final FluidEntry<VirtualFluid> nitrogen = Metallurgica.registrate.virtualFluid("nitrogen", NITROGEN_RL, NITROGEN_RL).lang("Nitrogen").register();
    public static final FluidEntry<VirtualFluid> bfg = Metallurgica.registrate.virtualFluid("bfg", AIR_RL, AIR_RL).lang("Blast Furnace Gas").register();
    public static final FluidEntry<RiverSandFluid> riverSand = Metallurgica.registrate.virtualFluid("river_sand", RiverSandFluidType::new, RiverSandFluid::new).lang("River Sand").register();
    
    public static final FluidEntry<MoltenMetal.Flowing> moltenIron = createMoltenMetal("iron", "Iron");
    
    public static FluidEntry<MoltenMetal.Flowing> createMoltenMetal(String name, String langName) {
        return Metallurgica.registrate.moltenMetal("molten_" + name).lang("Molten " + langName).source(MoltenMetal.Source::new).bucket().build().register();
    }
    
    public static void register() {
    }
}
