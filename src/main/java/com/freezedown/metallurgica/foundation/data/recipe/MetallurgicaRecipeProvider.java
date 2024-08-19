package com.freezedown.metallurgica.foundation.data.recipe;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.registry.TFMGFluids;
import com.drmangotea.createindustry.registry.TFMGItems;
import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.fluids.types.RiverSandFluid;
import com.freezedown.metallurgica.registry.*;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MetallurgicaRecipeProvider extends RecipeProvider {
    
    protected final List<GeneratedRecipe> all = new ArrayList<>();
    
    public MetallurgicaRecipeProvider(DataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> p_200404_1_) {
        all.forEach(c -> c.register(p_200404_1_));
        Metallurgica.LOGGER.info("{} registered {} recipe{}", getName(), all.size(), all.size() == 1 ? "" : "s");
    }
    
    protected GeneratedRecipe register(GeneratedRecipe recipe) {
        all.add(recipe);
        return recipe;
    }
    
    @FunctionalInterface
    public interface GeneratedRecipe {
        void register(Consumer<FinishedRecipe> consumer);
    }
    
    public static class Marker {
    }
    
    public static class I {
        public static ItemLike salt() {
            return MetallurgicaItems.salt.get();
        }
        public static TagKey<Item> redstone() {
            return Tags.Items.DUSTS_REDSTONE;
        }
        public static TagKey<Item> magnetite() {
            return MetallurgicaTags.forgeItemTag("raw_materials/magnetite");
        }
        public static TagKey<Item> nativeCopper() {
            return MetallurgicaTags.forgeItemTag("raw_materials/native_copper");
        }
        
        public static TagKey<Item> nativeGold() {
            return MetallurgicaTags.forgeItemTag("raw_materials/native_gold");
        }
        public static TagKey<Item> magnetiteRubble() {
            return MetallurgicaTags.forgeItemTag("material_rubble/magnetite");
        }
        public static TagKey<Item> nativeCopperRubble() {
            return MetallurgicaTags.forgeItemTag("material_rubble/native_copper");
        }
        public static TagKey<Item> bauxiteRubble() {
            return MetallurgicaTags.forgeItemTag("material_rubble/bauxite");
        }
        public static TagKey<Item> nativeGoldRubble() {
            return MetallurgicaTags.forgeItemTag("material_rubble/native_gold");
        }
        public static TagKey<Item> bauxiteStone() {
            return MetallurgicaTags.modItemTag(CreateTFMG.MOD_ID, "stone_types/bauxite");
        }
        public static TagKey<Item> magnetiteStone() {
            return MetallurgicaTags.modItemTag("stone_types/magnetite");
        }
        public static TagKey<Item> malachiteStone() {
            return MetallurgicaTags.modItemTag("stone_types/malachite");
        }
        public static ItemLike copperRubble() {
            return MetallurgicaItems.copperRubble.get();
        }
        public static ItemLike malachite() { return MetallurgicaOre.MALACHITE.MATERIAL.raw().get(); }
        public static ItemLike copperOxide() {
            return MetallurgicaItems.copperOxide.get();
        }
        public static ItemLike fireClayBall() {
            return TFMGItems.FIRECLAY_BALL.get();
        }
        public static ItemLike sulfurDust() {
            return TFMGItems.SULFUR_DUST.get();
        }
        public static ItemLike nitrateDust() {
            return TFMGItems.NITRATE_DUST.get();
        }
        public static ItemLike cokeDust() {
            return TFMGItems.COAL_COKE_DUST.get();
        }
        public static TagKey<Item> bauxite() {
            return MetallurgicaTags.forgeItemTag("raw_materials/bauxite");
        }
        public static ItemLike loosenedBauxite() {
            return MetallurgicaItems.loosenedBauxite.get();
        }
        public static ItemLike washedAlumina() {
            return MetallurgicaItems.washedAlumina.get();
        }
        public static ItemLike alumina() {
            return MetallurgicaItems.alumina.get();
        }
        public static ItemLike aluminumDust() {
            return MetallurgicaItems.aluminumDust.get();
        }
        public static ItemLike redSand() {
            return Items.RED_SAND;
        }
        public static ItemLike magnetiteLumps() {
            return MetallurgicaItems.magnetiteLumps.get();
        }
        public static ItemLike richMagnetite() { return MetallurgicaOre.MALACHITE.MATERIAL.rich().get(); }
        public static ItemLike goldNugget() {
            return Items.GOLD_NUGGET;
        }
        public static ItemLike sand() {
            return Items.SAND;
        }
    }
    
    public static class F {
        public static Fluid decontaminatedWater() {
            return MetallurgicaFluids.decontaminatedWater.get();
        }
        public static Fluid preheatedAir() {
            return MetallurgicaFluids.preheatedAir.get();
        }
        public static Fluid nitrogen() {
            return MetallurgicaFluids.nitrogen.get();
        }
        public static Fluid bfg() {
            return MetallurgicaFluids.bfg.get();
        }
        public static Fluid riverSand() {
            return MetallurgicaFluids.riverSand.get();
        }
        public static Fluid cryolite() {
            return MetallurgicaFluids.cryolite.get();
        }
        public static Fluid hydrochloricAcid() {
            return MetallurgicaFluids.hydrochloricAcid.get();
        }
        public static Fluid sulfuricAcid() {
            return MetallurgicaFluids.sulfuricAcid.get();
        }
        public static Fluid sodiumHydroxide() {
            return MetallurgicaFluids.sodiumHydroxide.get();
        }
        public static Fluid sodiumHypochlorite() {
            return MetallurgicaFluids.sodiumHypochlorite.get();
        }
        public static Fluid chlorine() {
            return MetallurgicaFluids.chlorine.get();
        }
        public static Fluid slag() {
            return TFMGFluids.MOLTEN_SLAG.get();
        }
        public static Fluid carbonDioxide() {
            return TFMGFluids.CARBON_DIOXIDE.get();
        }
        public static Fluid magnetiteFines() {
            return MetallurgicaFluids.magnetiteFines.get();
        }
        
        public static Fluid water() {
            return Fluids.WATER;
        }
        
        public static Fluid moltenIron() {
            return MetallurgicaMetals.IRON.METAL.getMolten().get();
        }
        public static Fluid moltenCopper() {
            return MetallurgicaMetals.COPPER.METAL.getMolten().get();
        }
        
        public static FluidStack riverSandStack(String mineral, int amount) {
            return RiverSandFluid.of(amount, mineral);
        }
        
        public static FluidIngredient riverSand(String mineral, int amount) {
            return FluidIngredient.fromFluidStack(riverSandStack(mineral, amount));
        }
    }
}
