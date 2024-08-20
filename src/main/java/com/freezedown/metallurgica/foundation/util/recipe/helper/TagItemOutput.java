package com.freezedown.metallurgica.foundation.util.recipe.helper;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class TagItemOutput implements Supplier<ItemStack> {
    /**
     * Gets the item output of this recipe
     * @return  Item output
     */
    @Override
    public abstract ItemStack get();
    
    /**
     * Writes this output to JSON
     * @param  writeCount  If true, serializes the count
     * @return  Json element
     */
    public abstract JsonElement serialize(boolean writeCount);
    
    /**
     * Creates a new output for the given tag
     * @param tag   Tag
     * @param count Stack count
     * @return Output
     */
    public static TagItemOutput fromTag(TagKey<Item> tag, int count) {
        return new OfTagPreference(tag, count);
    }
    
    /**
     * Creates a new output for the given tag
     * @param tag  Tag
     * @return Output
     */
    public static TagItemOutput fromTag(TagKey<Item> tag) {
        return fromTag(tag, 1);
    }
    
    /**
     * Writes this output to the packet buffer
     * @param buffer  Packet buffer instance
     */
    public void write(FriendlyByteBuf buffer) {
        buffer.writeItem(get());
    }
    
    /** Class for an output from a tag preference */
    private static class OfTagPreference extends TagItemOutput {
        private final TagKey<Item> tag;
        private final int count;
        private ItemStack cachedResult = null;
        
        public OfTagPreference(TagKey<Item> tag, int count) {
            this.tag = tag;
            this.count = count;
        }
        
        @Override
        public ItemStack get() {
            // cache the result from the tag preference to save effort, especially helpful if the tag becomes invalid
            // this object should only exist in recipes so no need to invalidate the cache
            if (cachedResult == null) {
                // if the preference is empty, do not cache it.
                // This should only happen if someone scans recipes before tag are computed in which case we cache the wrong resolt.
                // We protect against empty tags in our recipes via conditions.
                Optional<Item> preference = TagPreference.getPreference(tag);
                if (preference.isEmpty()) {
                    return ItemStack.EMPTY;
                }
                cachedResult = new ItemStack(preference.orElseThrow(), count);
            }
            return cachedResult;
        }
        
        @Override
        public JsonElement serialize(boolean writeCount) {
            JsonObject json = new JsonObject();
            json.addProperty("tag", tag.location().toString());
            if (writeCount) {
                json.addProperty("count", count);
            }
            return json;
        }
    }
}
