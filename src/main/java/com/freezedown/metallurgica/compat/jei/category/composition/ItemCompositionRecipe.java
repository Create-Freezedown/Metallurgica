package com.freezedown.metallurgica.compat.jei.category.composition;

import com.freezedown.metallurgica.foundation.config.MetallurgicaConfigs;
import com.freezedown.metallurgica.foundation.data.custom.composition.Element;
import com.simibubi.create.foundation.utility.Components;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemCompositionRecipe {
    public final Item item;
    public final List<Element> elements;

    public ItemCompositionRecipe(Item item, List<Element> elements) {
        this.item = item;
        this.elements = elements;
    }

    public Item getItem() {
        return item;
    }

    public List<Element> getElements() {
        return elements;
    }

    public List<MutableComponent> createElementLine() {
        Map<String, Integer> elementCounts = new HashMap<>();
        int totalElementsAmount = 0;
        List<MutableComponent> lines = new ArrayList<>();
        for (Element element : getElements()) {
            String elementName = element.name();
            elementCounts.put(elementName, elementCounts.getOrDefault(elementName, 0) + element.amount());
            totalElementsAmount += element.amount();
        }

        for (Map.Entry<String, Integer> entry : elementCounts.entrySet()) {
            String elementName = entry.getKey();
            int elementAmount = entry.getValue();

            MutableComponent line = Components.empty();
            float percentage = (float) elementAmount / totalElementsAmount * 100;

            line.append(Components.translatable("metallurgica.element." + elementName)).append(" ");
            if (MetallurgicaConfigs.client().whatAreTheseElements.get()) {
                line.append("(").append(Components.translatable("metallurgica.element.name." + elementName.toLowerCase())).append(") ");
            }
            //The percentage should only have 3 decimal places
            percentage = (float) Math.round(percentage * 1000) / 1000;
            line.append("=> " + percentage + "%");

            lines.add(line);
        }
        return lines;
    }


}
