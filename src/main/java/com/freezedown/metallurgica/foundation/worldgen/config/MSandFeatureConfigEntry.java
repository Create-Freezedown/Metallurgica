package com.freezedown.metallurgica.foundation.worldgen.config;

import com.freezedown.metallurgica.foundation.worldgen.MetallurgicaFeatures;
import com.freezedown.metallurgica.foundation.worldgen.feature.configuration.MConfigDrivenSandFeatureConfiguration;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.simibubi.create.foundation.config.ConfigBase;
import com.simibubi.create.foundation.utility.Couple;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import com.freezedown.metallurgica.world.MetallurgicaOreFeatureConfigEntries;
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

public class MSandFeatureConfigEntry extends ConfigBase {
    public static final Map<ResourceLocation, MSandFeatureConfigEntry> ALL = new HashMap<>();

    public static final Codec<MSandFeatureConfigEntry> CODEC = ResourceLocation.CODEC
            .comapFlatMap(MSandFeatureConfigEntry::read, entry -> entry.id);

    public final ResourceLocation id;
    public final ConfigInt clusterSize;
    public final ConfigFloat frequency;
    public final ConfigInt chance;
    public final ConfigInt minHeight;
    public final ConfigInt maxHeight;

    private MSandFeatureConfigEntry.DatagenExtension datagenExt;

    public MSandFeatureConfigEntry(ResourceLocation id, int clusterSize, float frequency, int chance, int minHeight, int maxHeight) {
        this.id = id;

        this.clusterSize = i(clusterSize, 0, "clusterSize");
        this.frequency = f(frequency, 0, 512, "frequency", "Amount of clusters generated per Chunk.",
                "  >1 to spawn multiple.", "  <1 to make it a chance.", "  0 to disable.");
        this.chance = i(chance, "chance");
        this.minHeight = i(minHeight, "minHeight");
        this.maxHeight = i(maxHeight, "maxHeight");

        ALL.put(id, this);
    }

    @Nullable
    public MSandFeatureConfigEntry.StandardDatagenExtension standardDatagenExt() {
        if (datagenExt == null) {
            datagenExt = new MSandFeatureConfigEntry.StandardDatagenExtension();
        }
        if (datagenExt instanceof MSandFeatureConfigEntry.StandardDatagenExtension standard) {
            return standard;
        }
        return null;
    }

    @Nullable
    public MSandFeatureConfigEntry.CustomStandardDatagenExtension customStandardDatagenExt() {
        if (datagenExt == null) {
            datagenExt = new MSandFeatureConfigEntry.CustomStandardDatagenExtension();
        }
        if (datagenExt instanceof MSandFeatureConfigEntry.CustomStandardDatagenExtension custom) {
            return custom;
        }
        return null;
    }

    @Nullable
    public MSandFeatureConfigEntry.DatagenExtension datagenExt() {
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

    public static DataResult<MSandFeatureConfigEntry> read(ResourceLocation id) {
        MSandFeatureConfigEntry entry = ALL.get(id);
        if (entry != null) {
            return DataResult.success(entry);
        } else {
            return DataResult.error("Not a valid SandFeatureConfigEntry: " + id);
        }
    }

    public abstract class DatagenExtension {
        public TagKey<Biome> biomeTag;

        public MSandFeatureConfigEntry.DatagenExtension biomeTag(TagKey<Biome> biomes) {
            this.biomeTag = biomes;
            return this;
        }

        public abstract ConfiguredFeature<?, ?> createConfiguredFeature(RegistryAccess registryAccess);

        public PlacedFeature createPlacedFeature(RegistryAccess registryAccess) {
            Registry<ConfiguredFeature<?, ?>> featureRegistry = registryAccess.registryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY);
            Holder<ConfiguredFeature<?, ?>> featureHolder = featureRegistry.getOrCreateHolderOrThrow(ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, id));
            return new PlacedFeature(featureHolder, List.of(new MSandFeatureConfigDrivenPlacement(MSandFeatureConfigEntry.this)));
        }

