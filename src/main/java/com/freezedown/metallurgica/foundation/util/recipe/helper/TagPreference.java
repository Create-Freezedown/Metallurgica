package com.freezedown.metallurgica.foundation.util.recipe.helper;

import com.freezedown.metallurgica.foundation.util.LogicHelper;
import com.freezedown.metallurgica.foundation.util.RegistryHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.EventPriority;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class TagPreference {
    /** Just an alphabetically late RL to simplify null checks */
    private static final ResourceLocation DEFAULT_ID = new ResourceLocation("zzzzz:zzzzz"); // simplfies null checks
    
    /** Cache from any tag key to its value */
    private static final Map<TagKey<?>, Optional<?>> PREFERENCE_CACHE = new ConcurrentHashMap<>();
    /** Cache of comparator instances, not concurrent because it's only used inside {@link #getUncachedPreference(TagKey)} which is only used inside the concurrent {@link #PREFERENCE_CACHE}. */
    private static final Map<ResourceKey<?>, RegistryComparator<?>> COMPARATOR_CACHE = new HashMap<>();
    
    /** Registers the listener with the event bus */
    public static void init() {
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, TagsUpdatedEvent.class, e -> PREFERENCE_CACHE.clear());
    }
    
    /** Gets the comparator for the given registry */
    @SuppressWarnings("unchecked")
    private static <T> Comparator<T> getComparator(Registry<T> registry) {
        return (Comparator<T>)COMPARATOR_CACHE.computeIfAbsent(registry.key(), k -> new RegistryComparator<>(registry));
    }
    
    /** Gets the preference from a tag without going through the cache, internal logic behind {@link #getPreference(TagKey)} */
    private static <T> Optional<T> getUncachedPreference(TagKey<T> tag) {
        Registry<T> registry = RegistryHelper.getRegistry(tag.registry());
        if (registry == null) {
            return Optional.empty();
        }
        // streams have a lovely function to get the minimum element based on a comparator
        // if the tag is empty, stream is empty so returns empty
        return RegistryHelper.getTagValueStream(tag).min(getComparator(registry));
    }
    
    /** Don't create a new lambda instance every time we call {@link #getPreference(TagKey)} */
    private static final Function<TagKey<?>,Optional<?>> PREFERENCE_LOOKUP = TagPreference::getUncachedPreference;
    
    /**
     * Gets the preferred value from a tag based on mod ID
     * @param tag    Tag to fetch
     * @return  Preferred value from the tag, or empty optional if the tag is empty
     */
    @SuppressWarnings("unchecked")
    public static <T> Optional<T> getPreference(TagKey<T> tag) {
        // fetch cached value if we have one
        return (Optional<T>) PREFERENCE_CACHE.computeIfAbsent(tag, PREFERENCE_LOOKUP);
    }
    
    /** Logic to compare two registry values */
    private record RegistryComparator<T>(Registry<T> registry) implements Comparator<T> {
        @Override
        public int compare(T a, T b) {
            // first get registry names, use default ID if null (unlikely)
            ResourceLocation idA = Objects.requireNonNullElse(registry.getKey(a), DEFAULT_ID);
            ResourceLocation idB = Objects.requireNonNullElse(registry.getKey(b), DEFAULT_ID);
            // first compare preferences
            List<? extends String> entries = TagPreferenceManager.getTagPreferences();
            int size = entries.size();
            int indexA = LogicHelper.defaultIf(entries.indexOf(idA.getNamespace()), -1, size);
            int indexB = LogicHelper.defaultIf(entries.indexOf(idB.getNamespace()), -1, size);
            if (indexA != indexB) {
                return Integer.compare(indexA, indexB);
            }
            // for stability, fallback to registry name compare
            return idA.compareNamespaced(idB);
        }
    }
}
