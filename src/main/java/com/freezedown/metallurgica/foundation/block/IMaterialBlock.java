package com.freezedown.metallurgica.foundation.block;

import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.BlockFlag;

public interface IMaterialBlock {

    public Material getMaterial();

    public BlockFlag getFlag();
}
