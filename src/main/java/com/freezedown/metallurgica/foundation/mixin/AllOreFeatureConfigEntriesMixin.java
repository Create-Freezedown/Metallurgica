package com.freezedown.metallurgica.foundation.mixin;

import com.drmangotea.createindustry.worldgen.TFMGLayeredPatterns;
import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.world.striated.MetallurgicaLayeredPatterns;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.DynamicDataProvider;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.infrastructure.worldgen.AllLayerPatterns;
import com.simibubi.create.infrastructure.worldgen.AllOreFeatureConfigEntries;
import com.simibubi.create.infrastructure.worldgen.OreFeatureConfigEntry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Mixin({AllOreFeatureConfigEntries.class})
public class AllOreFeatureConfigEntriesMixin {
    @Unique
    private static final OreFeatureConfigEntry STRIATED_GEODES_OVERWORLD;
    
    public AllOreFeatureConfigEntriesMixin() {
    }
    
    private static OreFeatureConfigEntry create(String name, int clusterSize, float frequency, int minHeight, int maxHeight) {
        ResourceLocation id = Create.asResource(name);
        return new OreFeatureConfigEntry(id, clusterSize, frequency, minHeight, maxHeight);
    }
    
    @Shadow
    public static void fillConfig(ForgeConfigSpec.Builder builder, String namespace) {
        OreFeatureConfigEntry.ALL.forEach((id, entry) -> {
            if (id.getNamespace().equals(namespace)) {
                builder.push(entry.getName());
                entry.addToConfig(builder);
                builder.pop();
            }
            
        });
    }
    
    @Shadow
    public static void init() {
    }
    
    @Shadow
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        RegistryAccess registryAccess = (RegistryAccess)RegistryAccess.BUILTIN.get();
        Map<ResourceLocation, ConfiguredFeature<?, ?>> configuredFeatures = new HashMap();
        Iterator var4 = OreFeatureConfigEntry.ALL.entrySet().iterator();
        
        while(var4.hasNext()) {
            Map.Entry<ResourceLocation, OreFeatureConfigEntry> entry = (Map.Entry)var4.next();
            OreFeatureConfigEntry.DatagenExtension datagenExt = ((OreFeatureConfigEntry)entry.getValue()).datagenExt();
            if (datagenExt != null) {
                configuredFeatures.put((ResourceLocation)entry.getKey(), datagenExt.createConfiguredFeature(registryAccess));
            }
        }
        
        DynamicDataProvider<ConfiguredFeature<?, ?>> configuredFeatureProvider = DynamicDataProvider.create(generator, "Create's Configured Features", registryAccess, Registry.CONFIGURED_FEATURE_REGISTRY, configuredFeatures);
        if (configuredFeatureProvider != null) {
            generator.addProvider(true, configuredFeatureProvider);
        }
        
        Map<ResourceLocation, PlacedFeature> placedFeatures = new HashMap();
        Iterator var13 = OreFeatureConfigEntry.ALL.entrySet().iterator();
        
        while(var13.hasNext()) {
            Map.Entry<ResourceLocation, OreFeatureConfigEntry> entry = (Map.Entry)var13.next();
            OreFeatureConfigEntry.DatagenExtension datagenExt = ((OreFeatureConfigEntry)entry.getValue()).datagenExt();
            if (datagenExt != null) {
                placedFeatures.put((ResourceLocation)entry.getKey(), datagenExt.createPlacedFeature(registryAccess));
            }
        }
        
        DynamicDataProvider<PlacedFeature> placedFeatureProvider = DynamicDataProvider.create(generator, "Create's Placed Features", registryAccess, Registry.PLACED_FEATURE_REGISTRY, placedFeatures);
        if (placedFeatureProvider != null) {
            generator.addProvider(true, placedFeatureProvider);
        }
        
        Map<ResourceLocation, BiomeModifier> biomeModifiers = new HashMap();
        Iterator var16 = OreFeatureConfigEntry.ALL.entrySet().iterator();
        
        while(var16.hasNext()) {
            Map.Entry<ResourceLocation, OreFeatureConfigEntry> entry = (Map.Entry)var16.next();
            OreFeatureConfigEntry.DatagenExtension datagenExt = ((OreFeatureConfigEntry)entry.getValue()).datagenExt();
            if (datagenExt != null) {
                biomeModifiers.put((ResourceLocation)entry.getKey(), datagenExt.createBiomeModifier(registryAccess));
            }
        }
        
        DynamicDataProvider<BiomeModifier> biomeModifierProvider = DynamicDataProvider.create(generator, "Create's Biome Modifiers", registryAccess, ForgeRegistries.Keys.BIOME_MODIFIERS, biomeModifiers);
        if (biomeModifierProvider != null) {
            generator.addProvider(true, biomeModifierProvider);
        }
        
    }
    
    static {
        STRIATED_GEODES_OVERWORLD = create("striated_geodes_overworld", 48, 1 / 32f, -50, 70)
                .layeredDatagenExt()
                .withLayerPattern(MetallurgicaLayeredPatterns.AMETHYST)
                .biomeTag(BiomeTags.IS_OVERWORLD)
                .parent();
    }
}
