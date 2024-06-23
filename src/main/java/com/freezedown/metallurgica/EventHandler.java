package com.freezedown.metallurgica;

import com.freezedown.metallurgica.content.mineral.deposit.DepositManager;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {
    @SubscribeEvent
    public void jsonReading(AddReloadListenerEvent event) {
        DepositManager depositManager = new DepositManager();
        event.addListener(depositManager);
    }
}
