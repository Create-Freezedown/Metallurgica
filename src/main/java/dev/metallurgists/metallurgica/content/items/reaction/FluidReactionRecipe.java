package dev.metallurgists.metallurgica.content.items.reaction;

import com.google.gson.JsonObject;
import com.mojang.realmsclient.util.JsonUtils;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

public class FluidReactionRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    @Getter
    private final Ingredient input;
    @Getter
    private final FluidIngredient fluid;
    @Getter
    private final ProcessingOutput result;
    @Getter
    private final boolean source;
    @Getter
    private final boolean remove;

    public FluidReactionRecipe(final ResourceLocation id, final ProcessingOutput result, final Ingredient input, final FluidIngredient fluid, final boolean source, final boolean remove) {
        this.id = id;
        this.result = result;
        this.input = input;
        this.fluid = fluid;
        this.source = source;
        this.remove = remove;
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return false;
    }

    public boolean matches(Container container, FluidState fluid) {
        // validate container size
        if(container.getContainerSize() < 1) {
            return false;
        }
        // check first item for match
        if(!input.test(container.getItem(0))) {
            return false;
        }
        // check fluid for match
        if(!getFluid().test(new FluidStack(fluid.getType(), 1)) || (isSource() && !fluid.isSource())) {
            return false;
        }
        // all checks passed
        return true;
    }

    public boolean matches(Container container, FluidStack fluid) {
        // validate container size
        if(container.getContainerSize() < 1) {
            return false;
        }
        // check first item for match
        if(!input.test(container.getItem(0))) {
            return false;
        }
        // check fluid for match
        if(!getFluid().test(fluid) || (isSource() && fluid.getAmount() <= FluidType.BUCKET_VOLUME)) {
            return false;
        }
        // all checks passed
        return true;
    }

                           @Override
    public ItemStack assemble(Container pContainer, RegistryAccess registryAccess) {
        pContainer.removeItem(0, 1);
        return getResultItem(registryAccess).copy();
    }

    public ItemStack assemble(Container container, BlockPos pos, Level level) {
        if(remove) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
        }
        return assemble(container, level.registryAccess());
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return result.getStack();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return null;
    }

    public static class Serializer implements RecipeSerializer<FluidReactionRecipe> {
        public static final String CATEGORY = "fluid_reaction";

        private static final String KEY_INGREDIENT = "ingredient";
        private static final String KEY_RESULT = "result";
        private static final String KEY_FLUID = "fluid";
        private static final String KEY_SOURCE = "source";
        private static final String KEY_REMOVE = "remove";

        @Override
        public FluidReactionRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            final Ingredient ingredient = CraftingHelper.getIngredient(jsonObject.get(KEY_INGREDIENT), false);
            final ProcessingOutput result = ProcessingOutput.deserialize(jsonObject.get(KEY_RESULT));
            final FluidIngredient fluidIngredient = FluidIngredient.deserialize(jsonObject.get(KEY_FLUID));
            final boolean source = JsonUtils.getBooleanOr(KEY_SOURCE, jsonObject, false);
            final boolean remove = JsonUtils.getBooleanOr(KEY_REMOVE, jsonObject, false);
            return new FluidReactionRecipe(resourceLocation, result, ingredient, fluidIngredient, source, remove);
        }

        @Override
        public @Nullable FluidReactionRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf buf) {
            final ResourceLocation id = buf.readResourceLocation();
            final ProcessingOutput result = ProcessingOutput.read(buf);
            final Ingredient ingredient = Ingredient.fromNetwork(buf);
            final FluidIngredient fluid = FluidIngredient.read(buf);
            final boolean source = buf.readBoolean();
            final boolean remove = buf.readBoolean();
            return new FluidReactionRecipe(id, result, ingredient, fluid, source, remove);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, FluidReactionRecipe recipe) {
            buf.writeResourceLocation(recipe.getId());
            recipe.getResult().write(buf);
            recipe.getInput().toNetwork(buf);
            recipe.getFluid().write(buf);
            buf.writeBoolean(recipe.isSource());
            buf.writeBoolean(recipe.isRemove());
        }
    }
}
