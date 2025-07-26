package dev.metallurgists.metallurgica.foundation.material.block;

import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.interfaces.IBlockRegistry;

public interface IMaterialBlock {

    Material getMaterial();

    IBlockRegistry getFlag();
}
