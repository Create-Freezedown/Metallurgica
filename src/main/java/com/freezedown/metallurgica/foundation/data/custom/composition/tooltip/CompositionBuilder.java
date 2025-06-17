package com.freezedown.metallurgica.foundation.data.custom.composition.tooltip;

import com.freezedown.metallurgica.foundation.data.custom.composition.FinishedComposition;
import com.freezedown.metallurgica.infastructure.element.data.SubComposition;
import com.freezedown.metallurgica.foundation.util.CommonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("deprecation")
public class CompositionBuilder {
    private final ItemLike item;
    private final List<SubComposition> subCompositions;
    
    public CompositionBuilder(ItemLike item, List<SubComposition> subCompositions) {
        this.item = item;
        this.subCompositions = subCompositions;
    }
    
    public static CompositionBuilder create(ItemLike item, List<SubComposition> subCompositions) {
        return new CompositionBuilder(item, subCompositions);
    }
    
    static ResourceLocation getDefaultCompositionId(ItemLike pItemLike) {
        return BuiltInRegistries.ITEM.getKey(pItemLike.asItem());
    }
    
    public void save(Consumer<FinishedComposition> pFinishedCompositionConsumer) {
        this.save(pFinishedCompositionConsumer, getDefaultCompositionId(this.item));
    }
    
    public void save(Consumer<FinishedComposition> pFinishedCompositionConsumer, ResourceLocation pCompositionId) {
        Item toApply = this.item.asItem();
        pFinishedCompositionConsumer.accept(new DataGenResult(pCompositionId, toApply, this.subCompositions));
    }
    
    public static class DataGenResult implements FinishedComposition {
        private final Item item;
        private final List<SubComposition> subCompositions;
        private ResourceLocation id;
        
        public DataGenResult(ResourceLocation pId, Item item, List<SubComposition> subCompositions) {
            this.item = item;
            this.subCompositions = subCompositions;
            this.id = pId;
        }

        
        @Override
        public void serializeData(JsonObject json) {
            json.addProperty("item", CommonUtil.getItemIdentifier(item));
            JsonArray compositionsArray = new JsonArray();
            for (SubComposition subComposition : subCompositions) {
                compositionsArray.add(subComposition.toJson());
            }
            json.add("compositions", compositionsArray);
        }
        
        @Override
        public ResourceLocation getId() {
            return id;
        }
    }
}
