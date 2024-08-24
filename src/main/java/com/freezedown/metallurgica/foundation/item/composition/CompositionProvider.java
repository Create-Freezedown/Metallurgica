package com.freezedown.metallurgica.foundation.item.composition;

import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.drmangotea.createindustry.registry.TFMGItems;
import com.freezedown.metallurgica.registry.MetallurgicaElements;
import com.freezedown.metallurgica.registry.MetallurgicaItems;
import com.freezedown.metallurgica.registry.MetallurgicaOre;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.simibubi.create.AllItems;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class CompositionProvider implements DataProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    protected final DataGenerator.PathProvider compositionPathProvider;
    
    public CompositionProvider(DataGenerator pGenerator) {
        this.compositionPathProvider = pGenerator.createPathProvider(DataGenerator.Target.DATA_PACK, "metallurgica_utilities/compositions");
    }
    
    public static void register(DataGenerator gen) {
        CompositionProvider compositionProvider = new CompositionProvider(gen);
        gen.addProvider(true, new DataProvider() {
            
            @Override
            public void run(CachedOutput cachedOutput) throws IOException {
                try {
                    compositionProvider.run(cachedOutput);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            @Override
            public String getName() {
                return "Metallurgica's Composition Provider";
            }
        });
    }
    
    @Override
    public void run(CachedOutput cachedOutput) {
        Set<ResourceLocation> set = Sets.newHashSet();
        this.buildCompositions((pFinishedComposition) -> {
            if (!set.add(pFinishedComposition.getId())) {
                throw new IllegalStateException("Duplicate composition " + pFinishedComposition.getId());
            } else {
                saveComposition(cachedOutput, pFinishedComposition.serializeComposition(), this.compositionPathProvider.json(pFinishedComposition.getId()));
            }
        });
        
    }
    
    private static void saveComposition(CachedOutput pOutput, JsonObject pCompositionJson, Path pPath) {
        try {
            DataProvider.saveStable(pOutput, pCompositionJson, pPath);
        } catch (IOException var4) {
            IOException ioexception = var4;
            LOGGER.error("Couldn't save composition {}", pPath, ioexception);
        }
        
    }
    
    
    protected void buildCompositions(Consumer<FinishedComposition> pFinishedCompositionConsumer) {
        metallurgicaCompositions(pFinishedCompositionConsumer);
        materialCompositions(pFinishedCompositionConsumer);
        compatCompositions(pFinishedCompositionConsumer);
    }
    
    protected void metallurgicaCompositions(Consumer<FinishedComposition> pFinishedCompositionConsumer) {
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.salt.get(), Element.createComposition(MetallurgicaElements.SODIUM.ELEMENT, MetallurgicaElements.CHLORINE.ELEMENT));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.magnesiumOxide.get(), Element.createComposition(MetallurgicaElements.MAGNESIUM.ELEMENT, MetallurgicaElements.OXYGEN.ELEMENT));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.magnesiumChloride.get(), Element.createComposition(MetallurgicaElements.MAGNESIUM.ELEMENT, MetallurgicaElements.CHLORINE.ELEMENT));
        
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.aluminumNugget.get(), Element.createComposition(MetallurgicaElements.ALUMINUM.ELEMENT));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.aluminumSheet.get(), Element.createComposition(MetallurgicaElements.COPPER.ELEMENT));
        
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.bronzeIngot.get(), Element.createComposition(MetallurgicaElements.COPPER.ELEMENT.withAmount(7), MetallurgicaElements.TIN.ELEMENT.withAmount(2)));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.bronzeNugget.get(), Element.createComposition(MetallurgicaElements.COPPER.ELEMENT.withAmount(7), MetallurgicaElements.TIN.ELEMENT.withAmount(2)));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.bronzeSheet.get(), Element.createComposition(MetallurgicaElements.COPPER.ELEMENT.withAmount(7), MetallurgicaElements.TIN.ELEMENT.withAmount(2)));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.bronzeDust.get(), Element.createComposition(MetallurgicaElements.COPPER.ELEMENT.withAmount(7), MetallurgicaElements.TIN.ELEMENT.withAmount(2)));
        
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.arsenicalBronzeIngot.get(), Element.createComposition(MetallurgicaElements.COPPER.ELEMENT.withAmount(4), MetallurgicaElements.TIN.ELEMENT.withAmount(1), MetallurgicaElements.ARSENIC.ELEMENT.withAmount(3)));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.arsenicalBronzeNugget.get(), Element.createComposition(MetallurgicaElements.COPPER.ELEMENT.withAmount(4), MetallurgicaElements.TIN.ELEMENT.withAmount(1), MetallurgicaElements.ARSENIC.ELEMENT.withAmount(3)));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.arsenicalbronzeSheet.get(), Element.createComposition(MetallurgicaElements.COPPER.ELEMENT.withAmount(4), MetallurgicaElements.TIN.ELEMENT.withAmount(1), MetallurgicaElements.ARSENIC.ELEMENT.withAmount(3)));
        
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.titaniumAluminideIngot.get(), Element.createComposition(MetallurgicaElements.TITANIUM.ELEMENT.withAmount(2), MetallurgicaElements.ALUMINUM.ELEMENT));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.titaniumAluminideNugget.get(), Element.createComposition(MetallurgicaElements.TITANIUM.ELEMENT.withAmount(2), MetallurgicaElements.ALUMINUM.ELEMENT));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.titaniumAluminideSheet.get(), Element.createComposition(MetallurgicaElements.TITANIUM.ELEMENT.withAmount(2), MetallurgicaElements.ALUMINUM.ELEMENT));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.titaniumAluminideDust.get(), Element.createComposition(MetallurgicaElements.TITANIUM.ELEMENT.withAmount(2), MetallurgicaElements.ALUMINUM.ELEMENT));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.semiPressedTitaniumAluminideSheet.get(), Element.createComposition(MetallurgicaElements.TITANIUM.ELEMENT.withAmount(2), MetallurgicaElements.ALUMINUM.ELEMENT));
        
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.titaniumIngot.get(), Element.createComposition(MetallurgicaElements.TITANIUM.ELEMENT));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.titaniumNugget.get(), Element.createComposition(MetallurgicaElements.TITANIUM.ELEMENT));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.titaniumSheet.get(), Element.createComposition(MetallurgicaElements.TITANIUM.ELEMENT));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.titaniumDust.get(), Element.createComposition(MetallurgicaElements.TITANIUM.ELEMENT));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.semiPressedTitaniumSheet.get(), Element.createComposition(MetallurgicaElements.TITANIUM.ELEMENT));
        
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.pigIron.get(), Element.createComposition(MetallurgicaElements.IRON.ELEMENT.withAmount(3), MetallurgicaElements.CARBON.ELEMENT));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.wroughtIronIngot.get(), Element.createComposition(MetallurgicaElements.IRON.ELEMENT.withAmount(3), MetallurgicaElements.CARBON.ELEMENT));
    }
    
    protected void materialCompositions(Consumer<FinishedComposition> pFinishedCompositionConsumer) {
        createComposition(pFinishedCompositionConsumer, MetallurgicaOre.MAGNETITE.MATERIAL.raw().get(), Element.createComposition(MetallurgicaElements.IRON.ELEMENT.withAmount(3), MetallurgicaElements.OXYGEN.ELEMENT.withAmount(4)));
        
        createComposition(pFinishedCompositionConsumer, MetallurgicaOre.BAUXITE.MATERIAL.raw().get(), Element.createComposition(MetallurgicaElements.ALUMINUM.ELEMENT.withAmount(2), MetallurgicaElements.OXYGEN.ELEMENT.withAmount(3), MetallurgicaElements.HYDROGEN.ELEMENT.withAmount(2).withNumbersUp(), MetallurgicaElements.OXYGEN.ELEMENT.withAmount(2).withNumbersUp()));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.washedAlumina.get(), Element.createComposition(MetallurgicaElements.ALUMINUM.ELEMENT.withAmount(2), MetallurgicaElements.OXYGEN.ELEMENT.withAmount(3)));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.alumina.get(), Element.createComposition(MetallurgicaElements.ALUMINUM.ELEMENT.withAmount(2), MetallurgicaElements.OXYGEN.ELEMENT.withAmount(3)));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.aluminumDust.get(), Element.createComposition(MetallurgicaElements.ALUMINUM.ELEMENT));
        
        createComposition(pFinishedCompositionConsumer, MetallurgicaOre.CASSITERITE.MATERIAL.raw().get(), Element.createComposition(MetallurgicaElements.TIN.ELEMENT, MetallurgicaElements.OXYGEN.ELEMENT.withAmount(2)));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.alluvialCassiterite.get(), Element.createComposition(MetallurgicaElements.TIN.ELEMENT, MetallurgicaElements.OXYGEN.ELEMENT.withAmount(2)));
        
        createComposition(pFinishedCompositionConsumer, MetallurgicaOre.NATIVE_COPPER.MATERIAL.raw().get(), Element.createComposition(MetallurgicaElements.COPPER.ELEMENT));
        createComposition(pFinishedCompositionConsumer, MetallurgicaOre.MALACHITE.MATERIAL.raw().get(), Element.createComposition(MetallurgicaElements.COPPER.ELEMENT.withAmount(2), MetallurgicaElements.CARBON.ELEMENT, MetallurgicaElements.OXYGEN.ELEMENT.withAmount(3), MetallurgicaElements.HYDROGEN.ELEMENT.withAmount(2)));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.copperOxide.get(), Element.createComposition(MetallurgicaElements.COPPER.ELEMENT, MetallurgicaElements.OXYGEN.ELEMENT));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.copperRubble.get(), Element.createComposition(MetallurgicaElements.COPPER.ELEMENT));
        
        createComposition(pFinishedCompositionConsumer, MetallurgicaOre.FLUORITE.MATERIAL.raw().get(), Element.createComposition(MetallurgicaElements.CALCIUM.ELEMENT, MetallurgicaElements.FLUORINE.ELEMENT.withAmount(2)));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.fluoriteCluster.get(), Element.createComposition(MetallurgicaElements.CALCIUM.ELEMENT, MetallurgicaElements.FLUORINE.ELEMENT.withAmount(2)));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.fluoritePowder.get(), Element.createComposition(MetallurgicaElements.CALCIUM.ELEMENT, MetallurgicaElements.FLUORINE.ELEMENT.withAmount(2)));
        
        createComposition(pFinishedCompositionConsumer, MetallurgicaOre.RUTILE.MATERIAL.raw().get(), Element.createComposition(MetallurgicaElements.TITANIUM.ELEMENT, MetallurgicaElements.OXYGEN.ELEMENT.withAmount(2)));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.rutilePowder.get(), Element.createComposition(MetallurgicaElements.TITANIUM.ELEMENT, MetallurgicaElements.OXYGEN.ELEMENT.withAmount(2)));
        
        createComposition(pFinishedCompositionConsumer, MetallurgicaOre.NATIVE_GOLD.MATERIAL.raw().get(), Element.createComposition(MetallurgicaElements.GOLD.ELEMENT));
        
        createComposition(pFinishedCompositionConsumer, MetallurgicaOre.SMITHSONITE.MATERIAL.raw().get(), Element.createComposition(MetallurgicaElements.ZINC.ELEMENT, MetallurgicaElements.CARBON.ELEMENT, MetallurgicaElements.OXYGEN.ELEMENT.withAmount(3)));
        createComposition(pFinishedCompositionConsumer, MetallurgicaOre.SPHALERITE.MATERIAL.raw().get(), Element.createComposition(MetallurgicaElements.ZINC.ELEMENT.withBrackets(), MetallurgicaElements.IRON.ELEMENT.withBrackets(), MetallurgicaElements.SULFUR.ELEMENT));
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
    
    protected static void createComposition(Consumer<FinishedComposition> pFinishedCompositionConsumer, ItemLike item, List<Element> elements) {
        CompositionBuilder.create(item, elements).save(pFinishedCompositionConsumer);
    }
    
    @Override
    public String getName() {
        return "Metallurgica Composition Provider";
    }
}
