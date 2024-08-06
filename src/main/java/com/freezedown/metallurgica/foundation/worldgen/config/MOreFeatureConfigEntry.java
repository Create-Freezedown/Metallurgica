package com.freezedown.metallurgica.foundation.worldgen.config;

import com.freezedown.metallurgica.foundation.worldgen.MetallurgicaFeatures;
import com.freezedown.metallurgica.foundation.worldgen.feature.configuration.MConfigDrivenLayeredOreFeatureConfiguration;
import com.freezedown.metallurgica.foundation.worldgen.feature.configuration.MConfigDrivenOreFeatureConfiguration;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.simibubi.create.foundation.config.ConfigBase;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.infrastructure.worldgen.LayerPattern;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MOreFeatureConfigEntry extends ConfigBase {
    public static final Map<ResourceLocation, MOreFeatureConfigEntry> ALL = new HashMap<>();
    
    public static final Codec<MOreFeatureConfigEntry> CODEC = ResourceLocation.CODEC
            .comapFlatMap(MOreFeatureConfigEntry::read, entry -> entry.id);
    
    public final ResourceLocation id;
    public final ConfigInt clusterSize;
    public final ConfigFloat frequency;
    public final ConfigInt minHeight;
    public final ConfigInt maxHeight;
    
    private DatagenExtension datagenExt;
    
    public MOreFeatureConfigEntry(ResourceLocation id, int clusterSize, float frequency, int minHeight, int maxHeight) {
        this.id = id;
        
        this.clusterSize = i(clusterSize, 0, "clusterSize");
        this.frequency = f(frequency, 0, 512, "frequency", "Amount of clusters generated per Chunk.",
                "  >1 to spawn multiple.", "  <1 to make it a chance.", "  0 to disable.");
        this.minHeight = i(minHeight, "minHeight");
        this.maxHeight = i(maxHeight, "maxHeight");
        
        ALL.put(id, this);
    }
    
    @Nullable
    public StandardDatagenExtension standardDatagenExt() {
        if (datagenExt == null) {
            datagenExt = new StandardDatagenExtension();
        }
        if (datagenExt instanceof StandardDatagenExtension standard) {
            return standard;
        }
        return null;
    }
    
    @Nullable
    public CustomStandardDatagenExtension customStandardDatagenExt() {
        if (datagenExt == null) {
            datagenExt = new CustomStandardDatagenExtension();
        }
        if (datagenExt instanceof CustomStandardDatagenExtension custom) {
            return custom;
        }
        return null;
    }
    
    @Nullable
    public LayeredDatagenExtension layeredDatagenExt() {
        if (datagenExt == null) {
            datagenExt = new LayeredDatagenExtension();
        }
        if (datagenExt instanceof LayeredDatagenExtension layered) {
            return layered;
        }
        return null;
    }
    
    @Nullable
    public DatagenExtension datagenExt() {
        if (datagenExt != null) {
            return datagenExt;
        }
        return null;
    }
    
    public void addToConfig(ForgeConfigSpec.Builder builder) {
        registerAll(builder);
    }
    
    @Override
    public String getName() {
        return id.getPath();
    }
    
    public static DataResult<MOreFeatureConfigEntry> read(ResourceLocation id) {
        MOreFeatureConfigEntry entry = ALL.get(id);
        if (entry != null) {
            return DataResult.success(entry);
        } else {
            return DataResult.error("Not a valid OreFeatureConfigEntry: " + id);
        }
    }
    
    public abstract class DatagenExtension {
        public TagKey<Biome> biomeTag;
        
        public DatagenExtension biomeTag(TagKey<Biome> biomes) {
            this.biomeTag = biomes;
            return this;
        }
        
        public abstract ConfiguredFeature<?, ?> createConfiguredFeature(RegistryAccess registryAccess);
        
        public PlacedFeature createPlacedFeature(RegistryAccess registryAccess) {
            Registry<ConfiguredFeature<?, ?>> featureRegistry = registryAccess.registryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY);
            Holder<ConfiguredFeature<?, ?>> featureHolder = featureRegistry.getOrCreateHolderOrThrow(ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, id));
            return new PlacedFeature(featureHolder, List.of(new MConfigDrivenPlacement(MOreFeatureConfigEntry.this)));
        }
        
        public BiomeModifier createBiomeModifier(RegistryAccess registryAccess) {
            Registry<Biome> biomeRegistry = registryAccess.registryOrThrow(Registry.BIOME_REGISTRY);
            Registry<PlacedFeature> featureRegistry = registryAccess.registryOrThrow(Registry.PLACED_FEATURE_REGISTRY);
            HolderSet<Biome> biomes = new HolderSet.Named<>(biomeRegistry, biomeTag);
            Holder<PlacedFeature> featureHolder = featureRegistry.getOrCreateHolderOrThrow(ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, id));
            return new ForgeBiomeModifiers.AddFeaturesBiomeModifier(biomes, HolderSet.direct(featureHolder), GenerationStep.Decoration.UNDERGROUND_ORES);
        }
        
        public MOreFeatureConfigEntry parent() {
            return MOreFeatureConfigEntry.this;
        }
    }
    
    public class StandardDatagenExtension extends MOreFeatureConfigEntry.DatagenExtension {
        public NonNullSupplier<? extends Block> block;
        public NonNullSupplier<? extends Block> deepBlock;
        public NonNullSupplier<? extends Block> netherBlock;
        
        public StandardDatagenExtension withBlock(NonNullSupplier<? extends Block> block) {
            this.block = block;
            this.deepBlock = block;
            return this;
        }
        
        public StandardDatagenExtension withBlocks(Couple<NonNullSupplier<? extends Block>> blocks) {
            this.block = blocks.getFirst();
            this.deepBlock = blocks.getSecond();
            return this;
        }
        
        public StandardDatagenExtension withNetherBlock(NonNullSupplier<? extends Block> block) {
            this.netherBlock = block;
            return this;
        }
        
        @Override
        public StandardDatagenExtension biomeTag(TagKey<Biome> biomes) {
            super.biomeTag(biomes);
            return this;
        }
        
        @Override
        public ConfiguredFeature<?, ?> createConfiguredFeature(RegistryAccess registryAccess) {
            List<OreConfiguration.TargetBlockState> targetStates = new ArrayList<>();
            if (block != null)
                targetStates.add(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, block.get()
                        .defaultBlockState()));
            if (deepBlock != null)
                targetStates.add(OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, deepBlock.get()
                        .defaultBlockState()));
            if (netherBlock != null)
                targetStates.add(OreConfiguration.target(OreFeatures.NETHER_ORE_REPLACEABLES, netherBlock.get()
                        .defaultBlockState()));
            
            MConfigDrivenOreFeatureConfiguration config = new MConfigDrivenOreFeatureConfiguration(MOreFeatureConfigEntry.this, 0, targetStates);
            return new ConfiguredFeature<>(MetallurgicaFeatures.STANDARD_ORE.get(), config);
        }
    }
    public class CustomStandardDatagenExtension extends MOreFeatureConfigEntry.DatagenExtension {
        public NonNullSupplier<? extends Block> block;
        public NonNullSupplier<? extends Block> secondaryBlock;
        public RuleTest ruleTest;
        public RuleTest secondaryRuleTest;
        
        public CustomStandardDatagenExtension withBlock(NonNullSupplier<? extends Block> block, RuleTest ruleTest) {
            this.block = block;
            this.ruleTest = ruleTest;
            return this;
        }
        
        public CustomStandardDatagenExtension withBlocks(Couple<NonNullSupplier<? extends Block>> blocks, Couple<RuleTest> ruleTests) {
            this.block = blocks.getFirst();
            this.secondaryBlock = blocks.getSecond();
            this.ruleTest = ruleTests.getFirst();
            this.secondaryRuleTest = ruleTests.getSecond();
            return this;
        }
        
        @Override
        public CustomStandardDatagenExtension biomeTag(TagKey<Biome> biomes) {
            super.biomeTag(biomes);
            return this;
        }
        
        @Override
        public ConfiguredFeature<?, ?> createConfiguredFeature(RegistryAccess registryAccess) {
            List<OreConfiguration.TargetBlockState> targetStates = new ArrayList<>();
            if (block != null && ruleTest != null)
                targetStates.add(OreConfiguration.target(ruleTest, block.get()
                        .defaultBlockState()));
            if (secondaryBlock != null && secondaryRuleTest != null)
                targetStates.add(OreConfiguration.target(secondaryRuleTest, secondaryBlock.get()
                        .defaultBlockState()));
            MConfigDrivenOreFeatureConfiguration config = new MConfigDrivenOreFeatureConfiguration(MOreFeatureConfigEntry.this, 0, targetStates);
            return new ConfiguredFeature<>(MetallurgicaFeatures.STANDARD_ORE.get(), config);
        }
    }
    
    public class LayeredDatagenExtension extends MOreFeatureConfigEntry.DatagenExtension {
        public final List<NonNullSupplier<LayerPattern>> layerPatterns = new ArrayList<>();
        
        public LayeredDatagenExtension withLayerPattern(NonNullSupplier<LayerPattern> pattern) {
            this.layerPatterns.add(pattern);
            return this;
        }
        
        @Override
        public LayeredDatagenExtension biomeTag(TagKey<Biome> biomes) {
            super.biomeTag(biomes);
            return this;
        }
        
        @Override
        public ConfiguredFeature<?, ?> createConfiguredFeature(RegistryAccess registryAccess) {
            List<LayerPattern> layerPatterns = this.layerPatterns.stream()
                    .map(NonNullSupplier::get)
                    .toList();
            
            MConfigDrivenLayeredOreFeatureConfiguration config = new MConfigDrivenLayeredOreFeatureConfiguration(MOreFeatureConfigEntry.this, 0, layerPatterns);
            return new ConfiguredFeature<>(MetallurgicaFeatures.LAYERED_ORE.get(), config);
        }
    }
}
