package com.freezedown.metallurgica;

import com.freezedown.metallurgica.content.mineral.deposit.DepositManager;
import com.freezedown.metallurgica.foundation.item.composition.CompositionManager;
import com.freezedown.metallurgica.foundation.util.recipe.helper.TagPreferenceManager;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {
    @SubscribeEvent
    public void jsonReading(AddReloadListenerEvent event) {
        DepositManager depositManager = new DepositManager();
        TagPreferenceManager tagPreferenceManager = new TagPreferenceManager();
        CompositionManager compositionManager = new CompositionManager();
        event.addListener(tagPreferenceManager);
        event.addListener(depositManager);
        event.addListener(compositionManager);
    }
}
