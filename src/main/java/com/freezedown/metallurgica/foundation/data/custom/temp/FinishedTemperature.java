package com.freezedown.metallurgica.foundation.data.custom.temp;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public interface FinishedTemperature {
    void serializeData(JsonObject json);
    
    default JsonObject serializeTemp() {
        JsonObject $$0 = new JsonObject();
        serializeData($$0);
        return $$0;
    }
    ResourceLocation getId();
}
