package com.freezedown.metallurgica.foundation.data.recipe;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.datagen.recipes.builder.VatMachineRecipeBuilder;
import com.drmangotea.tfmg.recipes.VatMachineRecipe;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.fluids.types.RiverSandFluid;
import com.freezedown.metallurgica.content.machines.vat.floatation_cell.FloatationCatalyst;
import com.freezedown.metallurgica.content.machines.vat.floatation_cell.FloatationCatalystBuilder;
import com.freezedown.metallurgica.infastructure.material.registry.flags.FlagKey;
import com.freezedown.metallurgica.infastructure.material.MaterialHelper;
import com.freezedown.metallurgica.registry.*;
import com.freezedown.metallurgica.registry.material.MetMaterials;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class MetallurgicaRecipeProvider extends RecipeProvider {
    
    protected final List<GeneratedRecipe> all = new ArrayList<>();
    
    public MetallurgicaRecipeProvider(DataGenerator generator) {
        super(generator.getPackOutput());
    }
    
    protected GeneratedRecipe register(GeneratedRecipe recipe) {
        all.add(recipe);
        return recipe;
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        all.forEach(c -> c.register(consumer));
        Metallurgica.LOGGER.info("{} registered {} recipe{}", getName(), all.size(), all.size() == 1 ? "" : "s");
    }

    @FunctionalInterface
    public interface GeneratedRecipe {
        void register(Consumer<FinishedRecipe> consumer);
    }

    public GeneratedRecipe createVatRecipe(String namespace, Supplier<ItemLike> singleIngredient, UnaryOperator<VatMachineRecipeBuilder> transform, VatMachineRecipeBuilder.VatRecipeParams params) {
        ProcessingRecipeSerializer<VatMachineRecipe> serializer = TFMGRecipeTypes.VAT_MACHINE_RECIPE.getSerializer();
        GeneratedRecipe generatedRecipe = (c) -> {
            ItemLike itemLike = singleIngredient.get();
            transform.apply((VatMachineRecipeBuilder)(new VatMachineRecipeBuilder(serializer.getFactory(), params, new ResourceLocation(namespace, CatnipServices.REGISTRIES.getKeyOrThrow(itemLike.asItem()).getPath()))).withItemIngredients(new Ingredient[]{Ingredient.of(new ItemLike[]{itemLike})})).build(c);
        };
        this.all.add(generatedRecipe);
        return generatedRecipe;
    }

    public GeneratedRecipe createVatRecipe(Supplier<ItemLike> singleIngredient, UnaryOperator<VatMachineRecipeBuilder> transform, VatMachineRecipeBuilder.VatRecipeParams params) {
        return this.createVatRecipe("metallurgica", singleIngredient, transform, params);
    }

    protected <T extends ProcessingRecipe<?>> GeneratedRecipe createVatRecipeWithDeferredId(Supplier<ResourceLocation> name, UnaryOperator<VatMachineRecipeBuilder> transform, VatMachineRecipeBuilder.VatRecipeParams params) {
        ProcessingRecipeSerializer<VatMachineRecipe> serializer = TFMGRecipeTypes.VAT_MACHINE_RECIPE.getSerializer();
        GeneratedRecipe generatedRecipe = (c) -> transform.apply(new VatMachineRecipeBuilder(serializer.getFactory(), params, name.get())).build(c);
        this.all.add(generatedRecipe);
        return generatedRecipe;
    }

    public GeneratedRecipe createVatRecipe(ResourceLocation name, UnaryOperator<VatMachineRecipeBuilder> transform, VatMachineRecipeBuilder.VatRecipeParams params) {
        return this.createVatRecipeWithDeferredId(() -> name, transform, params);
    }

    public GeneratedRecipe createVatRecipe(String name, UnaryOperator<VatMachineRecipeBuilder> transform, VatMachineRecipeBuilder.VatRecipeParams params) {
        return this.createVatRecipe(Metallurgica.asResource(name), transform, params);
    }

    public GeneratedRecipe createFloatationCatalyst(String namespace, Supplier<ItemLike> singleIngredient, UnaryOperator<FloatationCatalystBuilder> transform, FloatationCatalystBuilder.FloatationCatalystParams params) {
        ProcessingRecipeSerializer<FloatationCatalyst> serializer = MetallurgicaRecipeTypes.floatation_catalyst.getSerializer();
        GeneratedRecipe generatedRecipe = (c) -> {
            ItemLike itemLike = singleIngredient.get();
            transform.apply((FloatationCatalystBuilder)(new FloatationCatalystBuilder(serializer.getFactory(), params, new ResourceLocation(namespace, CatnipServices.REGISTRIES.getKeyOrThrow(itemLike.asItem()).getPath()))).withItemIngredients(new Ingredient[]{Ingredient.of(new ItemLike[]{itemLike})})).build(c);
        };
        this.all.add(generatedRecipe);
        return generatedRecipe;
    }

    public GeneratedRecipe createFloatationCatalyst(Supplier<ItemLike> singleIngredient, UnaryOperator<FloatationCatalystBuilder> transform, FloatationCatalystBuilder.FloatationCatalystParams params) {
        return this.createFloatationCatalyst("metallurgica", singleIngredient, transform, params);
    }

    protected <T extends ProcessingRecipe<?>> GeneratedRecipe createFloatationCatalystWithDeferredId(Supplier<ResourceLocation> name, UnaryOperator<FloatationCatalystBuilder> transform, FloatationCatalystBuilder.FloatationCatalystParams params) {
        ProcessingRecipeSerializer<FloatationCatalyst> serializer = MetallurgicaRecipeTypes.floatation_catalyst.getSerializer();
        GeneratedRecipe generatedRecipe = (c) -> transform.apply(new FloatationCatalystBuilder(serializer.getFactory(), params, name.get())).build(c);
        this.all.add(generatedRecipe);
        return generatedRecipe;
    }

    public GeneratedRecipe createFloatationCatalyst(ResourceLocation name, UnaryOperator<FloatationCatalystBuilder> transform, FloatationCatalystBuilder.FloatationCatalystParams params) {
        return this.createFloatationCatalystWithDeferredId(() -> name, transform, params);
    }

    public GeneratedRecipe createFloatationCatalyst(String name, UnaryOperator<FloatationCatalystBuilder> transform, FloatationCatalystBuilder.FloatationCatalystParams params) {
        return this.createFloatationCatalyst(Metallurgica.asResource(name), transform, params);
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
            return MetallurgicaTags.modItemTag(TFMG.MOD_ID, "stone_types/bauxite");
        }
        public static TagKey<Item> magnetiteStone() {
            return MetallurgicaTags.modItemTag("stone_types/magnetite");
        }
        public static TagKey<Item> malachiteStone() {
            return MetallurgicaTags.modItemTag("stone_types/malachite");
        }
        //public static ItemLike malachite() { return MetallurgicaOre.MALACHITE.ORE.raw().get(); }
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
        public static ItemLike redSand() {
            return Items.RED_SAND;
        }
        public static ItemLike magnetiteLumps() {
            return MetallurgicaItems.magnetiteLumps.get();
        }
        //public static ItemLike richMagnetite() { return MetallurgicaOre.MALACHITE.ORE.rich().get(); }
        public static ItemLike goldNugget() {
            return Items.GOLD_NUGGET;
        }
        public static ItemLike sand() {
            return Items.SAND;
        }
        public static ItemLike ceramicClay() {return MetallurgicaItems.ceramicClay.get();}
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
            return MaterialHelper.getFluid(MetMaterials.IRON.get(), FlagKey.MOLTEN);
        }
        public static Fluid moltenCopper() {
            return MaterialHelper.getFluid(MetMaterials.COPPER.get(), FlagKey.MOLTEN);
        }
        
        public static FluidStack riverSandStack(String mineral, int amount) {
            return RiverSandFluid.of(amount, mineral);
        }
        
        public static FluidIngredient riverSand(String mineral, int amount) {
            return FluidIngredient.fromFluidStack(riverSandStack(mineral, amount));
        }
    }
}
