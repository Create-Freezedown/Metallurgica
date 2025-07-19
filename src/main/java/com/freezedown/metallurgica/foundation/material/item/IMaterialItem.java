package com.freezedown.metallurgica.foundation.material.item;

import com.freezedown.metallurgica.infastructure.material.Material;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.ItemFlag;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.interfaces.IItemRegistry;

public interface IMaterialItem {
    Material getMaterial();

    IItemRegistry getFlag();
}
