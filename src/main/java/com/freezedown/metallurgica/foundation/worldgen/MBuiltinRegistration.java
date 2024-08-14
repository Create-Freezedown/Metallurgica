package com.freezedown.metallurgica.foundation.worldgen;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.worldgen.config.MDepositFeatureConfigEntry;
import com.freezedown.metallurgica.foundation.worldgen.config.MOreFeatureConfigEntry;
import com.freezedown.metallurgica.foundation.worldgen.config.MTypedDepositFeatureConfigEntry;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class MBuiltinRegistration {
    private static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURE_REGISTER = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, Metallurgica.ID);
    private static final DeferredRegister<PlacedFeature> PLACED_FEATURE_REGISTER = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Metallurgica.ID);
    private static final DeferredRegister<BiomeModifier> BIOME_MODIFIER_REGISTER = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIERS, Metallurgica.ID);
    
    static {
        for (Map.Entry<ResourceLocation, MOreFeatureConfigEntry> entry : MOreFeatureConfigEntry.ALL.entrySet()) {
            ResourceLocation id = entry.getKey();
            if (id.getNamespace().equals(Metallurgica.ID)) {
                MOreFeatureConfigEntry.DatagenExtension datagenExt = entry.getValue().datagenExt();
                
                if (datagenExt != null) {
                    CONFIGURED_FEATURE_REGISTER.register(id.getPath(), () -> datagenExt.createConfiguredFeature(BuiltinRegistries.ACCESS));
                    PLACED_FEATURE_REGISTER.register(id.getPath(), () -> datagenExt.createPlacedFeature(BuiltinRegistries.ACCESS));
                    BIOME_MODIFIER_REGISTER.register(id.getPath(), () -> datagenExt.createBiomeModifier(BuiltinRegistries.ACCESS));
                }
            }
        }
        for (Map.Entry<ResourceLocation, MDepositFeatureConfigEntry> entry : MDepositFeatureConfigEntry.ALL.entrySet()) {
            ResourceLocation id = entry.getKey();
            if (id.getNamespace().equals(Metallurgica.ID)) {
                MDepositFeatureConfigEntry.DatagenExtension datagenExt = entry.getValue().datagenExt();
                
                if (datagenExt != null) {
                    CONFIGURED_FEATURE_REGISTER.register(id.getPath(), () -> datagenExt.createConfiguredFeature(BuiltinRegistries.ACCESS));
                    PLACED_FEATURE_REGISTER.register(id.getPath(), () -> datagenExt.createPlacedFeature(BuiltinRegistries.ACCESS));
                    BIOME_MODIFIER_REGISTER.register(id.getPath(), () -> datagenExt.createBiomeModifier(BuiltinRegistries.ACCESS));
                }
            }
        }
        for (Map.Entry<ResourceLocation, MTypedDepositFeatureConfigEntry> entry : MTypedDepositFeatureConfigEntry.ALL.entrySet()) {
            ResourceLocation id = entry.getKey();
            if (id.getNamespace().equals(Metallurgica.ID)) {
                MTypedDepositFeatureConfigEntry.DatagenExtension datagenExt = entry.getValue().datagenExt();
                
                if (datagenExt != null) {
                    CONFIGURED_FEATURE_REGISTER.register(id.getPath(), () -> datagenExt.createConfiguredFeature(BuiltinRegistries.ACCESS));
                    PLACED_FEATURE_REGISTER.register(id.getPath(), () -> datagenExt.createPlacedFeature(BuiltinRegistries.ACCESS));
                    BIOME_MODIFIER_REGISTER.register(id.getPath(), () -> datagenExt.createBiomeModifier(BuiltinRegistries.ACCESS));
                }
            }
        }
    }
    
    public static void register(IEventBus modEventBus) {
        CONFIGURED_FEATURE_REGISTER.register(modEventBus);
        PLACED_FEATURE_REGISTER.register(modEventBus);
        BIOME_MODIFIER_REGISTER.register(modEventBus);
    }
}
