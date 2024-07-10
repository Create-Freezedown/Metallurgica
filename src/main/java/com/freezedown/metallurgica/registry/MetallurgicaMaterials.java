package com.freezedown.metallurgica.registry;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.MetallurgicaRegistrate;
import com.freezedown.metallurgica.foundation.material.MaterialEntry;
import com.freezedown.metallurgica.foundation.worldgen.MOreFeatureConfigEntry;

public enum MetallurgicaMaterials {
    NATIVE_COPPER(26, 7, -3, 97, 1, 8, -3, 97),
    NATIVE_GOLD(12, 4, -12, 56, 1, 5, -12, 56),
    MAGNETITE(23, 4, -3, 128, 1, 5, -3, 128),
    BAUXITE(19, 9, -30, 70, 1, 10, -30, 70)
    ;

    public final MOreFeatureConfigEntry DEPOSIT;
    public final MOreFeatureConfigEntry CLUSTER;
    public final MaterialEntry materialEntry;
    MetallurgicaMaterials(int clusterSize, int clusterFrequency, int clusterMinHeight, int clusterMaxHeight, int depositSize, int depositFrequency, int depositMinHeight, int depositMaxHeight) {
        MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.itemGroup);
        materialEntry = registrate.material(this.name().toLowerCase());
        DEPOSIT = materialEntry.deposit(depositSize, depositFrequency, depositMinHeight, depositMaxHeight);
        CLUSTER = materialEntry.cluster(clusterSize, clusterFrequency, clusterMinHeight, clusterMaxHeight);
    }
}
