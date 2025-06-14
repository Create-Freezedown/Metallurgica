package com.freezedown.metallurgica.foundation.item.registry.flags.base;

import com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

import java.util.List;

public interface IRegisterPartialModels {

    List<PartialModel> getPartialModels();

    default void initializePartialModels() {
        // Default implementation does nothing, can be overridden by subclasses
    }
}
