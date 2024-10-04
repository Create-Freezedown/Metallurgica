package com.freezedown.metallurgica.foundation.worldgen.feature.configuration;

import com.freezedown.metallurgica.foundation.worldgen.MetallurgicaFeatures;
import com.freezedown.metallurgica.foundation.worldgen.config.GenericConfigDrivenPlacement;
import com.freezedown.metallurgica.foundation.worldgen.config.MConfigDrivenPlacement;
import com.freezedown.metallurgica.foundation.worldgen.config.MOreFeatureConfigEntry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.simibubi.create.foundation.config.ConfigBase;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericFeatureConfigEntry extends ConfigBase {
    public static final Map<ResourceLocation, GenericFeatureConfigEntry> ALL = new HashMap<>();

    public static final Codec<GenericFeatureConfigEntry> CODEC = ResourceLocation.CODEC
            .comapFlatMap(GenericFeatureConfigEntry::read, entry -> entry.id);

    public final ResourceLocation id;
    private DatagenExtension datagenExt;

    public final int size;
    public final ConfigFloat frequency;
    public final ConfigInt chance;
    public final ConfigInt minHeight;
    public final ConfigInt maxHeight;

    public GenericFeatureConfigEntry(ResourceLocation id, int size, float frequency, int chance, int minHeight, int maxHeight) {
        this.id = id;

        this.size = size;
        this.frequency = f(frequency, 0, 512, "frequency", "Amount generated per Chunk.",
                "  >1 to spawn multiple.", "  <1 to make it a chance.", "  0 to disable.");
        this.chance = i(chance, "chance");
        this.minHeight = i(minHeight, "minHeight");
        this.maxHeight = i(maxHeight, "maxHeight");

        ALL.put(id, this);
    }

    @Nullable
    public LakeDatagenExtension standardDatagenExt() {
        if (datagenExt == null) {
            datagenExt = new LakeDatagenExtension();
        }
        if (datagenExt instanceof LakeDatagenExtension standard) {
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

    public static DataResult<GenericFeatureConfigEntry> read(ResourceLocation id) {
        GenericFeatureConfigEntry entry = ALL.get(id);
        if (entry != null) {
            return DataResult.success(entry);
        } else {
            return DataResult.error("Not a valid GenericFeatureConfigEntry: " + id);
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
            return new PlacedFeature(featureHolder, List.of(new GenericConfigDrivenPlacement(GenericFeatureConfigEntry.this)));
        }

        public BiomeModifier createBiomeModifier(RegistryAccess registryAccess) {
            Registry<Biome> biomeRegistry = registryAccess.registryOrThrow(Registry.BIOME_REGISTRY);
            Registry<PlacedFeature> featureRegistry = registryAccess.registryOrThrow(Registry.PLACED_FEATURE_REGISTRY);
            HolderSet<Biome> biomes = new HolderSet.Named<>(biomeRegistry, biomeTag);
            Holder<PlacedFeature> featureHolder = featureRegistry.getOrCreateHolderOrThrow(ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, id));
            return new ForgeBiomeModifiers.AddFeaturesBiomeModifier(biomes, HolderSet.direct(featureHolder), GenerationStep.Decoration.UNDERGROUND_ORES);
        }

        public GenericFeatureConfigEntry parent() {
            return GenericFeatureConfigEntry.this;
        }
    }

    public class LakeDatagenExtension extends DatagenExtension {
        @Override
        public ConfiguredFeature<?, ?> createConfiguredFeature(RegistryAccess registryAccess) {
            LakeConfiguration config = new LakeConfiguration(size);
            return new ConfiguredFeature<>(MetallurgicaFeatures.LAKE.get(), config);
        }
    }
}
