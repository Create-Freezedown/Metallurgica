package com.freezedown.metallurgica.content.fluids.effects.corrosion;

import com.freezedown.metallurgica.content.fluids.types.Acid;
import net.minecraft.world.level.material.Fluid;

public interface Corrosive {

    float getAcidity();

    default float getAcidity(Fluid fluid) {
        if (fluid instanceof Corrosive corrosive) {
            return corrosive.getAcidity();
        }
        return 7.0f;
    }
}
