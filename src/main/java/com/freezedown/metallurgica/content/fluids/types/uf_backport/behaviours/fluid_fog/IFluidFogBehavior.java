package com.freezedown.metallurgica.content.fluids.types.uf_backport.behaviours.fluid_fog;

import com.freezedown.metallurgica.content.fluids.types.uf_backport.behaviours.IFluidBehavior;
import oshi.util.tuples.Triplet;

import java.util.Optional;

public interface IFluidFogBehavior extends IFluidBehavior {
    Triplet<Optional<Float>, Optional<Float>, Optional<Float>> fogColor();
    
    Optional<Float> scaleFarPlaneDistance();
}
