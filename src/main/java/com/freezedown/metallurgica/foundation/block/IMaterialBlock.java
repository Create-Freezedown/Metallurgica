package com.freezedown.metallurgica.foundation.block;

import com.freezedown.metallurgica.foundation.material.registry.Material;
import com.freezedown.metallurgica.foundation.material.registry.flags.base.BlockFlag;

public interface IMaterialBlock {

    public Material getMaterial();

    public BlockFlag getFlag();
}
