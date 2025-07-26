package dev.metallurgists.metallurgica.foundation.data.custom.composition;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public interface FinishedComposition {
    void serializeData(JsonObject json);
    
    default JsonObject serializeComposition() {
        JsonObject $$0 = new JsonObject();
        serializeData($$0);
        return $$0;
    }
    ResourceLocation getId();
}
