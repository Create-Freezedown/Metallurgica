package dev.metallurgists.metallurgica.infastructure.element;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.infastructure.IHasDescriptionId;
import com.tterrag.registrate.AbstractRegistrate;
import lombok.Getter;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;

@Getter
public class Element implements IHasDescriptionId {
    private String descriptionId;
    private final ResourceLocation id;

    private final String symbol;
    private final int color;

    public Element(Properties properties) {
        this.id = properties.id;
        this.symbol = properties.symbol;
        this.color = properties.color;
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
        private int color = 0x818181; // Default color

        public Properties() {

        }

        public Properties symbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Properties color(int color) {
            this.color = color;
            return this;
        }

        public Properties id(AbstractRegistrate<?> owner, String name) {
            this.id = new ResourceLocation(owner.getModid(), name);
            return this;
        }
    }
}
