package com.freezedown.metallurgica.infastructure;

import net.minecraft.network.chat.Component;

public interface IHasDescriptionId {

    String getOrCreateDescriptionId();

    default String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    default Component getDisplayName() {
        return Component.translatable(this.getOrCreateDescriptionId());
    }
}
