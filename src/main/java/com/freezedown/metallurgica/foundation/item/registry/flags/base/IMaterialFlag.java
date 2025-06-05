package com.freezedown.metallurgica.foundation.item.registry.flags.base;

import com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey;

public interface IMaterialFlag {

    FlagKey<?> getKey();

    void verifyFlag(MaterialFlags flags);

}
