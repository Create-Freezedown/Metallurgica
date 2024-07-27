package com.freezedown.metallurgica.content.forging.advanced_casting;

import com.freezedown.metallurgica.registry.MetallurgicaRecipeTypes;
import com.google.gson.JsonObject;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class AdvancedCastingRecipe extends ProcessingRecipe<RecipeWrapper> {
    private String castingMoldType;
    private int coolingTime;
    public AdvancedCastingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(MetallurgicaRecipeTypes.advanced_casting, params);
    }
    protected int getMaxInputCount() {
        return 0;
    }
    
    protected int getMaxOutputCount() {
        return 3;
    }
    
    protected int getMaxFluidOutputCount() {
        return 1;
    }
    
    public boolean matches(CombinedTankWrapper inv, Level worldIn) {
        return inv.getFluidInTank(0).getAmount() != 0 && this.fluidIngredients.get(0).test(inv.getFluidInTank(0));
    }
    public FluidIngredient getInputFluid() {
        return this.getFluidIngredients().get(0);
    }
    public boolean matches(RecipeWrapper pContainer, Level pLevel) {
        return false;
    }
    
    public int getCoolingTime() {
        return this.coolingTime;
    }
    
    public String getCastingMoldType() {
        return this.castingMoldType;
    }
    
    public ItemStack getRecipeOutput() {
        return this.getResultItem();
    }
    @Override
    public void readAdditional(JsonObject json) {
        super.readAdditional(json);
        castingMoldType = GsonHelper.getAsString(json, "castingMoldType", "blank");
        coolingTime = GsonHelper.getAsInt(json, "coolingTime", 10);
    }
    
    @Override
    public void writeAdditional(JsonObject json) {
        super.writeAdditional(json);
        json.addProperty("castingMoldType", castingMoldType);
        json.addProperty("coolingTime", coolingTime);
    }
    
    @Override
    public void readAdditional(FriendlyByteBuf buffer) {
        super.readAdditional(buffer);
        castingMoldType = buffer.readUtf();
        coolingTime = buffer.readInt();
    }
    
    @Override
    public void writeAdditional(FriendlyByteBuf buffer) {
        super.writeAdditional(buffer);
        buffer.writeUtf(castingMoldType);
        buffer.writeInt(coolingTime);
    }
}
