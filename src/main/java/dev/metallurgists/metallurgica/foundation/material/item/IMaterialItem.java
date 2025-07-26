package dev.metallurgists.metallurgica.foundation.material.item;

import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.interfaces.IItemRegistry;

public interface IMaterialItem {
    Material getMaterial();

    IItemRegistry getFlag();
}
