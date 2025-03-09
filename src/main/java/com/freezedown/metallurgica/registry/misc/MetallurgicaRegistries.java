package com.freezedown.metallurgica.registry.misc;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.infastructure.conductor.Conductor;

public class MetallurgicaRegistries {
    public static final MetallurgicaRegistry.RL<Conductor> CONDUCTOR = new MetallurgicaRegistry.RL<>(Metallurgica.asResource("conductor"));
}
