package com.freezedown.metallurgica.foundation.temperature.server;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.temperature.TempUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.SERVER)
public class TemperatureHandler {
    //                   level           pos            data
    public static Map<ServerLevel, TemperatureHandler> TEMPERATURE_MAP;

    private final Map<BlockPos, BlockTemperatureData> map;
    private final ServerLevel level;

    public TemperatureHandler(ServerLevel l) {
        level = l;
        map = new HashMap<>();
    }

    public static TemperatureHandler getHandler(ServerLevel level) {
        if (!TEMPERATURE_MAP.containsKey(level)) {
            Metallurgica.LOGGER.error("TEMPERATURE SYSTEM: something mischievous this way comes");
            TEMPERATURE_MAP.put(level, new TemperatureHandler(level));
        }

        if (TEMPERATURE_MAP.get(level) == null) {
            Metallurgica.LOGGER.error("TEMPERATURE SYSTEM: something fucked this way comes");
            TEMPERATURE_MAP.replace(level, new TemperatureHandler(level));
        }

        return TEMPERATURE_MAP.get(level);
    }


    public static void generateMap(MinecraftServer server) {
        Metallurgica.LOGGER.info("TEMPERATURE SYSTEM: generated");
        TEMPERATURE_MAP = new HashMap<>();
        server.getAllLevels().forEach(level -> TEMPERATURE_MAP.put(level, new TemperatureHandler(level)));
    }

    public void addBlock(BlockPos pos) {
        // level.getBlockEntity(pos).getBlockState().getBlock(); <- key for table
        //TODO: add table for difusivity based on material
        double temp = TempUtils.getTemperature(pos, level); //TODO: replace with better calcs
        BlockTemperatureData data = new BlockTemperatureData(temp, 1.0);
        map.put(pos, data);
        Metallurgica.LOGGER.info("TEMPERATURE SYSTEM: added block");
    }

    public void setBlockTemperature(BlockPos pos, double temperature) {
        if(map.containsKey(pos)) {
            map.get(pos).temperature = temperature;
        } else {
            // level.getBlockEntity(pos).getBlockState().getBlock(); <- key for table
            //TODO: add table for difusivity based on material
            map.put(pos, new BlockTemperatureData(temperature, 1.0));
        }
    }

    public double getBlockTemperature(BlockPos pos) {
        if(!map.containsKey(pos)) {
//            addBlock(pos);1
            Metallurgica.LOGGER.error("TEMPERATURE SYSTEM: FUCK!");
            return 0.0;
        }
        return map.get(pos).getTemperature();
    }

    public void load(LevelChunk chunk) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = chunk.getMinBuildHeight(); y < chunk.getMaxBuildHeight(); y++) {
                    int s = chunk.getSectionIndex(y);

                    LevelChunkSection[] sections = chunk.getSections();

                    if(s <= 0 || s > sections.length) continue;

                    LevelChunkSection levelchunksection = sections[s];

                    if (levelchunksection.hasOnlyAir() || levelchunksection.getBlockState(x, y & 15, z).isAir()) continue;

                    BlockPos pos = chunk.getPos().getBlockAt(x, y, z);

                    if(map.containsKey(pos)) {
                        map.get(pos).isLoaded = true;
                    } else {
                        // level.getBlockEntity(pos).getBlockState().getBlock(); <- key for table
                        //TODO: add table for difusivity based on material
                        double temp = TempUtils.getTemperature(pos, level); //TODO: replace with better calcs
                        map.put(pos, new BlockTemperatureData(temp, 1.0));
                    }
                }
            }
        }
    }

    public void unload(LevelChunk chunk) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = chunk.getMaxBuildHeight(); y < chunk.getMinBuildHeight(); y++) {
                    int s = chunk.getSectionIndex(y);

                    LevelChunkSection[] sections = chunk.getSections();

                    if(s <= 0 || s > sections.length) continue;

                    LevelChunkSection levelchunksection = sections[s];

                    if (levelchunksection.hasOnlyAir() || levelchunksection.getBlockState(x, y & 15, z).isAir()) continue;

                    BlockPos pos = chunk.getPos().getBlockAt(x, y, z);

                    if(map.containsKey(pos)) {
                        map.get(pos).isLoaded = false;
                    } else {
                        Metallurgica.LOGGER.error("TEMPERATURE SYSTEM: why are unloaded blocks being unloaded???");
                    }
                }
            }
        }
    }

    public void tick() {
        for(BlockPos pos : map.keySet()) {
            BlockTemperatureData data = map.get(pos);
            if (data.isLoaded) {
                double temperature = data.temperature;
                double diffusivity = data.diffusivity;
                double sum = 0;

                // general formula for FDM heat transfer: original_temp + dt * diffusivity * dx * (sum(surrounding_diffs) - #_diffs * original_temp)
                // this simplifies to: original_temp + 0.05 * diffusivity * (sum(surrounding_diffs) - #_of_diffs * original_temp)
                // since all ticks happen every 20th of a second (or 0.05 seconds) and all blocks are 1 block apart

                if(map.containsKey(pos.above())) {
                    sum += map.get(pos.above()).temperature - temperature;
                }
                if(map.containsKey(pos.below())) {
                    sum += map.get(pos.below()).temperature - temperature;
                }

                //have to check if horizontal blocks are loaded cus chunks
                if(map.containsKey(pos.east()) && map.get(pos.east()).isLoaded) {
                    sum += map.get(pos.east()).temperature - temperature;
                }
                if(map.containsKey(pos.west()) && map.get(pos.west()).isLoaded) {
                    sum += map.get(pos.west()).temperature - temperature;
                }
                if(map.containsKey(pos.south()) && map.get(pos.south()).isLoaded) {
                    sum += map.get(pos.south()).temperature - temperature;
                }
                if(map.containsKey(pos.north()) && map.get(pos.north()).isLoaded) {
                    sum += map.get(pos.north()).temperature - temperature;
                }

                map.get(pos).temperature += 0.05F * diffusivity * sum;
            }
        }
    }

    private static class BlockTemperatureData {
        private boolean isLoaded;
        private double temperature;
        private final double diffusivity;

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
