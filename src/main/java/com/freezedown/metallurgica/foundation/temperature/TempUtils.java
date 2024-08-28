package com.freezedown.metallurgica.foundation.temperature;

import com.freezedown.metallurgica.Metallurgica;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
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
        return temperature + (humidity - 0.5) * 0.5;
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
    
    public static double getCurrentTemperature(BlockPos.MutableBlockPos pos, Biome biome) {
        int altitude = pos.getY();
        double temperature = getHeightAdjustedTemperature(pos, biome);
        double humidity = biome.getModifiedClimateSettings().downfall();
        Metallurgica.LOGGER.debug("Temperature: {}, Humidity: {}", temperature, humidity);
        return getBiomeTemperature(temperature, humidity);
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
                            getCurrentTemperature(entity.blockPosition().mutable(), entity.level.getBiome(entity.blockPosition()).get()));
        Metallurgica.LOGGER.debug("Temperature updated t0 {}", entity.getPersistentData().getDouble("CurrentTemperature"));
    }
}
