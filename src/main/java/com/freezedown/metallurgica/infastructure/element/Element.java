package com.freezedown.metallurgica.infastructure.element;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.infastructure.IHasDescriptionId;
import com.tterrag.registrate.AbstractRegistrate;
import lombok.Getter;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;

@Getter
public class Element implements IHasDescriptionId {
    private String descriptionId;
    private final ResourceLocation id;

    private final String symbol;

    public Element(Properties properties) {
        this.id = properties.id;
        this.symbol = properties.symbol;
    }

    public String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("element", getKey());
        }

        return this.descriptionId;
    }

    public ResourceLocation getKey() {
        return this.id;
    }

    public static class Properties {
        private ResourceLocation id = Metallurgica.asResource("null");
        private String symbol = "null";

        public Properties() {

        }

        public Properties symbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Properties id(AbstractRegistrate<?> owner, String name) {
            this.id = new ResourceLocation(owner.getModid(), name);
            return this;
        }
    }
}
