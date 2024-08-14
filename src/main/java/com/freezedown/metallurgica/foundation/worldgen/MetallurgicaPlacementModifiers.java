package com.freezedown.metallurgica.foundation.worldgen;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.worldgen.config.MConfigDrivenPlacement;
import com.freezedown.metallurgica.foundation.worldgen.config.MDepositConfigDrivenPlacement;
import com.freezedown.metallurgica.foundation.worldgen.config.MTypedDepositConfigDrivenPlacement;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MetallurgicaPlacementModifiers {
    private static final DeferredRegister<PlacementModifierType<?>> REGISTER = DeferredRegister.create(Registry.PLACEMENT_MODIFIER_REGISTRY, Metallurgica.ID);
    
    public static final RegistryObject<PlacementModifierType<MConfigDrivenPlacement>> CONFIG_DRIVEN = REGISTER.register("config_driven", () -> () -> MConfigDrivenPlacement.CODEC);
    public static final RegistryObject<PlacementModifierType<MDepositConfigDrivenPlacement>> DEPOSIT_CONFIG_DRIVEN = REGISTER.register("deposit_config_driven", () -> () -> MDepositConfigDrivenPlacement.CODEC);
    public static final RegistryObject<PlacementModifierType<MTypedDepositConfigDrivenPlacement>> TYPED_DEPOSIT_CONFIG_DRIVEN = REGISTER.register("typed_deposit_config_driven", () -> () -> MTypedDepositConfigDrivenPlacement.CODEC);
    
    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }
}
