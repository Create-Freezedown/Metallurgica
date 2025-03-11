package com.freezedown.metallurgica.foundation.item.registry.entry.sub_entry;

import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import lombok.Getter;

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
}
