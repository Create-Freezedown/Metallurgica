package com.freezedown.metallurgica.foundation.material.block;

import com.freezedown.metallurgica.infastructure.material.Material;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.BlockFlag;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.interfaces.IBlockRegistry;

public interface IMaterialBlock {

    Material getMaterial();

    IBlockRegistry getFlag();
}
