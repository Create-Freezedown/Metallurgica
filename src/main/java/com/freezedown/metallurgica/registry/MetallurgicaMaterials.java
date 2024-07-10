package com.freezedown.metallurgica.registry;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.MetallurgicaRegistrate;
import com.freezedown.metallurgica.foundation.material.MaterialEntry;

public class MetallurgicaMaterials {
    private static final MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.itemGroup);

    public static final MaterialEntry
            nativeCopper = registrate.material("native_copper"),
            nativeGold =   registrate.material("native_gold"),
            magnetite =    registrate.material("magnetite"),
            bauxite =      registrate.material("bauxite");
}
