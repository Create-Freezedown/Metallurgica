package dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.interfaces;

import dev.metallurgists.metallurgica.infastructure.material.Material;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;

public interface IHaveConnectedTextures {

    CTSpriteShiftEntry getSpriteShiftEntry(Material material);
}
