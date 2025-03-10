package com.freezedown.metallurgica.compat.jei.category.composition;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.config.MetallurgicaConfigs;
import com.freezedown.metallurgica.foundation.data.custom.composition.Element;
import com.freezedown.metallurgica.registry.MetallurgicaRecipeTypes;
import com.simibubi.create.Create;
import com.simibubi.create.compat.jei.ConversionRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemCompositionRecipe extends ProcessingRecipe<RecipeWrapper> {
    public Item item;
    public List<Element> elements;

    static int counter = 0;

    public ItemCompositionRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(MetallurgicaRecipeTypes.item_composition, params);
    }

    public static ItemCompositionRecipe create(Item item, List<Element> elements) {
        ResourceLocation recipeId = Metallurgica.asResource("item_composition_" + counter++);
        return new ProcessingRecipeBuilder<>((params) -> new ItemCompositionRecipe(params).set(item,elements), recipeId)
                .build();
    }

    public ItemCompositionRecipe set(Item item, List<Element> elements) {
        this.item = item;
        this.elements = elements;
        return this;
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

            MutableComponent line = Component.empty();
            float percentage = (float) elementAmount / totalElementsAmount * 100;

            line.append(Component.translatable("metallurgica.element." + elementName)).append(" ");
            if (MetallurgicaConfigs.client().whatAreTheseElements.get()) {
                line.append("(").append(Component.translatable("metallurgica.element.name." + elementName.toLowerCase())).append(") ");
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
