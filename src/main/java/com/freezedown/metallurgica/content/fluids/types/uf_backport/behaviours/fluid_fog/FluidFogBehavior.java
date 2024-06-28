package com.freezedown.metallurgica.content.fluids.types.uf_backport.behaviours.fluid_fog;

import oshi.util.tuples.Triplet;

import java.util.Optional;

public record FluidFogBehavior(
        Triplet<Optional<Float>, Optional<Float>, Optional<Float>> fogColor,
        Optional<Float> scaleFarPlaneDistance
) implements IFluidFogBehavior {}
