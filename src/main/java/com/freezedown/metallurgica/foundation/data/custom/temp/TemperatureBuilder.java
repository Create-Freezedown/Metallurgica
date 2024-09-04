package com.freezedown.metallurgica.foundation.data.custom.temp;

import com.freezedown.metallurgica.foundation.data.custom.composition.CompositionBuilder;
import com.freezedown.metallurgica.foundation.data.custom.composition.Element;
import com.freezedown.metallurgica.foundation.data.custom.composition.FinishedComposition;
import com.google.gson.JsonObject;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;

import java.util.List;
import java.util.function.Consumer;

public class TemperatureBuilder {
    private final ResourceLocation biomeId;
    private final double surfaceTemperature;
    
    public TemperatureBuilder(ResourceLocation biomeId, double surfaceTemperature) {
        this.biomeId = biomeId;
        this.surfaceTemperature = surfaceTemperature;
    }
    
    public static TemperatureBuilder create(ResourceLocation biomeId, double surfaceTemperature) {
        return new TemperatureBuilder(biomeId, surfaceTemperature);
    }
    
    public static TemperatureBuilder create(Holder<Biome> biome, double surfaceTemperature) {
        return new TemperatureBuilder(biome.unwrapKey().get().location(), surfaceTemperature);
    }
    static ResourceLocation getDefaultTemperatureId(ResourceLocation biomeId) {
        return biomeId;
    }
    
    public void save(Consumer<FinishedTemperature> pFinishedTemperatureConsumer) {
        this.save(pFinishedTemperatureConsumer, getDefaultTemperatureId(this.biomeId));
    }
    
    public void save(Consumer<FinishedTemperature> pFinishedTemperatureConsumer, ResourceLocation pTemperatureId) {
        ResourceLocation toApply = this.biomeId;
        double temperatureToApply = this.surfaceTemperature;
        pFinishedTemperatureConsumer.accept(new DataGenResult(pTemperatureId, toApply, temperatureToApply));
    }
    
    public static class DataGenResult implements FinishedTemperature {
        private final ResourceLocation biomeId;
        private final double surfaceTemperature;
        private ResourceLocation id;
        
        public DataGenResult(ResourceLocation pId, ResourceLocation biomeId, double surfaceTemperature) {
            this.biomeId = biomeId;
            this.surfaceTemperature = surfaceTemperature;
            this.id = pId;
        }
        
        @Override
        public void serializeData(JsonObject json) {
            json.addProperty("biome", biomeId.toString());
            json.addProperty("surfaceTemperature", surfaceTemperature);
        }
        
        @Override
        public ResourceLocation getId() {
            return id;
        }
    }
}
