package dev.metallurgists.metallurgica.foundation.util;

import net.minecraftforge.common.util.NonNullConsumer;

import java.lang.ref.WeakReference;

/**
 * Implementation of {@link NonNullConsumer} that weakly references a parent object.
 * Designed for use in {@link net.minecraftforge.common.util.LazyOptional#addListener(NonNullConsumer)},
 * to prevent the capability owner from keeping a reference to the listener TE and preventing garbage collection.
 * @param <TE>  Parent object type, typically a TE
 * @param <C>   Consumer value
 */
public class WeakConsumerWrapper<TE,C> implements NonNullConsumer<C> {
    private final WeakReference<TE> blockEntity;
    private final NonnullBiConsumer<TE,C> consumer;
    
    /**
     * Creates a new weak consumer wrapper
     * @param te        Weak reference, typically to a TE
     * @param consumer  Consumer using the TE and the consumed value. Should not use a lambda reference to an object that may need to be garbage collected
     */
    public WeakConsumerWrapper(TE blockEntity, NonnullBiConsumer<TE,C> consumer) {
        this.blockEntity = new WeakReference<>(blockEntity);
        this.consumer = consumer;
    }
    
    @Override
    public void accept(C c) {
        TE te = this.blockEntity.get();
        if (te != null) {
            consumer.accept(te, c);
        }
    }
}
