package dev.metallurgists.metallurgica.content.fluids.effects.corrosion;

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
