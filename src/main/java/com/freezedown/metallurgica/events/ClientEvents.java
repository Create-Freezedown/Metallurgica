package com.freezedown.metallurgica.events;


import com.freezedown.metallurgica.foundation.temperature.TemperatureOverlay;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {
    
    
    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents {
        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            // Register overlays in reverse order
            event.registerAbove(VanillaGuiOverlay.AIR_LEVEL.id(), "temperature", TemperatureOverlay.INSTANCE);
        }
    }
}
