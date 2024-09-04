package com.freezedown.metallurgica.registry;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.data.custom.composition.FinishedComposition;
import com.freezedown.metallurgica.foundation.data.custom.temp.FinishedTemperature;
import com.freezedown.metallurgica.foundation.data.custom.temp.TemperatureBuilder;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.core.Holder;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Consumer;

public class MetallurgicaBiomeTemperatures implements DataProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    protected final DataGenerator.PathProvider tempPathProvider;
    
    public MetallurgicaBiomeTemperatures(DataGenerator pGenerator) {
        this.tempPathProvider = pGenerator.createPathProvider(DataGenerator.Target.DATA_PACK, "metallurgica_utilities/biome_temperatures");
    }
    
    public static void register(DataGenerator gen) {
        MetallurgicaBiomeTemperatures metallurgicaBiomeTemperatures = new MetallurgicaBiomeTemperatures(gen);
        gen.addProvider(true, new DataProvider() {
            
            @Override
            public void run(CachedOutput cachedOutput) {
                try {
                    metallurgicaBiomeTemperatures.run(cachedOutput);
                } catch (Exception e) {
                    Metallurgica.LOGGER.error("Failed to save temperatures", e);
                }
            }
            
            @Override
            public String getName() {
                return "Metallurgica's Biome Temperature Provider";
            }
        });
    }
    
    @Override
    public void run(CachedOutput cachedOutput) throws IOException {
        Set<ResourceLocation> set = Sets.newHashSet();
        this.buildTemperatures((pFinishedTemp) -> {
            if (!set.add(pFinishedTemp.getId())) {
                throw new IllegalStateException("Duplicate composition " + pFinishedTemp.getId());
            } else {
                saveTemp(cachedOutput, pFinishedTemp.serializeTemp(), this.tempPathProvider.json(pFinishedTemp.getId()));
            }
        });
    }
    
    private static void saveTemp(CachedOutput pOutput, JsonObject pCompositionJson, Path pPath) {
        try {
            DataProvider.saveStable(pOutput, pCompositionJson, pPath);
        } catch (IOException ioexception) {
            LOGGER.error("Couldn't save composition {}", pPath, ioexception);
        }
    }
    
    protected void buildTemperatures(Consumer<FinishedTemperature> pFinishedTemperatureConsumer) {
        overworldTemperatures(pFinishedTemperatureConsumer);
        netherTemperatures(pFinishedTemperatureConsumer);
    }
    
    protected void overworldTemperatures(Consumer<FinishedTemperature> pFinishedTemperatureConsumer) {
        createTemperature(pFinishedTemperatureConsumer, Biomes.BADLANDS.location(), 26.6);
        createTemperature(pFinishedTemperatureConsumer, Biomes.BAMBOO_JUNGLE.location(), 32.3);
        createTemperature(pFinishedTemperatureConsumer, Biomes.BEACH.location(), 16.8);
        createTemperature(pFinishedTemperatureConsumer, Biomes.BIRCH_FOREST.location(), 8.2);
        createTemperature(pFinishedTemperatureConsumer, Biomes.COLD_OCEAN.location(), 2.0);
        createTemperature(pFinishedTemperatureConsumer, Biomes.DARK_FOREST.location(), 6.3);
        createTemperature(pFinishedTemperatureConsumer, Biomes.DEEP_COLD_OCEAN.location(), 0.5);
        createTemperature(pFinishedTemperatureConsumer, Biomes.DEEP_FROZEN_OCEAN.location(), -2.0);
        createTemperature(pFinishedTemperatureConsumer, Biomes.DEEP_LUKEWARM_OCEAN.location(), 1.5);
        createTemperature(pFinishedTemperatureConsumer, Biomes.DEEP_OCEAN.location(), 0.5);
        createTemperature(pFinishedTemperatureConsumer, Biomes.DESERT.location(), 38.2);
        createTemperature(pFinishedTemperatureConsumer, Biomes.ERODED_BADLANDS.location(), 26.6);
        createTemperature(pFinishedTemperatureConsumer, Biomes.FLOWER_FOREST.location(), 7.5);
        createTemperature(pFinishedTemperatureConsumer, Biomes.FOREST.location(), 8.2);
        createTemperature(pFinishedTemperatureConsumer, Biomes.FROZEN_OCEAN.location(), -1.5);
        createTemperature(pFinishedTemperatureConsumer, Biomes.FROZEN_PEAKS.location(), 3.4);
        createTemperature(pFinishedTemperatureConsumer, Biomes.FROZEN_RIVER.location(), -0.5);
        createTemperature(pFinishedTemperatureConsumer, Biomes.GROVE.location(), 8.2);
        createTemperature(pFinishedTemperatureConsumer, Biomes.ICE_SPIKES.location(), -0.5);
        createTemperature(pFinishedTemperatureConsumer, Biomes.JAGGED_PEAKS.location(), 8.2);
        createTemperature(pFinishedTemperatureConsumer, Biomes.JUNGLE.location(), 32.3);
        createTemperature(pFinishedTemperatureConsumer, Biomes.LUKEWARM_OCEAN.location(), 2.1);
        createTemperature(pFinishedTemperatureConsumer, Biomes.MANGROVE_SWAMP.location(), 26.6);
        createTemperature(pFinishedTemperatureConsumer, Biomes.MEADOW.location(), 8.2);
        createTemperature(pFinishedTemperatureConsumer, Biomes.MUSHROOM_FIELDS.location(), 8.2);
        createTemperature(pFinishedTemperatureConsumer, Biomes.OCEAN.location(), 0.5);
        createTemperature(pFinishedTemperatureConsumer, Biomes.OLD_GROWTH_BIRCH_FOREST.location(), 8.2);
        createTemperature(pFinishedTemperatureConsumer, Biomes.OLD_GROWTH_PINE_TAIGA.location(), 8.2);
        createTemperature(pFinishedTemperatureConsumer, Biomes.OLD_GROWTH_SPRUCE_TAIGA.location(), 8.2);
        createTemperature(pFinishedTemperatureConsumer, Biomes.PLAINS.location(), 9.4);
        createTemperature(pFinishedTemperatureConsumer, Biomes.RIVER.location(), 4.5);
        createTemperature(pFinishedTemperatureConsumer, Biomes.SAVANNA.location(), 26.6);
        createTemperature(pFinishedTemperatureConsumer, Biomes.SAVANNA_PLATEAU.location(), 26.6);
        createTemperature(pFinishedTemperatureConsumer, Biomes.SNOWY_BEACH.location(), 0.05);
        createTemperature(pFinishedTemperatureConsumer, Biomes.SNOWY_PLAINS.location(), 0.5);
        createTemperature(pFinishedTemperatureConsumer, Biomes.SNOWY_SLOPES.location(), 0.5);
        createTemperature(pFinishedTemperatureConsumer, Biomes.SNOWY_TAIGA.location(), 0.5);
        createTemperature(pFinishedTemperatureConsumer, Biomes.SPARSE_JUNGLE.location(), 32.3);
        createTemperature(pFinishedTemperatureConsumer, Biomes.STONY_PEAKS.location(), 8.2);
        createTemperature(pFinishedTemperatureConsumer, Biomes.STONY_SHORE.location(), 6.2);
        createTemperature(pFinishedTemperatureConsumer, Biomes.SUNFLOWER_PLAINS.location(), 9.4);
        createTemperature(pFinishedTemperatureConsumer, Biomes.SWAMP.location(), 26.6);
        createTemperature(pFinishedTemperatureConsumer, Biomes.TAIGA.location(), 8.2);
        createTemperature(pFinishedTemperatureConsumer, Biomes.WARM_OCEAN.location(), 5.6);
        createTemperature(pFinishedTemperatureConsumer, Biomes.WINDSWEPT_FOREST.location(), 8.1);
        createTemperature(pFinishedTemperatureConsumer, Biomes.WINDSWEPT_GRAVELLY_HILLS.location(), 8.1);
        createTemperature(pFinishedTemperatureConsumer, Biomes.WINDSWEPT_HILLS.location(), 8.1);
        createTemperature(pFinishedTemperatureConsumer, Biomes.WINDSWEPT_SAVANNA.location(), 24.6);
        createTemperature(pFinishedTemperatureConsumer, Biomes.WOODED_BADLANDS.location(), 24.3);
    }
    
    protected void netherTemperatures(Consumer<FinishedTemperature> pFinishedTemperatureConsumer) {
        //The Nether is hot enough to instantly evaporate water.
        createTemperature(pFinishedTemperatureConsumer, Biomes.NETHER_WASTES.location(), 235.3);
        createTemperature(pFinishedTemperatureConsumer, Biomes.CRIMSON_FOREST.location(), 179.4);
        createTemperature(pFinishedTemperatureConsumer, Biomes.WARPED_FOREST.location(), 143.5);
        createTemperature(pFinishedTemperatureConsumer, Biomes.SOUL_SAND_VALLEY.location(), 225.6);
        createTemperature(pFinishedTemperatureConsumer, Biomes.BASALT_DELTAS.location(), 245.3);
    }
    
    protected static void createTemperature(Consumer<FinishedTemperature> pFinishedTemperatureConsumer, ResourceLocation biomeId, double surfaceTemperature) {
        TemperatureBuilder.create(biomeId, surfaceTemperature).save(pFinishedTemperatureConsumer);
    }
    
    protected static void createTemperature(Consumer<FinishedTemperature> pFinishedTemperatureConsumer, Holder<Biome> biome, double surfaceTemperature) {
        TemperatureBuilder.create(biome, surfaceTemperature).save(pFinishedTemperatureConsumer);
    }
                                            
                                            
                                            @Override
    public String getName() {
        return "";
    }
}
