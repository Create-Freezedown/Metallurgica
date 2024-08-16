package com.freezedown.metallurgica.events;


import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.freezedown.metallurgica.registry.misc.MetallurgicaShaders.blurShader;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {
    
    @SubscribeEvent
    public static void renderTick(TickEvent.RenderTickEvent event) {
        if (blurShader == null) return;
        blurShader.safeGetUniform("Radius").set(8f);
        blurShader.safeGetUniform("Progress").set(1f);
        blurShader.apply();
    }
    
}
