package dev.metallurgists.metallurgica.infastructure.material.registry.flags.base;

import dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey;

public interface IMaterialFlag {

    FlagKey<?> getKey();

    void verifyFlag(MaterialFlags flags);

    //ResourceLocation getExistingId(Material material);
}
