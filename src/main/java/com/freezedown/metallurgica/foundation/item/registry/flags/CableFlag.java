package com.freezedown.metallurgica.foundation.item.registry.flags;

import com.freezedown.metallurgica.foundation.item.registry.flags.base.IMaterialFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.MaterialFlags;
import lombok.Getter;
import net.createmod.catnip.data.Pair;

public class CableFlag implements IMaterialFlag {

    @Getter
    private Pair<int[],int[]> colors;

    @Getter
    private double resistivity;

    @Getter
    private final String idPattern;

    public CableFlag(double resistivity, Pair<int[],int[]> colors) {
        this.idPattern = "%s_cable";
        this.resistivity = resistivity;
        this.colors = colors;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {
        flags.ensureSet(FlagKey.WIRE, true);
    }
}
