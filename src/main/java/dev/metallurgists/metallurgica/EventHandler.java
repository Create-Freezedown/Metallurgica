package dev.metallurgists.metallurgica;

import dev.metallurgists.metallurgica.content.mineral.deposit.DepositManager;
import dev.metallurgists.metallurgica.foundation.data.custom.composition.tooltip.CompositionManager;
import dev.metallurgists.metallurgica.foundation.data.custom.composition.fluid.FluidCompositionManager;
import dev.metallurgists.metallurgica.foundation.data.custom.composition.tooltip.MaterialCompositionManager;
import dev.metallurgists.metallurgica.foundation.data.custom.temp.biome.BiomeTemperatureManager;
import dev.metallurgists.metallurgica.foundation.data.custom.temp.dimension.DimensionTemperatureManager;
import dev.metallurgists.metallurgica.foundation.util.recipe.helper.TagPreferenceManager;
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
