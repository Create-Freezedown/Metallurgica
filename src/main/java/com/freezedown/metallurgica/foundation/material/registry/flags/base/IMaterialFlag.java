package com.freezedown.metallurgica.foundation.material.registry.flags.base;

import com.freezedown.metallurgica.foundation.material.registry.Material;
import com.freezedown.metallurgica.foundation.material.registry.flags.FlagKey;
import net.minecraft.resources.ResourceLocation;

public interface IMaterialFlag {

    FlagKey<?> getKey();

    void verifyFlag(MaterialFlags flags);

    ResourceLocation getExistingId(Material material);
}
