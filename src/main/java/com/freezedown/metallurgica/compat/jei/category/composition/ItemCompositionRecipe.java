package com.freezedown.metallurgica.compat.jei.category.composition;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.config.MetallurgicaConfigs;
import com.freezedown.metallurgica.infastructure.element.Element;
import com.freezedown.metallurgica.infastructure.element.data.ElementData;
import com.freezedown.metallurgica.infastructure.element.data.SubComposition;
import com.freezedown.metallurgica.registry.MetallurgicaRecipeTypes;
import com.freezedown.metallurgica.registry.misc.MetallurgicaElements;
import com.freezedown.metallurgica.registry.misc.MetallurgicaRegistries;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemCompositionRecipe extends ProcessingRecipe<RecipeWrapper> {
    public Item item;
    public List<SubComposition> subCompositions;

    static int counter = 0;

    public ItemCompositionRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(MetallurgicaRecipeTypes.item_composition, params);
    }

    public static ItemCompositionRecipe create(Item item, List<SubComposition> subCompositions) {
        ResourceLocation recipeId = Metallurgica.asResource("item_composition_" + counter++);
        return new ProcessingRecipeBuilder<>((params) -> new ItemCompositionRecipe(params).set(item,subCompositions), recipeId)
                .build();
    }

    public ItemCompositionRecipe set(Item item, List<SubComposition> subCompositions) {
        this.item = item;
        this.subCompositions = subCompositions;
        return this;
    }

    public Item getItem() {
        return item;
    }

    public List<SubComposition> getSubCompositions() {
        return subCompositions;
    }

    public List<MutableComponent> createElementLine() {
        Map<ResourceLocation, Integer> elementCounts = new HashMap<>();
        int totalElementsAmount = 0;
        List<MutableComponent> lines = new ArrayList<>();
        for (SubComposition subComposition : getSubCompositions()) {
            for (ElementData elementData : subComposition.getElements()) {
                ResourceLocation element = elementData.element();
                elementCounts.put(element, elementCounts.getOrDefault(element, 0) + elementData.amount());
                totalElementsAmount += elementData.amount();
            }
        }

        for (Map.Entry<ResourceLocation, Integer> entry : elementCounts.entrySet()) {
            ResourceLocation elementKey = entry.getKey();
            int elementAmount = entry.getValue();

            MutableComponent line = Component.empty();
            float percentage = (float) elementAmount / totalElementsAmount * 100;

            Element element = MetallurgicaRegistries.registeredElements.getOrDefault(elementKey, MetallurgicaElements.NULL.get());
            line.append(element.getSymbol()).append(" ");
            if (MetallurgicaConfigs.client().whatAreTheseElements.get()) {
                line.append("(").append(element.getDescriptionId()).append(") ");
            }
            //The percentage should only have 3 decimal places
            percentage = (float) Math.round(percentage * 1000) / 1000;
            line.append("=> " + percentage + "%");

            lines.add(line);
        }
        return lines;
    }


    @Override
    protected int getMaxInputCount() {
        return 1;
    }

    @Override
    protected int getMaxOutputCount() {
        return 0;
    }

    @Override
    public boolean matches(RecipeWrapper recipeWrapper, Level level) {
        return false;
    }
}
