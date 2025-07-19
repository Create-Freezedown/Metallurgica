package com.freezedown.metallurgica.infastructure.material.registry.flags.base.interfaces;

import com.freezedown.metallurgica.infastructure.material.Material;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;

public interface IHaveConnectedTextures {

    CTSpriteShiftEntry getSpriteShiftEntry(Material material);
}
