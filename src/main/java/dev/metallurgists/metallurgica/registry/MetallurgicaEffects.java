package dev.metallurgists.metallurgica.registry;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.experimental.burns.ChemicalBurnEffect;
import dev.metallurgists.metallurgica.experimental.exposure_effects.LeadPoisoningEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MetallurgicaEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Metallurgica.ID);
    
    public static final RegistryObject<MobEffect> LEAD_POISONING = MOB_EFFECTS.register("lead_poisoning", () -> new LeadPoisoningEffect(0x4a5961));
    public static final RegistryObject<MobEffect> CHEMICAL_BURN_EFFECT = MOB_EFFECTS.register("chemical_burn_effect", () -> new ChemicalBurnEffect(0x4a5961));


    public static void register(IEventBus modEventBus){
        MOB_EFFECTS.register(modEventBus);
    }
    
}
