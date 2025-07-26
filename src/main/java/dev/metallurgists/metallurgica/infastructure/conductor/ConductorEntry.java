package dev.metallurgists.metallurgica.infastructure.conductor;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraftforge.registries.RegistryObject;

public class ConductorEntry<T extends Conductor> extends RegistryEntry<T> {
    public ConductorEntry(AbstractRegistrate<?> owner, RegistryObject<T> delegate) {
        super(owner, delegate);
    }

    public static <T extends Conductor> ConductorEntry<T> cast(RegistryEntry<T> entry) {
        return RegistryEntry.cast(ConductorEntry.class, entry);
    }
}
