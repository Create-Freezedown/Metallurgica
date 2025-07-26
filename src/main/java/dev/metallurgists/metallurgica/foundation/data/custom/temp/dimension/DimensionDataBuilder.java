package dev.metallurgists.metallurgica.foundation.data.custom.temp.dimension;

import dev.metallurgists.metallurgica.foundation.data.custom.temp.FinishedData;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class DimensionDataBuilder {
    private final ResourceLocation dimensionId;
    private final double maxHeightTemperature;
    private final double minHeightTemperature;
    
    public DimensionDataBuilder(ResourceLocation dimensionId, double maxHeightTemperature, double minHeightTemperature) {
        this.dimensionId = dimensionId;
        this.maxHeightTemperature = maxHeightTemperature;
        this.minHeightTemperature = minHeightTemperature;
    }
    
    public static DimensionDataBuilder create(ResourceLocation dimensionId, double maxHeightTemperature, double minHeightTemperature) {
        return new DimensionDataBuilder(dimensionId, maxHeightTemperature, minHeightTemperature);
    }
    
    public void save(Consumer<FinishedData> pFinishedDimensionDataConsumer) {
        this.save(pFinishedDimensionDataConsumer, getDefaultDimensionId(this.dimensionId));
    }
    
    public void save(Consumer<FinishedData> pFinishedDimensionDataConsumer, ResourceLocation pDimensionId) {
        ResourceLocation toApply = this.dimensionId;
        double maxHeightTemperatureToApply = this.maxHeightTemperature;
        double minHeightTemperatureToApply = this.minHeightTemperature;
        pFinishedDimensionDataConsumer.accept(new DataGenResult(pDimensionId, toApply, maxHeightTemperatureToApply, minHeightTemperatureToApply));
    }
    
    static ResourceLocation getDefaultDimensionId(ResourceLocation dimensionId) {
        return dimensionId;
    }
    
    public static class DataGenResult implements FinishedData {
        private final ResourceLocation dimensionId;
        private final double maxHeightTemperature;
        private final double minHeightTemperature;
        private ResourceLocation id;
        
        public DataGenResult(ResourceLocation pId, ResourceLocation dimensionId, double maxHeightTemperature, double minHeightTemperature) {
            this.dimensionId = dimensionId;
            this.maxHeightTemperature = maxHeightTemperature;
            this.minHeightTemperature = minHeightTemperature;
            this.id = pId;
        }
        
        @Override
        public void serializeData(JsonObject json) {
            json.addProperty("dimension", dimensionId.toString());
            json.addProperty("maxHeightTemperature", maxHeightTemperature);
            json.addProperty("minHeightTemperature", minHeightTemperature);
        }
        
        @Override
        public ResourceLocation getId() {
            return id;
        }
    }
    
}
