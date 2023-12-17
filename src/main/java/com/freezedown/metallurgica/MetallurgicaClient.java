package com.freezedown.metallurgica;

import com.freezedown.metallurgica.registry.MetallurgicaPartialModels;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class MetallurgicaClient {
    
    public static void onCtorClient(IEventBus modEventBus, IEventBus forgeEventBus) {
        modEventBus.addListener(MetallurgicaClient::clientInit);
    }
    public static void clientInit(final FMLClientSetupEvent event) {
        MetallurgicaPartialModels.init();
    }
}
