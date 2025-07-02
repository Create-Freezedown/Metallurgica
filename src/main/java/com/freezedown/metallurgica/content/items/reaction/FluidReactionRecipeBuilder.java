package com.freezedown.metallurgica.content.items.reaction;

import com.freezedown.metallurgica.registry.misc.MetallurgicaSpecialRecipes;
import com.google.gson.JsonObject;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class FluidReactionRecipeBuilder implements RecipeBuilder {
    private final Ingredient input;
    private FluidIngredient fluidInput;
    private final ProcessingOutput result;
    private boolean source;
    private boolean remove;
    @javax.annotation.Nullable
    private String group;

    public FluidReactionRecipeBuilder(ItemLike input, ProcessingOutput result) {
        this.input = Ingredient.of(input);
        this.result = result;
    }

    public FluidReactionRecipeBuilder fluid(FluidIngredient fluidInput) {
        this.fluidInput = fluidInput;
        return this;
    }

    public FluidReactionRecipeBuilder requireSource() {
        this.source = true;
        return this;
    }

    public FluidReactionRecipeBuilder removeFluid() {
        this.remove = true;
        return this;
    }

    @Override
    public RecipeBuilder unlockedBy(String s, CriterionTriggerInstance criterionTriggerInstance) {
        return null;
    }

    @Override
    public FluidReactionRecipeBuilder group(@Nullable String groupName) {
        this.group = groupName;
        return this;
    }

    @Override
    public Item getResult() {
        return this.result.getStack().getItem();
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation recipeId) {
        consumer.accept(new Result(recipeId, group, input, fluidInput, result, source, remove));
    }

    static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final String group;
        private final Ingredient input;
        private final FluidIngredient fluidInput;
        private final ProcessingOutput result;
        private final boolean source;
        private final boolean remove;

        private static final String KEY_INGREDIENT = "ingredient";
        private static final String KEY_RESULT = "result";
        private static final String KEY_FLUID = "fluid";
        private static final String KEY_SOURCE = "source";
        private static final String KEY_REMOVE = "remove";

        public Result(ResourceLocation id, String group, Ingredient input, FluidIngredient fluidInput, ProcessingOutput result, boolean source, boolean remove) {
            this.id = id;
            this.group = group;
            this.input = input;
            this.fluidInput = fluidInput;
            this.result = result;
            this.source = source;
            this.remove = remove;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }
            json.add(KEY_INGREDIENT, input.toJson());
            json.add(KEY_FLUID, fluidInput.serialize());
            json.add(KEY_RESULT, result.serialize());
            json.addProperty(KEY_SOURCE, source);
            json.addProperty(KEY_REMOVE, remove);
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return MetallurgicaSpecialRecipes.FLUID_REACTION_SERIALIZER.get();
        }

        @Override
        public @Nullable JsonObject serializeAdvancement() {
            return null;
        }

        @Override
        public @Nullable ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
