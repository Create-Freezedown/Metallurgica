package com.freezedown.metallurgica.registry.misc;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.infastructure.conductor.Conductor;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.HashMap;
import java.util.Map;

public class MetallurgicaRegistries {
    //public static final MetallurgicaRegistry.RL<Conductor> CONDUCTOR = new MetallurgicaRegistry.RL<>(Metallurgica.asResource("conductor"));
    public static final Map<ResourceLocation, Conductor> registeredConductors = new HashMap<ResourceLocation, Conductor>();

    public static final ResourceKey<Registry<Conductor>> CONDUCTOR_KEY = Metallurgica.registrate().makeRegistry("conductor", () -> new RegistryBuilder<Conductor>().hasTags().allowModification().setDefaultKey(Metallurgica.asResource("copper")));


    public static void register() {
    }
}
