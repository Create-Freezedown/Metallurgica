package com.freezedown.metallurgica.foundation.material.block;

import com.freezedown.metallurgica.infastructure.material.Material;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.BlockFlag;

public interface IMaterialBlock {

    Material getMaterial();

    BlockFlag getFlag();
}
