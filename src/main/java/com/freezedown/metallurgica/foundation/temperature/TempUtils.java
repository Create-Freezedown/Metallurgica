package com.freezedown.metallurgica.foundation.temperature;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.data.custom.temp.BiomeTemperatureManager;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
        //gotta check for the dimension sea level
        int seaLevel = level.getSeaLevel();
        int min = level.getMinBuildHeight();
        boolean isAboveSeaLevel = pos.getY() > seaLevel;
        boolean isNegative = pos.getY() < 0;
        if (isAboveSeaLevel)
            return pos.getY() * -0.0065;
        else if (isNegative)
            return invertNegativeYLevels(pos.getY(), min, seaLevel) * -0.0065;
        else
            return pos.getY() * 0.0065;
    }
    private static int invertNegativeYLevels(int y, int minHeight, int oceanLevel) {
        int invertedMinHeight = minHeight * -1;
        //swap around the values so that -1 would be equal to 1 plus the ocean level
        return y + oceanLevel;
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
        double temperature = BiomeTemperatureManager.getTemperature(biomeLoc);
        double humidity = biome.getModifiedClimateSettings().downfall();
        Metallurgica.LOGGER.debug("Temperature: {}, Humidity: {}", temperature, humidity);
        return ((getBiomeTemperature(temperature, humidity)) + getAltitudeModifier(pos, level)) * getPrecipitationModifier(pos, level);
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
