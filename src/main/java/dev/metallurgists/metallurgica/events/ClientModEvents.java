package dev.metallurgists.metallurgica.events;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.foundation.util.DebugHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Metallurgica.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void clientInit(FMLClientSetupEvent evt) {
        var bus = MinecraftForge.EVENT_BUS;
        bus.addListener((CustomizeGuiOverlayEvent.DebugText e) -> DebugHandler.onDrawDebugText(e.getLeft()));

    }
}