        public BiomeModifier createBiomeModifier(RegistryAccess registryAccess) {
            Registry<Biome> biomeRegistry = registryAccess.registryOrThrow(Registry.BIOME_REGISTRY);
            Registry<PlacedFeature> featureRegistry = registryAccess.registryOrThrow(Registry.PLACED_FEATURE_REGISTRY);
            HolderSet<Biome> biomes = new HolderSet.Named<>(biomeRegistry, biomeTag);
            Holder<PlacedFeature> featureHolder = featureRegistry.getOrCreateHolderOrThrow(ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, id));
            return new ForgeBiomeModifiers.AddFeaturesBiomeModifier(biomes, HolderSet.direct(featureHolder), GenerationStep.Decoration.UNDERGROUND_ORES);
        }

        public MSandFeatureConfigEntry parent() {
            return MSandFeatureConfigEntry.this;
        }
    }

    public class StandardDatagenExtension extends MSandFeatureConfigEntry.DatagenExtension {
        public NonNullSupplier<? extends Block> block;
        public NonNullSupplier<? extends Block> deepBlock;
        public NonNullSupplier<? extends Block> netherBlock;

        public MSandFeatureConfigEntry.StandardDatagenExtension withBlock(NonNullSupplier<? extends Block> block) {
            this.block = block;
            this.deepBlock = block;
            return this;
        }

        public MSandFeatureConfigEntry.StandardDatagenExtension withBlocks(Couple<NonNullSupplier<? extends Block>> blocks) {
            this.block = blocks.getFirst();
            this.deepBlock = blocks.getSecond();
            return this;
        }

        public MSandFeatureConfigEntry.StandardDatagenExtension withNetherBlock(NonNullSupplier<? extends Block> block) {
            this.netherBlock = block;
            return this;
        }

        @Override
        public MSandFeatureConfigEntry.StandardDatagenExtension biomeTag(TagKey<Biome> biomes) {
            super.biomeTag(biomes);
            return this;
        }

        @Override
        public ConfiguredFeature<?, ?> createConfiguredFeature(RegistryAccess registryAccess) {
            List<OreConfiguration.TargetBlockState> targetStates = new ArrayList<>();
            if (block != null)
                targetStates.add(OreConfiguration.target(MetallurgicaOreFeatureConfigEntries.SAND_REPLACEABLE, block.get()
                        .defaultBlockState()));
            if (deepBlock != null)
                targetStates.add(OreConfiguration.target(MetallurgicaOreFeatureConfigEntries.SAND_REPLACEABLE, deepBlock.get()
                        .defaultBlockState()));
            targetStates = new ArrayList<>();
            if (netherBlock != null)
                targetStates.add(OreConfiguration.target(OreFeatures.NETHER_ORE_REPLACEABLES, netherBlock.get()
                        .defaultBlockState()));
            MConfigDrivenSandFeatureConfiguration config = new MConfigDrivenSandFeatureConfiguration(MSandFeatureConfigEntry.this, 0, targetStates);
            return new ConfiguredFeature<>(MetallurgicaFeatures.SAND_DEPOSIT.get(), config);
        }
    }
    public class CustomStandardDatagenExtension extends MSandFeatureConfigEntry.DatagenExtension {
        public NonNullSupplier<? extends Block> block;
        public NonNullSupplier<? extends Block> secondaryBlock;
        public RuleTest ruleTest;
        public RuleTest secondaryRuleTest;

        public MSandFeatureConfigEntry.CustomStandardDatagenExtension withBlock(NonNullSupplier<? extends Block> block, RuleTest ruleTest) {
            this.block = block;
            this.ruleTest = ruleTest;
            return this;
        }

        public MSandFeatureConfigEntry.CustomStandardDatagenExtension withBlocks(Couple<NonNullSupplier<? extends Block>> blocks, Couple<RuleTest> ruleTests) {
            this.block = blocks.getFirst();
            this.secondaryBlock = blocks.getSecond();
            this.ruleTest = ruleTests.getFirst();
            this.secondaryRuleTest = ruleTests.getSecond();
            return this;
        }

        @Override
        public MSandFeatureConfigEntry.CustomStandardDatagenExtension biomeTag(TagKey<Biome> biomes) {
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
            MConfigDrivenSandFeatureConfiguration config = new MConfigDrivenSandFeatureConfiguration(MSandFeatureConfigEntry.this, 0, targetStates);
            return new ConfiguredFeature<>(MetallurgicaFeatures.SAND_DEPOSIT.get(), config);
        }
    }
}
