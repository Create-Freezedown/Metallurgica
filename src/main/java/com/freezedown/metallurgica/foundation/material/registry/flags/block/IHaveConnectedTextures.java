package com.freezedown.metallurgica.foundation.material.registry.flags.block;

import com.freezedown.metallurgica.foundation.material.registry.Material;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;

public interface IHaveConnectedTextures {

    CTSpriteShiftEntry getSpriteShiftEntry(Material material);
}
