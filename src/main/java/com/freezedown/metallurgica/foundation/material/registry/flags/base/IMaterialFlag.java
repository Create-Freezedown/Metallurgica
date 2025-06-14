package com.freezedown.metallurgica.foundation.material.registry.flags.base;

import com.freezedown.metallurgica.foundation.material.registry.flags.FlagKey;

public interface IMaterialFlag {

    FlagKey<?> getKey();

    void verifyFlag(MaterialFlags flags);

}
