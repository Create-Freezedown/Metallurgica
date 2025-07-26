package dev.metallurgists.metallurgica.infastructure.conductor;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.infastructure.IHasDescriptionId;
import com.tterrag.registrate.AbstractRegistrate;
import lombok.Getter;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;


@Getter
public class Conductor implements IHasDescriptionId {
    private String descriptionId;
    private final ResourceLocation id;

    private final int[] color1;
    private final int[] color2;

    public Conductor(Properties properties) {
        this.color1 = properties.color1;
        this.color2 = properties.color2;
        this.id = properties.id;
    }

    public String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("conductor", getKey());
        }

        return this.descriptionId;
    }

    public ResourceLocation getKey() {
        return this.id;
    }

    public static class Properties {
        private ResourceLocation id = Metallurgica.asResource("null");

        int[] color1 = {193, 90, 54, 255};
        int[] color2 = {156, 78, 49, 255};

        public Properties() {

        }

        public Properties color1(int r, int g, int b, int a) {
            this.color1 = new int[]{r,g,b,a};
            return  this;
        }
        public Properties color1(int[] color) {
            this.color1 = color;
            return  this;
        }
        public Properties color2(int r, int g, int b, int a) {
            this.color2 = new int[]{r,g,b,a};
            return  this;
        }
        public Properties color2(int[] color) {
            this.color2 = color;
            return  this;
        }

        public Properties id(AbstractRegistrate<?> owner, String name) {
            this.id = new ResourceLocation(owner.getModid(), name);
            return this;
        }
    }
}
