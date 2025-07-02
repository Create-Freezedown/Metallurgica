package com.freezedown.metallurgica.infastructure.material.registry.flags.base;

import com.freezedown.metallurgica.infastructure.material.Material;
import com.freezedown.metallurgica.infastructure.material.registry.flags.FlagKey;
import net.minecraft.resources.ResourceLocation;

public interface IMaterialFlag {

    FlagKey<?> getKey();

    void verifyFlag(MaterialFlags flags);

    ResourceLocation getExistingId(Material material);
}
