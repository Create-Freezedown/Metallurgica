package com.freezedown.metallurgica.foundation.data.custom.composition.tooltip;

import com.freezedown.metallurgica.foundation.data.custom.composition.Element;
import com.freezedown.metallurgica.foundation.data.custom.composition.FinishedComposition;
import com.freezedown.metallurgica.foundation.util.CommonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.function.Consumer;

public class CompositionBuilder {
    private final ItemLike item;
    private final List<Element> elements;
    
    public CompositionBuilder(ItemLike item, List<Element> elements) {
        this.item = item;
        this.elements = elements;
    }
    
    public static CompositionBuilder create(ItemLike item, List<Element> elements) {
        return new CompositionBuilder(item, elements);
    }
    
    static ResourceLocation getDefaultCompositionId(ItemLike pItemLike) {
        return Registry.ITEM.getKey(pItemLike.asItem());
    }
    
    public void save(Consumer<FinishedComposition> pFinishedCompositionConsumer) {
        this.save(pFinishedCompositionConsumer, getDefaultCompositionId(this.item));
    }
    
    public void save(Consumer<FinishedComposition> pFinishedCompositionConsumer, ResourceLocation pCompositionId) {
        Item toApply = this.item.asItem();
        List<Element> elementsToApply = this.elements;
        pFinishedCompositionConsumer.accept(new DataGenResult(pCompositionId, toApply, elementsToApply));
    }
    
    public static class DataGenResult implements FinishedComposition {
        private final Item item;
        private final List<Element> elements;
        private ResourceLocation id;
        
        public DataGenResult(ResourceLocation pId, Item item, List<Element> elements) {
            this.item = item;
            this.elements = elements;
            this.id = pId;
        }
        
        @Override
        public void serializeData(JsonObject json) {
            json.addProperty("item", CommonUtil.getItemIdentifier(item));
            JsonArray elementsArray = new JsonArray();
            for (Element element : elements) {
                JsonObject elementObject = new JsonObject();
                elementObject.addProperty("element", element.getName());
                elementObject.addProperty("amount", element.getAmount());
                elementObject.addProperty("groupedAmount", element.getGroupedAmount());
                elementObject.addProperty("areNumbersUp", element.areNumbersUp());
                elementObject.addProperty("bracketed", element.bracketed());
                elementObject.addProperty("forceCloseBracket", element.isBracketForceClosed());
                elementObject.addProperty("appendDash", element.hasDash());
                elementsArray.add(elementObject);
            }
            json.add("elements", elementsArray);
        }
        
        @Override
        public ResourceLocation getId() {
            return id;
        }
    }
}
