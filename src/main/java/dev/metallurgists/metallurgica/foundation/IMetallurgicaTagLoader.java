package dev.metallurgists.metallurgica.foundation;

import net.minecraft.core.Registry;
import org.jetbrains.annotations.Nullable;

public interface IMetallurgicaTagLoader<T> {

    void metallurgica$setRegistry(Registry<T> registry);

    @Nullable
    Registry<T> metallurgica$getRegistry();
}
