package com.freezedown.metallurgica.compat.jei.category.composition;

import com.freezedown.metallurgica.foundation.data.custom.composition.tooltip.CompositionManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.pouffydev.krystal_core.foundation.data.lang.Components;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.common.gui.textures.Textures;
import net.minecraft.client.Minecraft;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemCompositionCategory implements IRecipeCategory<ItemCompositionRecipe> {
    public static final RecipeType<ItemCompositionRecipe> ITEM_COMPOSITION = RecipeType.create("metallurgica", "element_composition", ItemCompositionRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable slotBackground;

    public static final List<ItemCompositionRecipe> COMPOSITIONS = new ArrayList<>();

    static {
        CompositionManager.compositions.forEach((item, composition) -> {
            COMPOSITIONS.add(new ItemCompositionRecipe(composition.item(), composition.elements()));
        });
    }

    public ItemCompositionCategory(IGuiHelper guiHelper, Textures textures) {
        this.background = guiHelper.createBlankDrawable(160, 125);;
        this.icon = asDrawable(AllGuiTextures.JEI_QUESTION_MARK);
        this.slotBackground = asDrawable(AllGuiTextures.JEI_SLOT);
    }

    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(ITEM_COMPOSITION, COMPOSITIONS);
    }

    @Override
    public RecipeType<ItemCompositionRecipe> getRecipeType() {
        return ITEM_COMPOSITION;
    }

    @Override
    public Component getTitle() {
        return Components.translatable("metallurgica.jei.category.element_composition");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    public void draw(ItemCompositionRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
        int xPos = 0;
        int yPos = slotBackground.getHeight() + 4;
        Minecraft minecraft = Minecraft.getInstance();

        for(MutableComponent element : recipe.createElementLine()) {
            int yOff = yPos += 10;
            MutableComponent descriptionLine = element.copy();
            minecraft.font.draw(poseStack, Language.getInstance().getVisualOrder(descriptionLine), (float)xPos, (float)yOff, -16777216);
            Objects.requireNonNull(minecraft.font);
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ItemCompositionRecipe recipe, IFocusGroup iFocusGroup) {
        int xPos = 72;
        if (recipe.getItem() instanceof BucketItem) {
            int xOff = 18 * 4;
            IRecipeSlotBuilder fluidSlotBuilder = builder.addSlot(RecipeIngredientRole.INPUT, xPos - 9, 1).setBackground(slotBackground, -1, -1);
            IIngredientAcceptor<?> fluidOutputSlotBuilder = builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT);
            IRecipeSlotBuilder inputSlotBuilder2 = builder.addSlot(RecipeIngredientRole.INPUT, xPos + 9, 1).setBackground(slotBackground, -1, -1);
            addBucketAndFluid(recipe.getItem(), inputSlotBuilder2, fluidSlotBuilder);
            addOutput(recipe.getItem(), fluidOutputSlotBuilder);
        } else {
            IRecipeSlotBuilder inputSlotBuilder = builder.addSlot(RecipeIngredientRole.INPUT, xPos, 1).setBackground(slotBackground, -1, -1);
            IIngredientAcceptor<?> outputSlotBuilder = builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT);
            addIngredient(recipe.getItem(), inputSlotBuilder);
            addOutput(recipe.getItem(), outputSlotBuilder);
        }

    }

    private static <T> void addIngredient(Item item, IIngredientAcceptor<?> slotBuilder) {
        slotBuilder.addItemStack(item.getDefaultInstance());
    }

    private static <T> void addBucketAndFluid(Item item, IIngredientAcceptor<?> slotBuilder, IIngredientAcceptor<?> fluidSlotBuilder) {
        if (item instanceof BucketItem bucketItem) {
            slotBuilder.addItemStack(bucketItem.getDefaultInstance());
            fluidSlotBuilder.addFluidStack(bucketItem.getFluid(), 1000);
        } else {
            throw new IllegalArgumentException("Item must be a BucketItem");
        }
    }

    private static <T> void addOutput(Item item, IIngredientAcceptor<?> slotBuilder) {
        if (item instanceof BucketItem bucketItem) {
            slotBuilder.addFluidStack(bucketItem.getFluid(), 1000).addItemStack(item.getDefaultInstance());
        } else {
            slotBuilder.addItemStack(item.getDefaultInstance());
        }
    }

    protected static IDrawable asDrawable(final AllGuiTextures texture) {
        return new IDrawable() {
            public int getWidth() {
                return texture.width;
            }

            public int getHeight() {
                return texture.height;
            }

            public void draw(PoseStack poseStack, int xOffset, int yOffset) {
                texture.render(poseStack, xOffset, yOffset);
            }
        };
    }
}
