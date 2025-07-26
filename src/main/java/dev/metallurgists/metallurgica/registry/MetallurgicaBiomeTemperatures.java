package dev.metallurgists.metallurgica.registry;

import dev.metallurgists.metallurgica.foundation.data.custom.temp.FinishedData;
import dev.metallurgists.metallurgica.foundation.data.custom.temp.TemperatureBuilder;
import dev.metallurgists.metallurgica.foundation.data.custom.temp.dimension.DimensionDataBuilder;
import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import net.minecraft.core.Holder;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class MetallurgicaBiomeTemperatures implements DataProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    protected final PackOutput.PathProvider tempPathProvider;
    protected final PackOutput.PathProvider dimensionPathProvider;
    
    public MetallurgicaBiomeTemperatures(PackOutput packOutput) {
        this.tempPathProvider = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "metallurgica_utilities/biome_temperatures");
        this.dimensionPathProvider = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "metallurgica_utilities/dimension_data");
    }
    
    public static void register(DataGenerator gen) {
        MetallurgicaBiomeTemperatures metallurgicaBiomeTemperatures = new MetallurgicaBiomeTemperatures(gen.getPackOutput());
        gen.addProvider(true, new DataProvider() {
            
            @Override
            public CompletableFuture<?> run(CachedOutput cachedOutput) {
                return metallurgicaBiomeTemperatures.run(cachedOutput);
            }
            
            @Override
            public String getName() {
                return "Metallurgica's Biome Temperature Provider";
            }
        });
    }
    
    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        List<CompletableFuture<?>> futures = new ArrayList<>();
        Set<ResourceLocation> set = Sets.newHashSet();
        this.buildTemperatures((pFinishedTemp) -> {
            if (!set.add(pFinishedTemp.getId())) {
                throw new IllegalStateException("Duplicate composition " + pFinishedTemp.getId());
            } else {
                futures.add(DataProvider.saveStable(cachedOutput, pFinishedTemp.serialize(), this.tempPathProvider.json(pFinishedTemp.getId())));
            }
        });
        this.buildDimensions((pFinishedDimension) -> {
            if (!set.add(pFinishedDimension.getId())) {
                throw new IllegalStateException("Duplicate composition " + pFinishedDimension.getId());
            } else {
                futures.add(DataProvider.saveStable(cachedOutput, pFinishedDimension.serialize(), this.dimensionPathProvider.json(pFinishedDimension.getId())));
            }
        });
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }
    
    protected void buildTemperatures(Consumer<FinishedData> pFinishedTemperatureConsumer) {
        overworldTemperatures(pFinishedTemperatureConsumer);
        netherTemperatures(pFinishedTemperatureConsumer);
    }
    
    protected void buildDimensions(Consumer<FinishedData> pFinishedDimensionConsumer) {
        createDimensionData(pFinishedDimensionConsumer, new ResourceLocation("minecraft", "overworld"), -23.2, 143.3);
        createDimensionData(pFinishedDimensionConsumer, new ResourceLocation("minecraft", "the_nether"), 183.2, 423.6);
        createDimensionData(pFinishedDimensionConsumer, new ResourceLocation("minecraft", "the_end"), -23.2, 143.3);
    }
    
    protected void overworldTemperatures(Consumer<FinishedData> pFinishedTemperatureConsumer) {
        createTemperature(pFinishedTemperatureConsumer, Biomes.BADLANDS.location(), 26.6, Biomes.ERODED_BADLANDS.location());
        createTemperature(pFinishedTemperatureConsumer, Biomes.BAMBOO_JUNGLE.location(), 32.3, Biomes.JUNGLE.location(), Biomes.SPARSE_JUNGLE.location());
        createTemperature(pFinishedTemperatureConsumer, Biomes.BEACH.location(), 16.8);
        createTemperature(pFinishedTemperatureConsumer, Biomes.BIRCH_FOREST.location(), 8.2, Biomes.WINDSWEPT_FOREST.location(), Biomes.FOREST.location(), Biomes.OLD_GROWTH_BIRCH_FOREST.location(), Biomes.GROVE.location(), Biomes.MEADOW.location());
        createTemperature(pFinishedTemperatureConsumer, Biomes.COLD_OCEAN.location(), 2.0);
        createTemperature(pFinishedTemperatureConsumer, Biomes.DARK_FOREST.location(), 6.3);
        createTemperature(pFinishedTemperatureConsumer, Biomes.DEEP_COLD_OCEAN.location(), 0.5);
        createTemperature(pFinishedTemperatureConsumer, Biomes.DEEP_FROZEN_OCEAN.location(), -2.0);
        createTemperature(pFinishedTemperatureConsumer, Biomes.DEEP_LUKEWARM_OCEAN.location(), 1.5);
        createTemperature(pFinishedTemperatureConsumer, Biomes.DEEP_OCEAN.location(), 0.5);
        createTemperature(pFinishedTemperatureConsumer, Biomes.DESERT.location(), 38.2);
        createTemperature(pFinishedTemperatureConsumer, Biomes.ERODED_BADLANDS.location(), 26.6, Biomes.BADLANDS.location());
        createTemperature(pFinishedTemperatureConsumer, Biomes.FLOWER_FOREST.location(), 7.5);
        createTemperature(pFinishedTemperatureConsumer, Biomes.FOREST.location(), 8.2, Biomes.WINDSWEPT_FOREST.location(), Biomes.BIRCH_FOREST.location(), Biomes.MEADOW.location(), Biomes.OLD_GROWTH_BIRCH_FOREST.location(), Biomes.GROVE.location());
        createTemperature(pFinishedTemperatureConsumer, Biomes.FROZEN_OCEAN.location(), -1.5);
        createTemperature(pFinishedTemperatureConsumer, Biomes.FROZEN_PEAKS.location(), 3.4);
        createTemperature(pFinishedTemperatureConsumer, Biomes.FROZEN_RIVER.location(), -0.5);
        createTemperature(pFinishedTemperatureConsumer, Biomes.GROVE.location(), 8.2, Biomes.FOREST.location(), Biomes.MEADOW.location(), Biomes.BIRCH_FOREST.location(), Biomes.WINDSWEPT_FOREST.location(), Biomes.OLD_GROWTH_BIRCH_FOREST.location());
        createTemperature(pFinishedTemperatureConsumer, Biomes.ICE_SPIKES.location(), -0.5);
        createTemperature(pFinishedTemperatureConsumer, Biomes.JAGGED_PEAKS.location(), 8.2);
        createTemperature(pFinishedTemperatureConsumer, Biomes.JUNGLE.location(), 32.3, Biomes.BAMBOO_JUNGLE.location(), Biomes.SPARSE_JUNGLE.location());
        createTemperature(pFinishedTemperatureConsumer, Biomes.LUKEWARM_OCEAN.location(), 2.1);
        createTemperature(pFinishedTemperatureConsumer, Biomes.MANGROVE_SWAMP.location(), 26.6, Biomes.SWAMP.location());
        createTemperature(pFinishedTemperatureConsumer, Biomes.MEADOW.location(), 8.2, Biomes.FOREST.location(), Biomes.GROVE.location(), Biomes.BIRCH_FOREST.location(), Biomes.WINDSWEPT_FOREST.location(), Biomes.OLD_GROWTH_BIRCH_FOREST.location());
        createTemperature(pFinishedTemperatureConsumer, Biomes.MUSHROOM_FIELDS.location(), 8.2);
        createTemperature(pFinishedTemperatureConsumer, Biomes.OCEAN.location(), 0.5);
        createTemperature(pFinishedTemperatureConsumer, Biomes.OLD_GROWTH_BIRCH_FOREST.location(), 8.2, Biomes.BIRCH_FOREST.location());
        createTemperature(pFinishedTemperatureConsumer, Biomes.OLD_GROWTH_PINE_TAIGA.location(), 8.2, Biomes.OLD_GROWTH_SPRUCE_TAIGA.location(), Biomes.TAIGA.location());
        createTemperature(pFinishedTemperatureConsumer, Biomes.OLD_GROWTH_SPRUCE_TAIGA.location(), 8.2, Biomes.OLD_GROWTH_PINE_TAIGA.location(), Biomes.TAIGA.location());
        createTemperature(pFinishedTemperatureConsumer, Biomes.PLAINS.location(), 9.4, Biomes.SUNFLOWER_PLAINS.location());
        createTemperature(pFinishedTemperatureConsumer, Biomes.RIVER.location(), 4.5);
        createTemperature(pFinishedTemperatureConsumer, Biomes.SAVANNA.location(), 26.6, Biomes.SAVANNA_PLATEAU.location(), Biomes.WINDSWEPT_SAVANNA.location());
        createTemperature(pFinishedTemperatureConsumer, Biomes.SAVANNA_PLATEAU.location(), 26.6, Biomes.SAVANNA.location(), Biomes.WINDSWEPT_SAVANNA.location());
        createTemperature(pFinishedTemperatureConsumer, Biomes.SNOWY_BEACH.location(), 0.05, Biomes.SNOWY_PLAINS.location(), Biomes.SNOWY_TAIGA.location(), Biomes.SNOWY_SLOPES.location());
        createTemperature(pFinishedTemperatureConsumer, Biomes.SNOWY_PLAINS.location(), 0.5, Biomes.SNOWY_BEACH.location(), Biomes.SNOWY_TAIGA.location(), Biomes.SNOWY_SLOPES.location());
        createTemperature(pFinishedTemperatureConsumer, Biomes.SNOWY_SLOPES.location(), 0.5, Biomes.SNOWY_BEACH.location(), Biomes.SNOWY_PLAINS.location(), Biomes.SNOWY_TAIGA.location());
        createTemperature(pFinishedTemperatureConsumer, Biomes.SNOWY_TAIGA.location(), 0.5, Biomes.SNOWY_BEACH.location(), Biomes.SNOWY_PLAINS.location(), Biomes.SNOWY_SLOPES.location());
        createTemperature(pFinishedTemperatureConsumer, Biomes.SPARSE_JUNGLE.location(), 32.3, Biomes.JUNGLE.location(), Biomes.BAMBOO_JUNGLE.location());
        createTemperature(pFinishedTemperatureConsumer, Biomes.STONY_PEAKS.location(), 8.2, Biomes.JAGGED_PEAKS.location());
        createTemperature(pFinishedTemperatureConsumer, Biomes.STONY_SHORE.location(), 6.2);
        createTemperature(pFinishedTemperatureConsumer, Biomes.SUNFLOWER_PLAINS.location(), 9.4, Biomes.PLAINS.location());
        createTemperature(pFinishedTemperatureConsumer, Biomes.SWAMP.location(), 26.6, Biomes.MANGROVE_SWAMP.location());
        createTemperature(pFinishedTemperatureConsumer, Biomes.TAIGA.location(), 8.2, Biomes.OLD_GROWTH_PINE_TAIGA.location(), Biomes.OLD_GROWTH_SPRUCE_TAIGA.location());
        createTemperature(pFinishedTemperatureConsumer, Biomes.WARM_OCEAN.location(), 5.6);
        createTemperature(pFinishedTemperatureConsumer, Biomes.WINDSWEPT_FOREST.location(), 8.1, Biomes.BIRCH_FOREST.location(), Biomes.FOREST.location(), Biomes.OLD_GROWTH_BIRCH_FOREST.location(), Biomes.GROVE.location(), Biomes.MEADOW.location());
        createTemperature(pFinishedTemperatureConsumer, Biomes.WINDSWEPT_GRAVELLY_HILLS.location(), 8.1, Biomes.WINDSWEPT_HILLS.location());
        createTemperature(pFinishedTemperatureConsumer, Biomes.WINDSWEPT_HILLS.location(), 8.1, Biomes.WINDSWEPT_GRAVELLY_HILLS.location());
        createTemperature(pFinishedTemperatureConsumer, Biomes.WINDSWEPT_SAVANNA.location(), 24.6, Biomes.SAVANNA.location(), Biomes.SAVANNA_PLATEAU.location());
        createTemperature(pFinishedTemperatureConsumer, Biomes.WOODED_BADLANDS.location(), 24.3);
    }
    
    protected void netherTemperatures(Consumer<FinishedData> pFinishedTemperatureConsumer) {
        //The Nether is hot enough to instantly evaporate water.
        createTemperature(pFinishedTemperatureConsumer, Biomes.NETHER_WASTES.location(), 235.3);
        createTemperature(pFinishedTemperatureConsumer, Biomes.CRIMSON_FOREST.location(), 179.4);
        createTemperature(pFinishedTemperatureConsumer, Biomes.WARPED_FOREST.location(), 143.5);
        createTemperature(pFinishedTemperatureConsumer, Biomes.SOUL_SAND_VALLEY.location(), 225.6);
        createTemperature(pFinishedTemperatureConsumer, Biomes.BASALT_DELTAS.location(), 245.3);
    }
    
    protected static void createTemperature(Consumer<FinishedData> pFinishedTemperatureConsumer, ResourceLocation biomeId, double surfaceTemperature) {
        TemperatureBuilder.create(biomeId, surfaceTemperature).save(pFinishedTemperatureConsumer);
    }
    
    protected static void createTemperature(Consumer<FinishedData> pFinishedTemperatureConsumer, Holder<Biome> biome, double surfaceTemperature) {
        TemperatureBuilder.create(biome, surfaceTemperature).save(pFinishedTemperatureConsumer);
    }
    
    protected static void createTemperature(Consumer<FinishedData> pFinishedTemperatureConsumer, ResourceLocation biomeId, double surfaceTemperature, ResourceLocation... blacklist) {
        TemperatureBuilder.create(biomeId, surfaceTemperature).withBlacklist(Arrays.stream(blacklist).toList()).save(pFinishedTemperatureConsumer);
    }
    
    protected static void createDimensionData(Consumer<FinishedData> pFinishedDimensionConsumer, ResourceLocation dimensionId, double maxHeightTemperature, double minHeightTemperature) {
        DimensionDataBuilder.create(dimensionId, maxHeightTemperature, minHeightTemperature).save(pFinishedDimensionConsumer);
    }
                                            
                                            @Override
    public String getName() {
        return "";
    }
}
