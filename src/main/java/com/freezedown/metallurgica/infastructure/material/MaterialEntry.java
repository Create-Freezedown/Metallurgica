package com.freezedown.metallurgica.infastructure.material;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraftforge.registries.RegistryObject;

public class MaterialEntry<T extends Material> extends RegistryEntry<T> {
    public MaterialEntry(AbstractRegistrate<?> owner, RegistryObject<T> delegate) {
        super(owner, delegate);
    }

    public static <T extends Material> MaterialEntry<T> cast(RegistryEntry<T> entry) {
        return RegistryEntry.cast(MaterialEntry.class, entry);
    }
}
