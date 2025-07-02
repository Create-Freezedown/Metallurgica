package com.freezedown.metallurgica.infastructure.conductor;

import com.freezedown.metallurgica.registry.misc.MetallurgicaRegistries;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import com.tterrag.registrate.util.nullness.NonnullType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;

public class ConductorBuilder<T extends Conductor, P> extends AbstractBuilder<Conductor, T, P, ConductorBuilder<T, P>> {

    public static <T extends Conductor, P> ConductorBuilder<T, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, NonNullFunction<Conductor.Properties, T> factory) {
        return new ConductorBuilder<>(owner, parent, name, callback, factory);
    }

    private final NonNullFunction<Conductor.Properties, T> factory;

    private NonNullSupplier<Conductor.Properties> initialProperties = () -> new Conductor.Properties().id(getOwner(), getName());
    private NonNullFunction<Conductor.Properties, Conductor.Properties> propertiesCallback = NonNullUnaryOperator.identity();

    public ConductorBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, NonNullFunction<Conductor.Properties, T> factory) {
        super(owner, parent, name, callback, MetallurgicaRegistries.CONDUCTOR_KEY);
        this.factory = factory;
    }

    public ConductorBuilder<T, P> properties(NonNullUnaryOperator<Conductor.Properties> func) {
        propertiesCallback = propertiesCallback.andThen(func);
        return this;
    }

    public ConductorBuilder<T, P> initialProperties(NonNullSupplier<Conductor.Properties> properties) {
        initialProperties = properties;
        return this;
    }

    public ConductorBuilder<T, P> defaultLang() {
        return lang(Conductor::getDescriptionId);
    }

    public ConductorBuilder<T, P> lang(String name) {
        return lang(Conductor::getDescriptionId, name);
    }

    @Override
    protected @NonnullType T createEntry() {
        Conductor.Properties properties = this.initialProperties.get();
        properties = propertiesCallback.apply(properties);
        return factory.apply(properties);
    }

    @Override
    protected RegistryEntry<T> createEntryWrapper(RegistryObject<T> delegate) {
        return new ConductorEntry<>(getOwner(), delegate);
    }

    @Override
    public ConductorEntry<T> register() {
        MetallurgicaRegistries.registeredConductors.put(new ResourceLocation(getOwner().getModid(), getName()), createEntry());
        return (ConductorEntry<T>) super.register();
    }
}
