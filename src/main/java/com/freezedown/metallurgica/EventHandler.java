package com.freezedown.metallurgica;

import com.freezedown.metallurgica.content.mineral.deposit.DepositManager;
import com.freezedown.metallurgica.foundation.data.custom.composition.tooltip.CompositionManager;
import com.freezedown.metallurgica.foundation.data.custom.composition.fluid.FluidCompositionManager;
import com.freezedown.metallurgica.foundation.data.custom.composition.tooltip.MaterialCompositionManager;
import com.freezedown.metallurgica.foundation.data.custom.temp.biome.BiomeTemperatureManager;
import com.freezedown.metallurgica.foundation.data.custom.temp.dimension.DimensionTemperatureManager;
import com.freezedown.metallurgica.foundation.util.recipe.helper.TagPreferenceManager;
import net.minecraft.client.server.LanServerPinger;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {

    @SubscribeEvent
    public void jsonReading(AddReloadListenerEvent event) {
        DepositManager depositManager = new DepositManager();
        TagPreferenceManager tagPreferenceManager = new TagPreferenceManager();
        CompositionManager compositionManager = new CompositionManager();
        FluidCompositionManager fluidCompositionManager = new FluidCompositionManager();
        BiomeTemperatureManager biomeTemperatureManager = new BiomeTemperatureManager();
        DimensionTemperatureManager dimensionTemperatureManager = new DimensionTemperatureManager();
        MaterialCompositionManager materialCompositionManager = new MaterialCompositionManager();
        event.addListener(tagPreferenceManager);
        event.addListener(depositManager);
        event.addListener(compositionManager);
        event.addListener(fluidCompositionManager);
        event.addListener(biomeTemperatureManager);
        event.addListener(dimensionTemperatureManager);
        event.addListener(materialCompositionManager);
    }
}
