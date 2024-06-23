package com.freezedown.metallurgica.foundation.worldgen;

import com.freezedown.metallurgica.Metallurgica;
import com.simibubi.create.Create;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MetallurgicaPlacementModifiers {
    private static final DeferredRegister<PlacementModifierType<?>> REGISTER = DeferredRegister.create(Registry.PLACEMENT_MODIFIER_REGISTRY, Metallurgica.ID);
    
    public static final RegistryObject<PlacementModifierType<MConfigDrivenPlacement>> CONFIG_DRIVEN = REGISTER.register("config_driven", () -> () -> MConfigDrivenPlacement.CODEC);
    
    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }
}
