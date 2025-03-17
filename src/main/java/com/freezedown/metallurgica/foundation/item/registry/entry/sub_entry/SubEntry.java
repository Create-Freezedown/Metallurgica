package com.freezedown.metallurgica.foundation.item.registry.entry.sub_entry;

import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;

@Getter
public class SubEntry {

    private final MetallurgicaRegistrate registrate;
    private final String name;


    public SubEntry(MetallurgicaRegistrate registrate, String name) {
        this.registrate = registrate;
        this.name = name;
    }


    String formatName(String string) {
        return string.formatted(getName());
    }

    ResourceLocation loc(String path) {
        return new ResourceLocation(getRegistrate().getModid(), path.formatted(getName()));
    }

    ResourceLocation locI(String path) {
        return new ResourceLocation(getRegistrate().getModid(), "item/" + path.formatted(getName()));
    }

    ResourceLocation locB(String path) {
        return new ResourceLocation(getRegistrate().getModid(), "block/" + path.formatted(getName()));
    }

    ResourceLocation locF(String path) {
        return new ResourceLocation(getRegistrate().getModid(), "fluid/" + path.formatted(getName()));
    }
}
