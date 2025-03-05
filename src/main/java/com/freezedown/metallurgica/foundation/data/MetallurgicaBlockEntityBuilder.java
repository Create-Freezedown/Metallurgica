package com.freezedown.metallurgica.foundation.data;

import com.simibubi.create.api.behaviour.display.DisplaySource;
import com.simibubi.create.api.behaviour.display.DisplayTarget;
import com.simibubi.create.api.registry.CreateRegistries;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BlockEntityBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.OneTimeEventReceiver;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.NonNullPredicate;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public class MetallurgicaBlockEntityBuilder<T extends BlockEntity, P> extends BlockEntityBuilder<T, P> {

    @Nullable
    private NonNullSupplier<SimpleBlockEntityVisualizer.Factory<T>> visualFactory;
    private NonNullPredicate<T> renderNormally;

    private Collection<NonNullSupplier<? extends Collection<NonNullSupplier<? extends Block>>>> deferredValidBlocks =
            new ArrayList<>();

    public static <T extends BlockEntity, P> BlockEntityBuilder<T, P> create(AbstractRegistrate<?> owner, P parent,
                                                                             String name, BuilderCallback callback, BlockEntityFactory<T> factory) {
        return new MetallurgicaBlockEntityBuilder<>(owner, parent, name, callback, factory);
    }

    protected MetallurgicaBlockEntityBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback,
                                       BlockEntityFactory<T> factory) {
        super(owner, parent, name, callback, factory);
    }

    public MetallurgicaBlockEntityBuilder<T, P> validBlocksDeferred(
            NonNullSupplier<? extends Collection<NonNullSupplier<? extends Block>>> blocks) {
        deferredValidBlocks.add(blocks);
        return this;
    }

    @Override
    protected BlockEntityType<T> createEntry() {
        deferredValidBlocks.stream()
                .map(Supplier::get)
                .flatMap(Collection::stream)
                .forEach(this::validBlock);
        return super.createEntry();
    }

    public MetallurgicaBlockEntityBuilder<T, P> displaySource(RegistryEntry<? extends DisplaySource> source) {
        this.onRegisterAfter(
                CreateRegistries.DISPLAY_SOURCE,
                type -> DisplaySource.BY_BLOCK_ENTITY.add(type, source.get())
        );
        return this;
    }

    public MetallurgicaBlockEntityBuilder<T, P> displayTarget(RegistryEntry<? extends DisplayTarget> target) {
        this.onRegisterAfter(
                CreateRegistries.DISPLAY_TARGET,
                type -> DisplayTarget.BY_BLOCK_ENTITY.register(type, target.get())
        );
        return this;
    }

    public MetallurgicaBlockEntityBuilder<T, P> visual(
            NonNullSupplier<SimpleBlockEntityVisualizer.Factory<T>> visualFactory) {
        return visual(visualFactory, true);
    }

    public MetallurgicaBlockEntityBuilder<T, P> visual(
            NonNullSupplier<SimpleBlockEntityVisualizer.Factory<T>> visualFactory,
            boolean renderNormally) {
        return visual(visualFactory, be -> renderNormally);
    }

    public MetallurgicaBlockEntityBuilder<T, P> visual(
            NonNullSupplier<SimpleBlockEntityVisualizer.Factory<T>> visualFactory,
            NonNullPredicate<T> renderNormally) {
        if (this.visualFactory == null) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> this::registerVisualizer);
        }

        this.visualFactory = visualFactory;
        this.renderNormally = renderNormally;

        return this;
    }

    protected void registerVisualizer() {
        OneTimeEventReceiver.addModListener(getOwner(), FMLClientSetupEvent.class, $ -> {
            var visualFactory = this.visualFactory;
            if (visualFactory != null) {
                NonNullPredicate<T> renderNormally = this.renderNormally;
                SimpleBlockEntityVisualizer.builder(getEntry())
                        .factory(visualFactory.get())
                        .skipVanillaRender(be -> !renderNormally.test(be))
                        .apply();
            }
        });
    }
}
