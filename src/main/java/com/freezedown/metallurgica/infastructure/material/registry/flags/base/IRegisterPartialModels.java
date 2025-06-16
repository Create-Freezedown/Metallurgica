package com.freezedown.metallurgica.infastructure.material.registry.flags.base;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;

import java.util.List;

public interface IRegisterPartialModels {

    List<PartialModel> getPartialModels();

    default void initializePartialModels() {
        // Default implementation does nothing, can be overridden by subclasses
    }
}
