package com.freezedown.metallurgica.registry;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.MetallurgicaRegistrate;
import com.freezedown.metallurgica.foundation.material.MaterialEntry;
import com.freezedown.metallurgica.foundation.worldgen.MOreFeatureConfigEntry;

public enum MetallurgicaMaterials {
    NATIVE_COPPER(26, 7, -3, 97, 8, -3, 97),
    NATIVE_GOLD(12, 4, -12, 56, 5, -12, 56),
    MAGNETITE(23, 4, -3, 128, 5, -3, 128),
    BAUXITE(19, 9, -30, 70, 10, -30, 70),
    MALACHITE(),
    //HEMATITE(),
    //CHALKOPYRITE(),
    //GALENA(),
    //PYROMORPHITE(),
    //NATIVE_LEAD(),
    //PENTLANDITE(),
    //ARGENTITE(),
    //CHLORARGYRITE(),
    //SPHALRITE(),
    //CALAMINE(),
    //PETALITE(),
    //SPODUMENE(),
    //DOLOMITE(),
    //MEGNESITE(),
    //WOLFRAMITE(),
    //RUTILE(),
    //URANINITE(),
    //SPERRYLITE(),
    //MONAZITE(),
    CASSITERITE(),
    FLUORITE(),
    ;

    public MOreFeatureConfigEntry DEPOSIT;
    public MOreFeatureConfigEntry CLUSTER;
    public final MaterialEntry materialEntry;

    MetallurgicaMaterials() {
        MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.materialItemGroup);
        materialEntry = registrate.material(this.name().toLowerCase());
    }

    MetallurgicaMaterials(int clusterSize, int clusterFrequency, int clusterMinHeight, int clusterMaxHeight, int depositFrequency, int depositMinHeight, int depositMaxHeight) {
        MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.materialItemGroup);
        materialEntry = registrate.material(this.name().toLowerCase());
        DEPOSIT = materialEntry.deposit(1, depositFrequency, depositMinHeight, depositMaxHeight);
        CLUSTER = materialEntry.cluster(clusterSize, clusterFrequency, clusterMinHeight, clusterMaxHeight);
    }
}
