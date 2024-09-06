package com.freezedown.metallurgica.foundation.temperature;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.data.custom.temp.biome.BiomeTemperatureManager;
import com.freezedown.metallurgica.foundation.data.custom.temp.dimension.DimensionTemperatureManager;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Predicate;

@Mod.EventBusSubscriber
public class TempUtils {
    
    
    public static double getBiomeTemperature(double temperature, double humidity) {
        return temperature + (humidity - 0.5) * 0.75;
    }
    
    private static float getHeightAdjustedTemperature(BlockPos pPos, Biome biome) {
        float f = biome.getModifiedClimateSettings().temperatureModifier().modifyTemperature(pPos, biome.getModifiedClimateSettings().temperature());
        if (pPos.getY() > 80) {
            float f1 = (float)(new PerlinSimplexNoise(new WorldgenRandom(new LegacyRandomSource(1234L)), ImmutableList.of(0)).getValue((float)pPos.getX() / 8.0F, (float)pPos.getZ() / 8.0F, false) * 8.0);
            return f - (f1 + (float)pPos.getY() - 80.0F) * 0.05F / 40.0F;
        } else {
            return f;
        }
    }
    //Every meter above sea level decreases temperature by 0.0065 degrees celsius
    //Every meter below sea level increases temperature by 0.0065 degrees celsius
    public static double getAltitudeModifier(BlockPos pos, Level level) {
        int y = pos.getY();
        int oceanLevel = level.getSeaLevel();
        if (y > oceanLevel) {
            return 1 - ((y - oceanLevel) * 0.0065);
        } else {
            return 1 + ((oceanLevel - y) * 0.0065);
        }
    }
    
    private static int invertNegativeYLevels(int y, int minHeight, int oceanLevel) {
        int invertedMinHeight = minHeight * -1;
        //swap around the values so that -1 would be equal to 1 plus the ocean level
        return y + oceanLevel;
    }
    
    private static Pair<ResourceLocation, Integer> getNearestValidBiome(BlockPos pos, ServerLevel level) {
        Pair<BlockPos, Holder<Biome>> locatedBiome = null;
        for (ResourceLocation biome : level.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).keySet().stream().toList()) {
            Predicate<Holder<Biome>> biomePredicate = (biomeHolder) -> biomeHolder.get() != level.getBiome(pos).get();
            locatedBiome = level.findClosestBiome3d(biomePredicate, pos, 6400, 32, 64);
            if (locatedBiome == null) return null;
            if (locatedBiome.getSecond().unwrapKey().get().location().equals(level.getBiome(pos).unwrapKey().get().location())) {
                continue;
            }
            for (ResourceLocation blacklistedBiome : BiomeTemperatureManager.getBlendingBlacklist(level.getBiome(pos).unwrapKey().get().location())) {
                if (locatedBiome.getSecond().unwrapKey().get().location().equals(blacklistedBiome)) {
                    return null;
                }
            }
            ResourceLocation biomeLoc = locatedBiome.getSecond().unwrapKey().get().location();
            int distance = (int) Math.sqrt(locatedBiome.getFirst().distSqr(pos));
            Metallurgica.LOGGER.debug("Nearest Biome: {}, Distance: {}", biomeLoc, distance);
            return Pair.of(biomeLoc, distance);
        }
        getDebug();
        return null;
    }
    
    private static void getDebug() {
        Metallurgica.LOGGER.debug("No valid biome found, weird");
    }
    
    private static double blendBiomeTemperatures(BlockPos pos, Level level) {
        //Get the temperature of the nearest biome and blend it with the current biome
        Pair<ResourceLocation, Integer> nearestBiome = getNearestValidBiome(pos, (ServerLevel) level);
        if (nearestBiome == null) {
            Metallurgica.LOGGER.debug("No valid biome found, weird");
            return BiomeTemperatureManager.getTemperature(level.getBiome(pos).unwrapKey().get().location());
        }
        double nearestBiomeTemp = BiomeTemperatureManager.getTemperature(nearestBiome.getFirst());
        double currentBiomeTemp = BiomeTemperatureManager.getTemperature(level.getBiome(pos).unwrapKey().get().location());
        double distance = nearestBiome.getSecond();
        if (distance > 100) {
            Metallurgica.LOGGER.debug("Distance is greater than 100, This is too far away to blend");
            return currentBiomeTemp;
        }
        double blendFactor = 1 - (distance / 100);
        if (distance <= 0) {
            Metallurgica.LOGGER.debug("Distance is less than 0.1, This is probably the same biome");
            return nearestBiomeTemp;
        }
        Metallurgica.LOGGER.debug("Nearest Biome: {}, Distance: {}, Blend Factor: {}", nearestBiome.getFirst(), distance, blendFactor);
        return (nearestBiomeTemp * blendFactor) + (currentBiomeTemp * (1 - blendFactor));
    }
    
    public static double getTemperature(BlockPos pos, Level level) {
        return blendBiomeTemperatures(pos, level);
    }
    
    public static double getPrecipitationModifier(BlockPos pos, Level level) {
        Biome.Precipitation precipitation = level.getBiome(pos).get().getPrecipitation();
        boolean isRaining = level.isRaining() && level.canSeeSky(pos);
        if (precipitation == Biome.Precipitation.RAIN) {
            return isRaining ? 0.93 : 1;
        } else if (precipitation == Biome.Precipitation.SNOW) {
            return isRaining ? 0.43 : 1;
        } else {
            return 1;
        }
    }
    
    public static double getCurrentTemperature(BlockPos.MutableBlockPos pos, Level level) {
        Biome biome = level.getBiome(pos).get();
        ResourceLocation biomeLoc = level.getBiome(pos).unwrapKey().get().location();
        double temperature = blendBiomeTemperatures(pos, level);
        double humidity = biome.getModifiedClimateSettings().downfall();
        Metallurgica.LOGGER.debug("Temperature: {}, Humidity: {}", temperature, humidity);
        return ((getBiomeTemperature(temperature, humidity)) * getAltitudeModifier(pos, level)) * getPrecipitationModifier(pos, level);
    }
    
    @SubscribeEvent
    public void updateTemperature(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        //if (entity.level.isClientSide)
        //    entity.getPersistentData()
        //            .remove("CurrentTemperature");
        if (entity.level.isClientSide)
            entity.getPersistentData()
                    .putDouble("CurrentTemperature",
                            getCurrentTemperature(entity.blockPosition().mutable(), entity.level));
        Metallurgica.LOGGER.debug("Temperature updated t0 {}", entity.getPersistentData().getDouble("CurrentTemperature"));
    }
}
