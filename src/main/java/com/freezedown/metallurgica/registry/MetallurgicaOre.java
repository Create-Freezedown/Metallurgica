package com.freezedown.metallurgica.registry;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.MetallurgicaRegistrate;
import com.freezedown.metallurgica.foundation.material.MaterialEntry;
import com.freezedown.metallurgica.foundation.material.MetalEntry;
import com.freezedown.metallurgica.foundation.worldgen.MOreFeatureConfigEntry;

import java.util.List;

import static com.freezedown.metallurgica.registry.MetallurgicaMetals.*;
import static com.freezedown.metallurgica.registry.MetallurgicaMetals.ZINC; //its dumb, so dont get rid of this
import static com.freezedown.metallurgica.registry.vanilla.MetallurgicaMetals.*;

public enum MetallurgicaOre {
    //COPPER
    NATIVE_COPPER(26, 7, -3, 97, 8, -3, 97, COPPER),
    MALACHITE(true, COPPER),
    CHALKOPYRITE(COPPER),
    //CHALKOPYRITE(),

    //GOLD
    NATIVE_GOLD(12, 4, -12, 56, 5, -12, 56, GOLD),

    //IRON
    MAGNETITE(23, 4, -3, 128, 5, -3, 128, true, IRON),
//    HEMATITE(IRON),
//    PENTLANDITE(IRON),

    //LITHIUM/ALUMINUM
    BAUXITE(19, 9, -30, 70, 10, -30, 70, ALUMINUM),
//    PETALITE(ALUMINUM, LITHIUM),
//    SPODUMENE(ALUMINUM, LITHIUM),

    //LEAD
//    GALENA(LEAD),
//    NATIVE_LEAD(LEAD),
//    PYROMORPHITE(LEAD),

    //ZINC
    SPHALRITE(ZINC),
    CALAMINE(ZINC),
    //SPHALRITE(),

    //TUNGSTEN
//    WOLFRAMITE(TUNGSTEN),

    //TITANIUM
    RUTILE(TITANIUM),

    //URANIUM/THORIUM
//    URANINITE(URANIUM),
//    MONAZITE(URANIUM, THORIUM),

    //PLATINUM
    //SPERRYLITE(),
    SPERRYLITE(PLATINUM),

    //LOTION (????)
    //CALAMINE(),

    //SILVER
//    ARGENTITE(SILVER),
//    CHLORARGYRITE(SILVER),

    //LIMESTONE
    DOLOMITE(),

    //MAGNESIUM
    MEGNESITE(MAGNESIUM),
    CASSITERITE(MAGNESIUM),

    //FLUORITE
    FLUORITE(),
    ;

    public MOreFeatureConfigEntry DEPOSIT;
    public MOreFeatureConfigEntry CLUSTER;
    public final MaterialEntry MATERIAL;
    public List<MetalEntry> METALS;

    MetallurgicaOre() {
        MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.materialItemGroup);
        MATERIAL = registrate.material(this.name().toLowerCase(), false);
    }

    MetallurgicaOre(com.freezedown.metallurgica.registry.MetallurgicaMetals... metals) {
        MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.materialItemGroup);
        MATERIAL = registrate.material(this.name().toLowerCase(), false);
        for(com.freezedown.metallurgica.registry.MetallurgicaMetals metal : metals) {
            METALS.add(metal.METAL);
        }
    }

    MetallurgicaOre(MetallurgicaMetals... metals) {
        MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.materialItemGroup);
        MATERIAL = registrate.material(this.name().toLowerCase(), false);
        for(MetallurgicaMetals metal : metals) {
            METALS.add(metal.METAL);
        }
    }

    MetallurgicaOre(boolean richb, com.freezedown.metallurgica.registry.MetallurgicaMetals... metals) {
        MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.materialItemGroup);
        MATERIAL = registrate.material(this.name().toLowerCase(), richb);
        for(com.freezedown.metallurgica.registry.MetallurgicaMetals metal : metals) {
            METALS.add(metal.METAL);
        }
    }

    MetallurgicaOre(boolean richb, MetallurgicaMetals... metals) {
        MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.materialItemGroup);
        MATERIAL = registrate.material(this.name().toLowerCase(), richb);
        for(MetallurgicaMetals metal : metals) {
            METALS.add(metal.METAL);
        }
    }

    MetallurgicaOre(int clusterSize,
                    int clusterFrequency, int clusterMinHeight, int clusterMaxHeight,
                    int depositFrequency, int depositMinHeight, int depositMaxHeight,
                    com.freezedown.metallurgica.registry.MetallurgicaMetals... metals) {
        MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.materialItemGroup);
        MATERIAL = registrate.material(this.name().toLowerCase(), false);
        DEPOSIT = MATERIAL.deposit(1, depositFrequency, depositMinHeight, depositMaxHeight);
        CLUSTER = MATERIAL.cluster(clusterSize, clusterFrequency, clusterMinHeight, clusterMaxHeight);
        for(com.freezedown.metallurgica.registry.MetallurgicaMetals metal : metals) {
            METALS.add(metal.METAL);
        }
    }

    MetallurgicaOre(int clusterSize,
                    int clusterFrequency, int clusterMinHeight, int clusterMaxHeight,
                    int depositFrequency, int depositMinHeight, int depositMaxHeight,
                    MetallurgicaMetals... metals) {
        MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.materialItemGroup);
        MATERIAL = registrate.material(this.name().toLowerCase(), false);
        DEPOSIT = MATERIAL.deposit(1, depositFrequency, depositMinHeight, depositMaxHeight);
        CLUSTER = MATERIAL.cluster(clusterSize, clusterFrequency, clusterMinHeight, clusterMaxHeight);
        for(MetallurgicaMetals metal : metals) {
            METALS.add(metal.METAL);
        }
    }

    MetallurgicaOre(int clusterSize,
                    int clusterFrequency, int clusterMinHeight, int clusterMaxHeight,
                    int depositFrequency, int depositMinHeight, int depositMaxHeight,
                    boolean richb,
                    MetallurgicaMetals... metals) {
        MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.materialItemGroup);
        MATERIAL = registrate.material(this.name().toLowerCase(), richb);
        DEPOSIT = MATERIAL.deposit(1, depositFrequency, depositMinHeight, depositMaxHeight);
        CLUSTER = MATERIAL.cluster(clusterSize, clusterFrequency, clusterMinHeight, clusterMaxHeight);
        for(MetallurgicaMetals metal : metals) {
            METALS.add(metal.METAL);
        }
    }
}
