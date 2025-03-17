package com.freezedown.metallurgica.foundation.item.registry.flags;

import com.freezedown.metallurgica.foundation.item.registry.flags.base.IMaterialFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.MaterialFlags;
import lombok.Getter;
import net.createmod.catnip.data.Pair;

public class WiringFlag implements IMaterialFlag {

    @Getter
    private Pair<int[],int[]> colors;

    @Getter
    private double resistivity;

    public WiringFlag(double resistivity, Pair<int[],int[]> colors) {
        this.resistivity = resistivity;
        this.colors = colors;
    }

    public void setResistivity(double resistivity) {
        if (resistivity <= 0) throw new IllegalArgumentException("Conductor Resistivity must be greater than zero!");
        this.resistivity = resistivity;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {
        flags.ensureSet(FlagKey.SHEET, false);
    }
}
