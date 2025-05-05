package com.freezedown.metallurgica.infastructure.temperature;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.temperature.BlockStateTemperature;
import com.freezedown.metallurgica.foundation.temperature.TempUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import oshi.util.tuples.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@SideOnly(Side.SERVER)
public class TemperatureHandler {
    //                   level           pos            data
    public static Map<ServerLevel, Map<BlockPos, BlockTemperatureData>> TEMPERATURE_MAP;

    public static void generateMap() {

    }

    public static void addBlock(ServerLevel level, BlockPos pos) {
        Map<BlockPos, BlockTemperatureData> map = TEMPERATURE_MAP.get(level);
        if(map.containsKey(pos)) {
            Metallurgica.LOGGER.error("TEMPERATURE SYSTEM: why are existing blocks being added???");
        } else {
            // level.getBlockEntity(pos).getBlockState().getBlock(); <- key for table
            //TODO: add table for difusivity based on material
            double temp = TempUtils.getTemperature(pos, level); //TODO: replace with better calcs
            map.put(pos, new BlockTemperatureData(temp, 0.001));
        }
    }

    public static void setBlockTemperature(ServerLevel level, BlockPos pos, double temperature) {
        Map<BlockPos, BlockTemperatureData> map = TEMPERATURE_MAP.get(level);
        if(map.containsKey(pos)) {
            map.get(pos).temperature = temperature;
        } else {
            // level.getBlockEntity(pos).getBlockState().getBlock(); <- key for table
            //TODO: add table for difusivity based on material
            map.put(pos, new BlockTemperatureData(temperature, 0.001));
        }
    }

    public static void load(ServerLevel level, LevelChunk chunk) {
        Map<BlockPos, BlockTemperatureData> map = TEMPERATURE_MAP.get(level);
        for(BlockPos pos : chunk.getBlockEntitiesPos()) {  //TODO: I'm pretty sure this gets all of the block positions with block entities, so switch that out for ALL block positions
            if(map.containsKey(pos)) {
                map.get(pos).isLoaded = true;
            } else {
                // level.getBlockEntity(pos).getBlockState().getBlock(); <- key for table
                //TODO: add table for difusivity based on material
                double temp = TempUtils.getTemperature(pos, level); //TODO: replace with better calcs
                map.put(pos, new BlockTemperatureData(temp, 0.001));
            }
        }
    }

    public static void unload(ServerLevel level, LevelChunk chunk) {
        Map<BlockPos, BlockTemperatureData> map = TEMPERATURE_MAP.get(level);
        for(BlockPos pos : chunk.getBlockEntitiesPos()) {
            if(map.containsKey(pos)) {
                map.get(pos).isLoaded = false;
            } else {
                Metallurgica.LOGGER.error("TEMPERATURE SYSTEM: why are unloaded blocks being unloaded???");
            }
        }
    }

    public static void tick(ServerLevel level) {
        Map<BlockPos, BlockTemperatureData> levelMap = TEMPERATURE_MAP.get(level);

        for(BlockPos pos : levelMap.keySet()) {
            BlockTemperatureData data = levelMap.get(pos);
            if (data.isLoaded) {
                double temperature = data.temperature;
                double diffusivity = data.diffusivity;
                double sum = 0;

                // general formula for FDM heat transfer: original_temp + dt * diffusivity * dx * (sum(surrounding_diffs) - #_diffs * original_temp)
                // this simplifies to: original_temp + 0.05 * diffusivity * (sum(surrounding_diffs) - #_of_diffs * original_temp)
                // since all ticks happen every 20th of a second (or 0.05 seconds) and all blocks are 1 block apart

                if(levelMap.containsKey(pos.above())) {
                    sum += levelMap.get(pos.above()).temperature - temperature;
                }
                if(levelMap.containsKey(pos.below())) {
                    sum += levelMap.get(pos.below()).temperature - temperature;
                }

                //have to check if horizontal blocks are loaded cus chunks
                if(levelMap.containsKey(pos.east()) && levelMap.get(pos.east()).isLoaded) {
                    sum += levelMap.get(pos.east()).temperature - temperature;
                }
                if(levelMap.containsKey(pos.west()) && levelMap.get(pos.west()).isLoaded) {
                    sum += levelMap.get(pos.west()).temperature - temperature;
                }
                if(levelMap.containsKey(pos.south()) && levelMap.get(pos.south()).isLoaded) {
                    sum += levelMap.get(pos.south()).temperature - temperature;
                }
                if(levelMap.containsKey(pos.north()) && levelMap.get(pos.north()).isLoaded) {
                    sum += levelMap.get(pos.north()).temperature - temperature;
                }

                levelMap.get(pos).temperature += temperature + (0.05F * diffusivity * sum);
            }
        }
    }

    public static class BlockTemperatureData {
        boolean isLoaded;
        double temperature;
        double diffusivity;

        public BlockTemperatureData(double v, double d) {
            isLoaded = true;
            temperature = v;
            diffusivity = d;
        }

        public void setLoaded() {
            this.isLoaded = true;
        }

        public void setUnloaded() {
            this.isLoaded = false;
        }

        public double getTemperature() {
            return temperature;
        }
    }
}
