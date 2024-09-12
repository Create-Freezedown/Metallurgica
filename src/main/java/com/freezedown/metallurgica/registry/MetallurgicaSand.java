
package com.freezedown.metallurgica.registry;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.material.SandEntry;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.foundation.worldgen.config.MSandFeatureConfigEntry;
import com.freezedown.metallurgica.foundation.worldgen.config.MTypedDepositFeatureConfigEntry;


public enum MetallurgicaSand {
    BLACK_SAND(false),
    RED_SAND(true),
    QUARTZ_SAND(false)
    ;

    public final SandEntry SAND;
    public MSandFeatureConfigEntry CLUSTER;
    public MTypedDepositFeatureConfigEntry DEPOSIT;

    MetallurgicaSand(boolean existing) {
        MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.materialItemGroup);
        SAND = registrate.sand(this.name().toLowerCase(), existing);
    }


    MetallurgicaSand(boolean existing, int clusterSize, int clusterFrequency, int clusterMinHeight, int clusterMaxHeight) {

        MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.materialItemGroup);
        SAND = registrate.sand(this.name().toLowerCase(), existing);
    }
}
