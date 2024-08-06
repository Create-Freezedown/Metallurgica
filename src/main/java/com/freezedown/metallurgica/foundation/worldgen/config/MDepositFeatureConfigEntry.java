package com.freezedown.metallurgica.foundation.worldgen.config;

import com.freezedown.metallurgica.foundation.worldgen.MetallurgicaFeatures;
import com.freezedown.metallurgica.foundation.worldgen.feature.configuration.MConfigDrivenOreFeatureConfiguration;
import com.freezedown.metallurgica.foundation.worldgen.feature.configuration.MOreDepositConfiguration;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.simibubi.create.foundation.config.ConfigBase;
import com.simibubi.create.foundation.utility.Couple;
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
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MDepositFeatureConfigEntry extends ConfigBase {
    public static final Map<ResourceLocation, MDepositFeatureConfigEntry> ALL = new HashMap<>();
    
    public static final Codec<MDepositFeatureConfigEntry> CODEC = ResourceLocation.CODEC
            .comapFlatMap(MDepositFeatureConfigEntry::read, entry -> entry.id);
    
    public final ResourceLocation id;
    public final ConfigFloat frequency;
    public final ConfigInt minHeight;
    public final ConfigInt maxHeight;
    
    private DatagenExtension datagenExt;
    
    public MDepositFeatureConfigEntry(ResourceLocation id, float frequency, int minHeight, int maxHeight) {
        this.id = id;
        
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
    
    public static DataResult<MDepositFeatureConfigEntry> read(ResourceLocation id) {
        MDepositFeatureConfigEntry entry = ALL.get(id);
        if (entry != null) {
            return DataResult.success(entry);
        } else {
            return DataResult.error("Not a valid DepositFeatureConfigEntry: " + id);
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
            return new PlacedFeature(featureHolder, List.of(new MDepositConfigDrivenPlacement(MDepositFeatureConfigEntry.this)));
        }
        
        public BiomeModifier createBiomeModifier(RegistryAccess registryAccess) {
            Registry<Biome> biomeRegistry = registryAccess.registryOrThrow(Registry.BIOME_REGISTRY);
            Registry<PlacedFeature> featureRegistry = registryAccess.registryOrThrow(Registry.PLACED_FEATURE_REGISTRY);
            HolderSet<Biome> biomes = new HolderSet.Named<>(biomeRegistry, biomeTag);
            Holder<PlacedFeature> featureHolder = featureRegistry.getOrCreateHolderOrThrow(ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, id));
            return new ForgeBiomeModifiers.AddFeaturesBiomeModifier(biomes, HolderSet.direct(featureHolder), GenerationStep.Decoration.UNDERGROUND_ORES);
        }
        
        public MDepositFeatureConfigEntry parent() {
            return MDepositFeatureConfigEntry.this;
        }
    }
    
    public class StandardDatagenExtension extends DatagenExtension {
        public NonNullSupplier<? extends Block> primaryStone;
        public NonNullSupplier<? extends Block> secondaryStone;
        public NonNullSupplier<? extends Block> mineralStone;
        public NonNullSupplier<? extends Block> deposit;
        
        
        public StandardDatagenExtension withBlock(NonNullSupplier<? extends Block> block, NonNullSupplier<? extends Block> mineralStone, NonNullSupplier<? extends Block> deposit) {
            this.primaryStone = block;
            this.secondaryStone = block;
            this.mineralStone = mineralStone;
            this.deposit = deposit;
            return this;
        }
        
        public StandardDatagenExtension withBlocks( NonNullSupplier<? extends Block> mineralStone, NonNullSupplier<? extends Block> deposit) {
            this.mineralStone = mineralStone;
            this.deposit = deposit;
            return this;
        }
        
        public StandardDatagenExtension withBlocks(Couple<NonNullSupplier<? extends Block>> blocks, NonNullSupplier<? extends Block> mineralStone, NonNullSupplier<? extends Block> deposit) {
            this.primaryStone = blocks.getFirst();
            this.secondaryStone = blocks.getSecond();
            this.mineralStone = mineralStone;
            this.deposit = deposit;
            return this;
        }
        
        @Override
        public StandardDatagenExtension biomeTag(TagKey<Biome> biomes) {
            super.biomeTag(biomes);
            return this;
        }
        
        @Override
        public ConfiguredFeature<?, ?> createConfiguredFeature(RegistryAccess registryAccess) {
            return new ConfiguredFeature<>(MetallurgicaFeatures.ORE_DEPOSIT_SURFACE.get(), new MOreDepositConfiguration(primaryStone.get().defaultBlockState(), secondaryStone.get().defaultBlockState(), mineralStone.get().defaultBlockState(), deposit.get().defaultBlockState()));
        }
    }
}
