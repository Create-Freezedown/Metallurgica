package com.freezedown.metallurgica.registry;

import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.data.custom.composition.tooltip.CompositionBuilder;
import com.freezedown.metallurgica.foundation.data.custom.composition.Element;
import com.freezedown.metallurgica.foundation.data.custom.composition.FinishedComposition;
import com.freezedown.metallurgica.foundation.data.custom.composition.fluid.FluidCompositionBuilder;
import com.freezedown.metallurgica.foundation.material.MaterialHelper;
import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import com.simibubi.create.AllItems;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey.INGOT;
import static com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey.SHEET;
import static com.freezedown.metallurgica.registry.misc.MetallurgicaMaterials.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MetallurgicaCompositions implements DataProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    protected final PackOutput.PathProvider compositionPathProvider;
    protected final PackOutput.PathProvider fluidCompositionPathProvider;
    
    public MetallurgicaCompositions(DataGenerator pGenerator) {
        this.compositionPathProvider = pGenerator.getPackOutput().createPathProvider(PackOutput.Target.DATA_PACK, "metallurgica_utilities/compositions");
        this.fluidCompositionPathProvider = pGenerator.getPackOutput().createPathProvider(PackOutput.Target.DATA_PACK, "metallurgica_utilities/fluid_compositions");
    }

    public static void register(DataGenerator gen) {
        MetallurgicaCompositions metallurgicaCompositions = new MetallurgicaCompositions(gen);
        gen.addProvider(true, new DataProvider() {
            
            @Override
            public CompletableFuture<?> run(CachedOutput cachedOutput) {
                return metallurgicaCompositions.run(cachedOutput);
            }
            
            @Override
            public String getName() {
                return "Metallurgica's Composition Provider";
            }
        });
    }
    
    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        Set<ResourceLocation> set = Sets.newHashSet();
        Set<ResourceLocation> fluidSet = Sets.newHashSet();
        List<CompletableFuture<?>> list = new ArrayList();
        this.buildCompositions((pFinishedComposition) -> {
            if (!set.add(pFinishedComposition.getId())) {
                throw new IllegalStateException("Duplicate composition " + pFinishedComposition.getId());
            } else {
                list.add(DataProvider.saveStable(cachedOutput, pFinishedComposition.serializeComposition(), this.compositionPathProvider.json(pFinishedComposition.getId())));
            }
        });
        this.buildFluidCompositions((pFinishedComposition) -> {
            if (!fluidSet.add(pFinishedComposition.getId())) {
                throw new IllegalStateException("Duplicate composition " + pFinishedComposition.getId());
            } else {
                list.add(DataProvider.saveStable(cachedOutput, pFinishedComposition.serializeComposition(), this.fluidCompositionPathProvider.json(pFinishedComposition.getId())));
            }
        });
        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }
    
    
    protected void buildCompositions(Consumer<FinishedComposition> pFinishedCompositionConsumer) {
        metallurgicaCompositions(pFinishedCompositionConsumer);
        materialCompositions(pFinishedCompositionConsumer);
        compatCompositions(pFinishedCompositionConsumer);
        bucketCompositions(pFinishedCompositionConsumer);
    }
    
    protected void buildFluidCompositions(Consumer<FinishedComposition> pFinishedCompositionConsumer) {
        acidCompositions(pFinishedCompositionConsumer);
        genericFluidCompositions(pFinishedCompositionConsumer);
    }
    
    protected void acidCompositions(Consumer<FinishedComposition> pFinishedCompositionConsumer) {
        createFluidComposition(pFinishedCompositionConsumer, getFluidStack(MetallurgicaFluids.hydrochloricAcid.get().getSource()), Element.createComposition(MetallurgicaElements.HYDROGEN.ELEMENT, MetallurgicaElements.CHLORINE.ELEMENT));
        createFluidComposition(pFinishedCompositionConsumer, getFluidStack(MetallurgicaFluids.sulfuricAcid.get().getSource()), Element.createComposition(MetallurgicaElements.HYDROGEN.ELEMENT.withAmount(2), MetallurgicaElements.SULFUR.ELEMENT.withAmount(1), MetallurgicaElements.OXYGEN.ELEMENT.withAmount(4)));
        createFluidComposition(pFinishedCompositionConsumer, getFluidStack(MetallurgicaFluids.sodiumHydroxide.get().getSource()), Element.createComposition(MetallurgicaElements.SODIUM.ELEMENT, MetallurgicaElements.OXYGEN.ELEMENT.withAmount(1), MetallurgicaElements.HYDROGEN.ELEMENT.withAmount(1)));
        createFluidComposition(pFinishedCompositionConsumer, getFluidStack(MetallurgicaFluids.sodiumHypochlorite.get().getSource()), Element.createComposition(MetallurgicaElements.SODIUM.ELEMENT, MetallurgicaElements.OXYGEN.ELEMENT.withAmount(1), MetallurgicaElements.CHLORINE.ELEMENT));
    }
    
    protected void genericFluidCompositions(Consumer<FinishedComposition> pFinishedCompositionConsumer) {
        createFluidComposition(pFinishedCompositionConsumer, getFluidStack(MetallurgicaFluids.chlorine.get().getSource()), Element.createComposition(MetallurgicaElements.CHLORINE.ELEMENT));
        createFluidComposition(pFinishedCompositionConsumer, getFluidStack(MetallurgicaFluids.crudeTitaniumTetrachloride.get().getSource()), Element.createComposition(
                MetallurgicaElements.IRON.ELEMENT.withBrackets(), MetallurgicaElements.CHLORINE.ELEMENT.withForceClosedBracket().withGroupedAmount(6),
                MetallurgicaElements.TITANIUM.ELEMENT.withBrackets(), MetallurgicaElements.CHLORINE.ELEMENT.withAmount(4).withForceClosedBracket().withGroupedAmount(24),
                MetallurgicaElements.TIN.ELEMENT.withBrackets(), MetallurgicaElements.CHLORINE.ELEMENT.withAmount(4).withForceClosedBracket().withGroupedAmount(9),
                MetallurgicaElements.SILICON.ELEMENT.withBrackets(), MetallurgicaElements.CHLORINE.ELEMENT.withAmount(4).withForceClosedBracket().withGroupedAmount(3)
        ));
        
        createFluidComposition(pFinishedCompositionConsumer, getFluidStack(MetallurgicaFluids.titaniumTetrachloride.get().getSource()), Element.createComposition(MetallurgicaElements.TITANIUM.ELEMENT, MetallurgicaElements.CHLORINE.ELEMENT.withAmount(4)));
        createFluidComposition(pFinishedCompositionConsumer, getFluidStack(MetallurgicaFluids.siliconTetrachloride.get().getSource()), Element.createComposition(MetallurgicaElements.SILICON.ELEMENT, MetallurgicaElements.CHLORINE.ELEMENT.withAmount(4)));
        createFluidComposition(pFinishedCompositionConsumer, getFluidStack(MetallurgicaFluids.tinTetrachloride.get().getSource()), Element.createComposition(MetallurgicaElements.TIN.ELEMENT, MetallurgicaElements.CHLORINE.ELEMENT.withAmount(4)));
        
        createFluidComposition(pFinishedCompositionConsumer, getFluidStack(MetallurgicaFluids.ironChloride.get().getSource()), Element.createComposition(MetallurgicaElements.IRON.ELEMENT, MetallurgicaElements.CHLORINE.ELEMENT));
        createFluidComposition(pFinishedCompositionConsumer, getFluidStack(MetallurgicaFluids.magnesiumChloride.get().getSource()), Element.createComposition(MetallurgicaElements.MAGNESIUM.ELEMENT, MetallurgicaElements.CHLORINE.ELEMENT));
    }
    
    protected void bucketCompositions(Consumer<FinishedComposition> pFinishedCompositionConsumer) {
        createComposition(pFinishedCompositionConsumer, getBucket("chlorine"), Element.createComposition(MetallurgicaElements.CHLORINE.ELEMENT));
        
        //Acids
        createComposition(pFinishedCompositionConsumer, getBucket("hydrochloric_acid"), Element.createComposition(MetallurgicaElements.HYDROGEN.ELEMENT, MetallurgicaElements.CHLORINE.ELEMENT));
        createComposition(pFinishedCompositionConsumer, getBucket("sulfuric_acid"), Element.createComposition(MetallurgicaElements.HYDROGEN.ELEMENT.withAmount(2), MetallurgicaElements.SULFUR.ELEMENT.withAmount(1), MetallurgicaElements.OXYGEN.ELEMENT.withAmount(4)));
        createComposition(pFinishedCompositionConsumer, getBucket("sodium_hydroxide"), Element.createComposition(MetallurgicaElements.SODIUM.ELEMENT, MetallurgicaElements.OXYGEN.ELEMENT.withAmount(1), MetallurgicaElements.HYDROGEN.ELEMENT.withAmount(1)));
        createComposition(pFinishedCompositionConsumer, getBucket("sodium_hypochlorite"), Element.createComposition(MetallurgicaElements.SODIUM.ELEMENT, MetallurgicaElements.OXYGEN.ELEMENT.withAmount(1), MetallurgicaElements.CHLORINE.ELEMENT));
        
        //Chlorides
        createComposition(pFinishedCompositionConsumer, getBucket("crude_titanium_tetrachloride"), Element.createComposition(
                MetallurgicaElements.IRON.ELEMENT.withBrackets(), MetallurgicaElements.CHLORINE.ELEMENT.withForceClosedBracket().withGroupedAmount(6),
                MetallurgicaElements.TITANIUM.ELEMENT.withBrackets(), MetallurgicaElements.CHLORINE.ELEMENT.withAmount(4).withForceClosedBracket().withGroupedAmount(24),
                MetallurgicaElements.TIN.ELEMENT.withBrackets(), MetallurgicaElements.CHLORINE.ELEMENT.withAmount(4).withForceClosedBracket().withGroupedAmount(9),
                MetallurgicaElements.SILICON.ELEMENT.withBrackets(), MetallurgicaElements.CHLORINE.ELEMENT.withAmount(4).withForceClosedBracket().withGroupedAmount(3)
        ));
        
        createComposition(pFinishedCompositionConsumer, getBucket("titanium_tetrachloride"), Element.createComposition(MetallurgicaElements.TITANIUM.ELEMENT, MetallurgicaElements.CHLORINE.ELEMENT.withAmount(4)));
        createComposition(pFinishedCompositionConsumer, getBucket("silicon_tetrachloride"), Element.createComposition(MetallurgicaElements.SILICON.ELEMENT, MetallurgicaElements.CHLORINE.ELEMENT.withAmount(4)));
        createComposition(pFinishedCompositionConsumer, getBucket("tin_tetrachloride"), Element.createComposition(MetallurgicaElements.TIN.ELEMENT, MetallurgicaElements.CHLORINE.ELEMENT.withAmount(4)));
        
        createComposition(pFinishedCompositionConsumer, getBucket("iron_chloride"), Element.createComposition(MetallurgicaElements.IRON.ELEMENT, MetallurgicaElements.CHLORINE.ELEMENT));
        createComposition(pFinishedCompositionConsumer, getBucket("magnesium_chloride"), Element.createComposition(MetallurgicaElements.MAGNESIUM.ELEMENT, MetallurgicaElements.CHLORINE.ELEMENT));
    }
    
    protected void metallurgicaCompositions(Consumer<FinishedComposition> pFinishedCompositionConsumer) {
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.salt.get(), Element.createComposition(MetallurgicaElements.SODIUM.ELEMENT, MetallurgicaElements.CHLORINE.ELEMENT));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.magnesiumOxide.get(), Element.createComposition(MetallurgicaElements.MAGNESIUM.ELEMENT, MetallurgicaElements.OXYGEN.ELEMENT));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.magnesiumChloride.get(), Element.createComposition(MetallurgicaElements.MAGNESIUM.ELEMENT, MetallurgicaElements.CHLORINE.ELEMENT));

        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.calciumPowder.get(), Element.createComposition(MetallurgicaElements.CALCIUM.ELEMENT));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.sodiumCarbonate.get(), Element.createComposition(MetallurgicaElements.SODIUM.ELEMENT.withAmount(2),MetallurgicaElements.CARBON.ELEMENT,MetallurgicaElements.OXYGEN.ELEMENT.withAmount(3)));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.ammoniumChloride.get(), Element.createComposition(MetallurgicaElements.NITROGEN.ELEMENT,MetallurgicaElements.HYDROGEN.ELEMENT.withAmount(4),MetallurgicaElements.CHLORINE.ELEMENT));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.vanadiumPentoxide.get(), Element.createComposition(MetallurgicaElements.VANADIUM.ELEMENT.withAmount(2),MetallurgicaElements.OXYGEN.ELEMENT.withAmount(5)));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.ammoniumMetavanadate.get(), Element.createComposition(MetallurgicaElements.NITROGEN.ELEMENT,MetallurgicaElements.HYDROGEN.ELEMENT.withAmount(4),MetallurgicaElements.VANADIUM.ELEMENT,MetallurgicaElements.OXYGEN.ELEMENT.withAmount(3)));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.sodiumOrthovanadate.get(), Element.createComposition(MetallurgicaElements.SODIUM.ELEMENT.withAmount(3),MetallurgicaElements.VANADIUM.ELEMENT,MetallurgicaElements.OXYGEN.ELEMENT.withAmount(4)));


        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.aluminumNugget.get(), Element.createComposition(MetallurgicaElements.ALUMINUM.ELEMENT));
        createComposition(pFinishedCompositionConsumer, MaterialHelper.get(ALUMINUM.getMaterial(), SHEET), Element.createComposition(MetallurgicaElements.COPPER.ELEMENT));
        
        createComposition(pFinishedCompositionConsumer, MaterialHelper.get(BRONZE.getMaterial(), INGOT), Element.createComposition(MetallurgicaElements.COPPER.ELEMENT.withAmount(7), MetallurgicaElements.TIN.ELEMENT.withAmount(2)));
        //createComposition(pFinishedCompositionConsumer, MetallurgicaItems.bronzeNugget.get(), Element.createComposition(MetallurgicaElements.COPPER.ELEMENT.withAmount(7), MetallurgicaElements.TIN.ELEMENT.withAmount(2)));
        createComposition(pFinishedCompositionConsumer, MaterialHelper.get(BRONZE.getMaterial(), SHEET), Element.createComposition(MetallurgicaElements.COPPER.ELEMENT.withAmount(7), MetallurgicaElements.TIN.ELEMENT.withAmount(2)));
        //createComposition(pFinishedCompositionConsumer, MetallurgicaItems.bronzeDust.get(), Element.createComposition(MetallurgicaElements.COPPER.ELEMENT.withAmount(7), MetallurgicaElements.TIN.ELEMENT.withAmount(2)));
        
        createComposition(pFinishedCompositionConsumer, MaterialHelper.get(ARSENICAL_BRONZE.getMaterial(), INGOT), Element.createComposition(MetallurgicaElements.COPPER.ELEMENT.withAmount(4), MetallurgicaElements.TIN.ELEMENT.withAmount(1), MetallurgicaElements.ARSENIC.ELEMENT.withAmount(3)));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.arsenicalBronzeNugget.get(), Element.createComposition(MetallurgicaElements.COPPER.ELEMENT.withAmount(4), MetallurgicaElements.TIN.ELEMENT.withAmount(1), MetallurgicaElements.ARSENIC.ELEMENT.withAmount(3)));
        createComposition(pFinishedCompositionConsumer, MaterialHelper.get(ARSENICAL_BRONZE.getMaterial(), SHEET), Element.createComposition(MetallurgicaElements.COPPER.ELEMENT.withAmount(4), MetallurgicaElements.TIN.ELEMENT.withAmount(1), MetallurgicaElements.ARSENIC.ELEMENT.withAmount(3)));
        
        createComposition(pFinishedCompositionConsumer, MaterialHelper.get(TITANIUM_ALUMINIDE.getMaterial(), INGOT), Element.createComposition(MetallurgicaElements.TITANIUM.ELEMENT.withAmount(2), MetallurgicaElements.ALUMINUM.ELEMENT));
        //createComposition(pFinishedCompositionConsumer, MetallurgicaItems.titaniumAluminideNugget.get(), Element.createComposition(MetallurgicaElements.TITANIUM.ELEMENT.withAmount(2), MetallurgicaElements.ALUMINUM.ELEMENT));
        createComposition(pFinishedCompositionConsumer, MaterialHelper.get(TITANIUM_ALUMINIDE.getMaterial(), SHEET), Element.createComposition(MetallurgicaElements.TITANIUM.ELEMENT.withAmount(2), MetallurgicaElements.ALUMINUM.ELEMENT));
        //createComposition(pFinishedCompositionConsumer, MetallurgicaItems.titaniumAluminideDust.get(), Element.createComposition(MetallurgicaElements.TITANIUM.ELEMENT.withAmount(2), MetallurgicaElements.ALUMINUM.ELEMENT));
        //createComposition(pFinishedCompositionConsumer, MetallurgicaItems.semiPressedTitaniumAluminideSheet.get(), Element.createComposition(MetallurgicaElements.TITANIUM.ELEMENT.withAmount(2), MetallurgicaElements.ALUMINUM.ELEMENT));
        
        createComposition(pFinishedCompositionConsumer, MaterialHelper.get(TITANIUM.getMaterial(), INGOT), Element.createComposition(MetallurgicaElements.TITANIUM.ELEMENT));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.titaniumNugget.get(), Element.createComposition(MetallurgicaElements.TITANIUM.ELEMENT));
        createComposition(pFinishedCompositionConsumer, MaterialHelper.get(TITANIUM.getMaterial(), SHEET), Element.createComposition(MetallurgicaElements.TITANIUM.ELEMENT));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.titaniumDust.get(), Element.createComposition(MetallurgicaElements.TITANIUM.ELEMENT));
        //createComposition(pFinishedCompositionConsumer, MetallurgicaItems.semiPressedTitaniumSheet.get(), Element.createComposition(MetallurgicaElements.TITANIUM.ELEMENT));
        
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.pigIron.get(), Element.createComposition(MetallurgicaElements.IRON.ELEMENT.withAmount(3), MetallurgicaElements.CARBON.ELEMENT));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.wroughtIronIngot.get(), Element.createComposition(MetallurgicaElements.IRON.ELEMENT.withAmount(3), MetallurgicaElements.CARBON.ELEMENT));
        
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.hornblendeShard.get(), Element.createComposition(
                MetallurgicaElements.CALCIUM.ELEMENT.withBrackets(),
                MetallurgicaElements.SODIUM.ELEMENT.withForceClosedBracket().withGroupedAmount(2),
                MetallurgicaElements.MAGNESIUM.ELEMENT.withBrackets(),
                MetallurgicaElements.IRON.ELEMENT.withBrackets(),
                MetallurgicaElements.ALUMINUM.ELEMENT.withForceClosedBracket().withGroupedAmount(5),
                MetallurgicaElements.ALUMINUM.ELEMENT.withBrackets(),
                MetallurgicaElements.SILICON.ELEMENT.withForceClosedBracket().withGroupedAmount(8),
                MetallurgicaElements.OXYGEN.ELEMENT.withAmount(22),
                MetallurgicaElements.OXYGEN.ELEMENT.withBrackets(),
                MetallurgicaElements.HYDROGEN.ELEMENT.withForceClosedBracket().withGroupedAmount(2)
        ));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.plagioclaseShard.get(), Element.createComposition(
                MetallurgicaElements.SODIUM.ELEMENT,
                MetallurgicaElements.ALUMINUM.ELEMENT,
                MetallurgicaElements.SILICON.ELEMENT.withAmount(3),
                MetallurgicaElements.OXYGEN.ELEMENT.withAmount(8).withDash(),
                MetallurgicaElements.CALCIUM.ELEMENT,
                MetallurgicaElements.ALUMINUM.ELEMENT.withAmount(2),
                MetallurgicaElements.SILICON.ELEMENT.withAmount(2),
                MetallurgicaElements.OXYGEN.ELEMENT
        ));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.biotiteShard.get(), Element.createComposition(
                MetallurgicaElements.POTASSIUM.ELEMENT,
                MetallurgicaElements.MAGNESIUM.ELEMENT.withBrackets(),
                MetallurgicaElements.IRON.ELEMENT.withForceClosedBracket().withGroupedAmount(3),
                MetallurgicaElements.ALUMINUM.ELEMENT,
                MetallurgicaElements.SILICON.ELEMENT.withAmount(3),
                MetallurgicaElements.OXYGEN.ELEMENT.withAmount(10),
                MetallurgicaElements.FLUORINE.ELEMENT.withBrackets(),
                MetallurgicaElements.OXYGEN.ELEMENT.withBrackets(),
                MetallurgicaElements.HYDROGEN.ELEMENT.withForceClosedBracket().withGroupedAmount(2)
        ));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.clinopyroxeneShard.get(), Element.createComposition(
                MetallurgicaElements.CALCIUM.ELEMENT.withBrackets(),
                MetallurgicaElements.MAGNESIUM.ELEMENT.withBrackets(),
                MetallurgicaElements.IRON.ELEMENT.withBrackets(),
                MetallurgicaElements.SODIUM.ELEMENT.withForceClosedBracket(),
                MetallurgicaElements.MAGNESIUM.ELEMENT.withBrackets(),
                MetallurgicaElements.IRON.ELEMENT.withBrackets(),
                MetallurgicaElements.ALUMINUM.ELEMENT.withForceClosedBracket(),
                MetallurgicaElements.SILICON.ELEMENT.withBrackets(),
                MetallurgicaElements.ALUMINUM.ELEMENT.withForceClosedBracket().withGroupedAmount(2),
                MetallurgicaElements.OXYGEN.ELEMENT.withAmount(6)
        ));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.orthopyroxeneShard.get(), Element.createComposition(
                MetallurgicaElements.MAGNESIUM.ELEMENT.withBrackets(),
                MetallurgicaElements.IRON.ELEMENT.withForceClosedBracket().withGroupedAmount(2),
                MetallurgicaElements.SILICON.ELEMENT.withAmount(2),
                MetallurgicaElements.OXYGEN.ELEMENT.withAmount(6)
        ));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.amphiboleShard.get(), Element.createComposition(
                MetallurgicaElements.MAGNESIUM.ELEMENT.withAmount(7),
                MetallurgicaElements.SILICON.ELEMENT.withAmount(8),
                MetallurgicaElements.OXYGEN.ELEMENT.withAmount(22),
                MetallurgicaElements.OXYGEN.ELEMENT.withBrackets(),
                MetallurgicaElements.HYDROGEN.ELEMENT.withForceClosedBracket().withGroupedAmount(2).withDash(),
                MetallurgicaElements.IRON.ELEMENT.withAmount(7),
                MetallurgicaElements.SILICON.ELEMENT.withAmount(8),
                MetallurgicaElements.OXYGEN.ELEMENT.withAmount(22),
                MetallurgicaElements.OXYGEN.ELEMENT.withBrackets(),
                MetallurgicaElements.HYDROGEN.ELEMENT.withForceClosedBracket().withGroupedAmount(2).withDash(),
                MetallurgicaElements.CALCIUM.ELEMENT.withAmount(7),
                MetallurgicaElements.SILICON.ELEMENT.withAmount(8),
                MetallurgicaElements.OXYGEN.ELEMENT.withAmount(22),
                MetallurgicaElements.OXYGEN.ELEMENT.withBrackets(),
                MetallurgicaElements.HYDROGEN.ELEMENT.withForceClosedBracket().withGroupedAmount(2)
        ));
    }
    
    protected void materialCompositions(Consumer<FinishedComposition> pFinishedCompositionConsumer) {
        createComposition(pFinishedCompositionConsumer, MetallurgicaOre.MAGNETITE.ORE.raw().get(), Element.createComposition(MetallurgicaElements.IRON.ELEMENT.withAmount(3), MetallurgicaElements.OXYGEN.ELEMENT.withAmount(4)));
        
        createComposition(pFinishedCompositionConsumer, MetallurgicaOre.BAUXITE.ORE.raw().get(), Element.createComposition(MetallurgicaElements.ALUMINUM.ELEMENT.withAmount(2), MetallurgicaElements.OXYGEN.ELEMENT.withAmount(3), MetallurgicaElements.HYDROGEN.ELEMENT.withAmount(2).withNumbersUp(), MetallurgicaElements.OXYGEN.ELEMENT.withAmount(2).withNumbersUp()));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.washedAlumina.get(), Element.createComposition(MetallurgicaElements.ALUMINUM.ELEMENT.withAmount(2), MetallurgicaElements.OXYGEN.ELEMENT.withAmount(3)));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.alumina.get(), Element.createComposition(MetallurgicaElements.ALUMINUM.ELEMENT.withAmount(2), MetallurgicaElements.OXYGEN.ELEMENT.withAmount(3)));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.aluminumDust.get(), Element.createComposition(MetallurgicaElements.ALUMINUM.ELEMENT));
        
        createComposition(pFinishedCompositionConsumer, MetallurgicaOre.CASSITERITE.ORE.raw().get(), Element.createComposition(MetallurgicaElements.TIN.ELEMENT, MetallurgicaElements.OXYGEN.ELEMENT.withAmount(2)));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.alluvialCassiterite.get(), Element.createComposition(MetallurgicaElements.TIN.ELEMENT, MetallurgicaElements.OXYGEN.ELEMENT.withAmount(2)));
        
        createComposition(pFinishedCompositionConsumer, MetallurgicaOre.NATIVE_COPPER.ORE.raw().get(), Element.createComposition(MetallurgicaElements.COPPER.ELEMENT));
        createComposition(pFinishedCompositionConsumer, MetallurgicaOre.MALACHITE.ORE.raw().get(), Element.createComposition(MetallurgicaElements.COPPER.ELEMENT.withAmount(2), MetallurgicaElements.CARBON.ELEMENT, MetallurgicaElements.OXYGEN.ELEMENT.withAmount(3), MetallurgicaElements.HYDROGEN.ELEMENT.withAmount(2)));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.copperOxide.get(), Element.createComposition(MetallurgicaElements.COPPER.ELEMENT, MetallurgicaElements.OXYGEN.ELEMENT));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.copperRubble.get(), Element.createComposition(MetallurgicaElements.COPPER.ELEMENT));
        
        createComposition(pFinishedCompositionConsumer, MetallurgicaOre.FLUORITE.ORE.raw().get(), Element.createComposition(MetallurgicaElements.CALCIUM.ELEMENT, MetallurgicaElements.FLUORINE.ELEMENT.withAmount(2)));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.fluoriteCluster.get(), Element.createComposition(MetallurgicaElements.CALCIUM.ELEMENT, MetallurgicaElements.FLUORINE.ELEMENT.withAmount(2)));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.fluoritePowder.get(), Element.createComposition(MetallurgicaElements.CALCIUM.ELEMENT, MetallurgicaElements.FLUORINE.ELEMENT.withAmount(2)));
        
        createComposition(pFinishedCompositionConsumer, MetallurgicaOre.RUTILE.ORE.raw().get(), Element.createComposition(MetallurgicaElements.TITANIUM.ELEMENT, MetallurgicaElements.OXYGEN.ELEMENT.withAmount(2)));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.rutilePowder.get(), Element.createComposition(MetallurgicaElements.TITANIUM.ELEMENT, MetallurgicaElements.OXYGEN.ELEMENT.withAmount(2)));
        
        createComposition(pFinishedCompositionConsumer, MetallurgicaOre.NATIVE_GOLD.ORE.raw().get(), Element.createComposition(MetallurgicaElements.GOLD.ELEMENT));
        
        createComposition(pFinishedCompositionConsumer, MetallurgicaOre.SMITHSONITE.ORE.raw().get(), Element.createComposition(MetallurgicaElements.ZINC.ELEMENT, MetallurgicaElements.CARBON.ELEMENT, MetallurgicaElements.OXYGEN.ELEMENT.withAmount(3)));
        createComposition(pFinishedCompositionConsumer, MetallurgicaOre.SPHALERITE.ORE.raw().get(), Element.createComposition(MetallurgicaElements.ZINC.ELEMENT.withBrackets(), MetallurgicaElements.IRON.ELEMENT.withBrackets(), MetallurgicaElements.SULFUR.ELEMENT));
    }
    
    protected void compatCompositions(Consumer<FinishedComposition> pFinishedCompositionConsumer) {
        createComposition(pFinishedCompositionConsumer, Items.IRON_INGOT, Element.createComposition(MetallurgicaElements.IRON.ELEMENT));
        createComposition(pFinishedCompositionConsumer, Items.IRON_NUGGET, Element.createComposition(MetallurgicaElements.IRON.ELEMENT));
        createComposition(pFinishedCompositionConsumer, Items.GOLD_INGOT, Element.createComposition(MetallurgicaElements.GOLD.ELEMENT));
        createComposition(pFinishedCompositionConsumer, Items.GOLD_NUGGET, Element.createComposition(MetallurgicaElements.GOLD.ELEMENT));
        createComposition(pFinishedCompositionConsumer, Items.COPPER_INGOT, Element.createComposition(MetallurgicaElements.COPPER.ELEMENT));
        createComposition(pFinishedCompositionConsumer, AllItems.COPPER_NUGGET.get(), Element.createComposition(MetallurgicaElements.COPPER.ELEMENT));
        createComposition(pFinishedCompositionConsumer, AllItems.ZINC_INGOT.get(), Element.createComposition(MetallurgicaElements.ZINC.ELEMENT));
        createComposition(pFinishedCompositionConsumer, AllItems.ZINC_NUGGET.get(), Element.createComposition(MetallurgicaElements.ZINC.ELEMENT));
        createComposition(pFinishedCompositionConsumer, TFMGItems.ALUMINUM_INGOT.get(), Element.createComposition(MetallurgicaElements.ALUMINUM.ELEMENT));
        
        createComposition(pFinishedCompositionConsumer, AllItems.BRASS_INGOT.get(), Element.createComposition(MetallurgicaElements.COPPER.ELEMENT.withAmount(3), MetallurgicaElements.ZINC.ELEMENT.withAmount(2)));
        createComposition(pFinishedCompositionConsumer, AllItems.BRASS_NUGGET.get(), Element.createComposition(MetallurgicaElements.COPPER.ELEMENT.withAmount(3), MetallurgicaElements.ZINC.ELEMENT.withAmount(2)));
        createComposition(pFinishedCompositionConsumer, AllItems.BRASS_SHEET.get(), Element.createComposition(MetallurgicaElements.COPPER.ELEMENT.withAmount(3), MetallurgicaElements.ZINC.ELEMENT.withAmount(2)));
        
        createComposition(pFinishedCompositionConsumer, TFMGItems.SULFUR_DUST.get(), Element.createComposition(MetallurgicaElements.SULFUR.ELEMENT));
        createComposition(pFinishedCompositionConsumer, TFMGBlocks.SULFUR.get(), Element.createComposition(MetallurgicaElements.SULFUR.ELEMENT));
        
        createComposition(pFinishedCompositionConsumer, TFMGItems.NITRATE_DUST.get(), Element.createComposition(MetallurgicaElements.POTASSIUM.ELEMENT, MetallurgicaElements.NITROGEN.ELEMENT, MetallurgicaElements.OXYGEN.ELEMENT.withAmount(3)));
        
        createComposition(pFinishedCompositionConsumer, TFMGItems.LIMESAND.get(), Element.createComposition(MetallurgicaElements.CALCIUM.ELEMENT, MetallurgicaElements.CARBON.ELEMENT, MetallurgicaElements.OXYGEN.ELEMENT.withAmount(3)));
    }
    
    public static Item getBucket(String name) {
        return Metallurgica.registrate.get(name+"_bucket", ForgeRegistries.ITEMS.getRegistryKey()).get();
    }
    public static FluidStack getFluidStack(Fluid fluid) {
        return new FluidStack(fluid, 1);
    }
    public static FluidStack getFluidStack(Fluid fluid, CompoundTag tag) {
        FluidStack stack = new FluidStack(fluid, 1);
        stack.setTag(tag);
        return stack;
    }
    protected static void createComposition(Consumer<FinishedComposition> pFinishedCompositionConsumer, ItemLike item, List<Element> elements) {
        CompositionBuilder.create(item, elements).save(pFinishedCompositionConsumer);
    }
    
    protected static void createFluidComposition(Consumer<FinishedComposition> pFinishedCompositionConsumer, FluidStack fluidStack, List<Element> elements) {
        FluidCompositionBuilder.create(fluidStack, elements).save(pFinishedCompositionConsumer);
    }
    
    @Override
    public String getName() {
        return "Metallurgica Composition Provider";
    }
}
