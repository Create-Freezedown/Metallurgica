package dev.metallurgists.metallurgica.foundation.data.custom.temp;

import dev.metallurgists.metallurgica.foundation.data.custom.temp.biome.BiomeTemperature;
import com.google.gson.JsonObject;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TemperatureBuilder {
    private final ResourceLocation biomeId;
    private final double surfaceTemperature;
    private final List<ResourceLocation> temperatureBlendBlacklist = new ArrayList<>();
    
    public TemperatureBuilder(ResourceLocation biomeId, double surfaceTemperature) {
        this.biomeId = biomeId;
        this.surfaceTemperature = surfaceTemperature;
    }
    
    public TemperatureBuilder withBlacklist(List<ResourceLocation> pTemperatureBlendBlacklist) {
        this.temperatureBlendBlacklist.addAll(pTemperatureBlendBlacklist);
        return this;
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
    
    public void save(Consumer<FinishedData> pFinishedTemperatureConsumer) {
        this.save(pFinishedTemperatureConsumer, getDefaultTemperatureId(this.biomeId));
    }
    
    public void save(Consumer<FinishedData> pFinishedTemperatureConsumer, ResourceLocation pTemperatureId) {
        ResourceLocation toApply = this.biomeId;
        double temperatureToApply = this.surfaceTemperature;
        List<ResourceLocation> temperatureBlendBlacklistToApply = this.temperatureBlendBlacklist;
        pFinishedTemperatureConsumer.accept(new DataGenResult(pTemperatureId, toApply, temperatureToApply, temperatureBlendBlacklistToApply));
    }
    
    public static class DataGenResult implements FinishedData {
        private final ResourceLocation biomeId;
        private final double surfaceTemperature;
        private final List<ResourceLocation> temperatureBlendBlacklist;
        private ResourceLocation id;
        
        public DataGenResult(ResourceLocation pId, ResourceLocation biomeId, double surfaceTemperature, List<ResourceLocation> temperatureBlendBlacklist) {
            this.biomeId = biomeId;
            this.surfaceTemperature = surfaceTemperature;
            this.temperatureBlendBlacklist = temperatureBlendBlacklist;
            this.id = pId;
        }
        
        @Override
        public void serializeData(JsonObject json) {
            json.addProperty("biome", biomeId.toString());
            json.addProperty("surfaceTemperature", surfaceTemperature);
            json.add("temperatureBlendBlacklist", BiomeTemperature.serializeBlacklist(temperatureBlendBlacklist));
        }
        
        @Override
        public ResourceLocation getId() {
            return id;
        }
    }
}
