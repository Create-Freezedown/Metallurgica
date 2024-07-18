package com.freezedown.metallurgica.registry;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.MetallurgicaRegistrate;
import com.freezedown.metallurgica.foundation.material.MaterialEntry;
import com.freezedown.metallurgica.foundation.worldgen.MOreFeatureConfigEntry;

public enum MetallurgicaMaterials {
    //COPPER
    NATIVE_COPPER(26, 7, -3, 97, 8, -3, 97),
    MALACHITE(true),
    CHALKOPYRITE(),

    //GOLD
    NATIVE_GOLD(12, 4, -12, 56, 5, -12, 56),

    //IRON
    MAGNETITE(23, 4, -3, 128, 5, -3, 128, true),
    HEMATITE(),
    PENTLANDITE(),

    //LITHIUM/ALUMINUM
    BAUXITE(19, 9, -30, 70, 10, -30, 70),
    PETALITE(),
    SPODUMENE(),

    //LEAD
    GALENA(),
    NATIVE_LEAD(),
    PYROMORPHITE(),

    //ZINC
    SPHALRITE(),

    //TUNGSTEN
    WOLFRAMITE(),

    //TITANIUM
    RUTILE(),

    //URANIUM/THORIUM
    URANINITE(),
    MONAZITE(),

    //PLATINUM
    SPERRYLITE(),

    //Silver
    ARGENTITE(),
    CHLORARGYRITE(),

    //LOTION (????)
    CALAMINE(),

    //LIMESTONE
    DOLOMITE(),

    //MAGNESIUM
    MEGNESITE(),
    CASSITERITE()
    ;

    public MOreFeatureConfigEntry DEPOSIT;
    public MOreFeatureConfigEntry CLUSTER;
    public final MaterialEntry MATERIAL;

    MetallurgicaMaterials() {
        MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.materialItemGroup);
        MATERIAL = registrate.material(this.name().toLowerCase(), false);
    }

    MetallurgicaMaterials(boolean richb) {
        MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.materialItemGroup);
        MATERIAL = registrate.material(this.name().toLowerCase(), richb);
    }

    MetallurgicaMaterials(int clusterSize, int clusterFrequency, int clusterMinHeight, int clusterMaxHeight, int depositFrequency, int depositMinHeight, int depositMaxHeight) {
        MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.materialItemGroup);
        MATERIAL = registrate.material(this.name().toLowerCase(), false);
        DEPOSIT = MATERIAL.deposit(1, depositFrequency, depositMinHeight, depositMaxHeight);
        CLUSTER = MATERIAL.cluster(clusterSize, clusterFrequency, clusterMinHeight, clusterMaxHeight);
    }

    MetallurgicaMaterials(int clusterSize, int clusterFrequency, int clusterMinHeight, int clusterMaxHeight, int depositFrequency, int depositMinHeight, int depositMaxHeight, boolean richb) {
        MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.materialItemGroup);
        MATERIAL = registrate.material(this.name().toLowerCase(), richb);
        DEPOSIT = MATERIAL.deposit(1, depositFrequency, depositMinHeight, depositMaxHeight);
        CLUSTER = MATERIAL.cluster(clusterSize, clusterFrequency, clusterMinHeight, clusterMaxHeight);
    }
}
