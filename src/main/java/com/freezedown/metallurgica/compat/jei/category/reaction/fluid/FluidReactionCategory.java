package com.freezedown.metallurgica.compat.jei.category.reaction.fluid;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.compat.jei.MetallurgicaJeiRecipeTypes;
import com.freezedown.metallurgica.content.items.reaction.FluidReactionRecipe;
import com.freezedown.metallurgica.registry.misc.MetallurgicaSpecialRecipes;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import static com.simibubi.create.compat.jei.category.CreateRecipeCategory.addStochasticTooltip;
import static com.simibubi.create.compat.jei.category.CreateRecipeCategory.getRenderedSlot;

public class FluidReactionCategory implements IRecipeCategory<FluidReactionRecipe> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Metallurgica.ID, "textures/gui/jei.png");

    private static final int WIDTH = 120;
    private static final int HEIGHT = 60;
    private static final int INPUT_X = 5;
    private static final int INPUT_Y = 22;
    private static final int ARROW_WIDTH = 22;
    private static final int ARROW_HEIGHT = 15;
    private static final int ARROW1_X = INPUT_X + 18 + 4;
    private static final int CATALYST_X = ARROW1_X + ARROW_WIDTH + 4;
    private static final int ARROW2_X = CATALYST_X + 18 + 4;
    private static final int OUTPUT_X = ARROW2_X + ARROW_WIDTH + 4;

    private final IGuiHelper guiHelper;
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable arrow;
    private final Component title;

    public FluidReactionCategory(final IGuiHelper guiHelper) {
        this.title = Component.translatable("jei." + MetallurgicaJeiRecipeTypes.FLUID_REACTION.getUid().getNamespace() + "." + MetallurgicaJeiRecipeTypes.FLUID_REACTION.getUid().getPath());
        this.guiHelper = guiHelper;
        this.background = guiHelper.createBlankDrawable(WIDTH, HEIGHT);
        this.icon = guiHelper.createDrawableIngredient(ForgeTypes.FLUID_STACK, new FluidStack(Fluids.FLOWING_WATER, 500));
        this.arrow = guiHelper.createDrawable(TEXTURE, 0, 0, ARROW_WIDTH, ARROW_HEIGHT);
    }

    @Override
    public RecipeType<FluidReactionRecipe> getRecipeType() {
        return MetallurgicaJeiRecipeTypes.FLUID_REACTION;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FluidReactionRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, INPUT_X, INPUT_Y)
                .addIngredients(recipe.getInput());
        final IRecipeSlotBuilder fluidSlotBuilder = builder.addSlot(RecipeIngredientRole.CATALYST, CATALYST_X, INPUT_Y);
        final String sourceKey = "jei.metallurgica.reaction.catalyst.tooltip." + (recipe.isSource() ? "source" : "flowing_or_source");
        final String removeKey = (recipe.isRemove() ? "jei.metallurgica.reaction.catalyst.tooltip.remove" : "");
        fluidSlotBuilder.addRichTooltipCallback((view, tooltip) -> {
            tooltip.add(Component.translatable(sourceKey).withStyle(ChatFormatting.ITALIC, ChatFormatting.WHITE));
            if (!removeKey.isEmpty()) {
                tooltip.add(Component.translatable(removeKey).withStyle(ChatFormatting.YELLOW));
            }
        });
        final int amount = recipe.isSource() ? 1000 : 500;
        recipe.getFluid().getMatchingFluidStacks().stream().filter(fluid -> !recipe.isSource()).forEach(fluid -> fluidSlotBuilder.addIngredient(ForgeTypes.FLUID_STACK, new FluidStack(fluid, amount)));
        builder
                .addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X, INPUT_Y)
                .setBackground(getRenderedSlot(recipe.getResult()), -1, -1)
                .addItemStack(recipe.getResult().getStack())
                .addRichTooltipCallback(addStochasticTooltip(recipe.getResult()));
    }

    @Override
    public void draw(FluidReactionRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        arrow.draw(guiGraphics, ARROW1_X, INPUT_Y);
        arrow.draw(guiGraphics, ARROW2_X, INPUT_Y);
    }
}
