package com.freezedown.metallurgica.registry;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.experimental.exposure_effects.LeadPoisoningEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MetallurgicaEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Metallurgica.ID);
    
    public static final RegistryObject<MobEffect> LEAD_POISONING = MOB_EFFECTS.register("lead_poisoning", () -> new LeadPoisoningEffect(0x4a5961));
    
    
    public static void register(IEventBus modEventBus){
        MOB_EFFECTS.register(modEventBus);
    }
    
}
