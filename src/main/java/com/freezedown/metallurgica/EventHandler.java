package com.freezedown.metallurgica;

import com.freezedown.metallurgica.content.mineral.deposit.DepositManager;
import com.freezedown.metallurgica.foundation.data.custom.composition.CompositionManager;
import com.freezedown.metallurgica.foundation.data.custom.composition.fluid.FluidCompositionManager;
import com.freezedown.metallurgica.foundation.util.recipe.helper.TagPreferenceManager;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {
    @SubscribeEvent
    public void jsonReading(AddReloadListenerEvent event) {
        DepositManager depositManager = new DepositManager();
        TagPreferenceManager tagPreferenceManager = new TagPreferenceManager();
        CompositionManager compositionManager = new CompositionManager();
        FluidCompositionManager fluidCompositionManager = new FluidCompositionManager();
        event.addListener(tagPreferenceManager);
        event.addListener(depositManager);
        event.addListener(compositionManager);
        event.addListener(fluidCompositionManager);
    }
}
