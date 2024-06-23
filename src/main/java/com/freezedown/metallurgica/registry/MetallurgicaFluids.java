package com.freezedown.metallurgica.registry;

import com.drmangotea.createindustry.CreateTFMG;
import com.freezedown.metallurgica.Metallurgica;
import com.simibubi.create.content.fluids.VirtualFluid;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.minecraft.resources.ResourceLocation;

public class MetallurgicaFluids {
    public static final ResourceLocation AIR_RL = CreateTFMG.asResource("fluid/air");
    public static final ResourceLocation NITROGEN_RL = Metallurgica.asResource("fluid/nitrogen");
    public static final ResourceLocation BFG_RL = Metallurgica.asResource("fluid/bfg");

    public static final FluidEntry<VirtualFluid> preheatedAir = Metallurgica.registrate.virtualFluid("preheated_air", AIR_RL, AIR_RL).lang("Preheated Air").register();
    public static final FluidEntry<VirtualFluid> nitrogen = Metallurgica.registrate.virtualFluid("nitrogen", NITROGEN_RL, NITROGEN_RL).lang("Nitrogen").register();
    public static final FluidEntry<VirtualFluid> bfg = Metallurgica.registrate.virtualFluid("bfg", AIR_RL, AIR_RL).lang("Blast Furnace Gas").register();
}
