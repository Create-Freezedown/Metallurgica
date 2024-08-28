package com.freezedown.metallurgica.foundation.util;

import com.google.gson.JsonElement;
import net.minecraft.network.FriendlyByteBuf;

public interface Writeable {
    public JsonElement serialize();
    
    public void write(FriendlyByteBuf buffer);
}
