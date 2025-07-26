package dev.metallurgists.metallurgica.infastructure.element;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraftforge.registries.RegistryObject;

public class ElementEntry<T extends Element> extends RegistryEntry<T> {
    public ElementEntry(AbstractRegistrate<?> owner, RegistryObject<T> delegate) {
        super(owner, delegate);
    }

    public static <T extends Element> ElementEntry<T> cast(RegistryEntry<T> entry) {
        return RegistryEntry.cast(ElementEntry.class, entry);
    }
}
