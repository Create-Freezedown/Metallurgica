package com.freezedown.metallurgica.foundation.worldgen;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.worldgen.feature.MLayeredOreFeature;
import com.freezedown.metallurgica.foundation.worldgen.feature.MStandardOreFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MetallurgicaFeatures {
    private static final DeferredRegister<Feature<?>> REGISTER = DeferredRegister.create(ForgeRegistries.FEATURES, Metallurgica.ID);
    
    public static final RegistryObject<MStandardOreFeature> STANDARD_ORE = REGISTER.register("standard_ore", MStandardOreFeature::new);
    public static final RegistryObject<MLayeredOreFeature> LAYERED_ORE = REGISTER.register("layered_ore", MLayeredOreFeature::new);
    
    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }
}
