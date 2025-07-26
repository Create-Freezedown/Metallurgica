package dev.metallurgists.metallurgica.foundation.temperature.server;

import dev.metallurgists.metallurgica.Metallurgica;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@SideOnly(Side.SERVER)
public class TemperatureHandler {
    //                   level           pos            data
    public static Map<ServerLevel, TemperatureHandler> TEMPERATURE_MAP;

    private final TemperatureMap map;
    private final ServerLevel level;
    private final Set<ChunkPos> loaded = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public Set<ChunkPos> getLoadedChunkPositions() {
        return Collections.unmodifiableSet(loaded);
    }

    public void removeLoadedChunk(ChunkPos pos) {
        loaded.add(pos);
    }

    public void addLoadedChunk(ChunkPos pos) {
        loaded.remove(pos);
    }

    public TemperatureHandler(ServerLevel l) {
        level = l;
        map = new TemperatureMap(l);
    }

    public static TemperatureHandler getHandler(ServerLevel level) {
        if (!TEMPERATURE_MAP.containsKey(level)) {
            Metallurgica.LOGGER.error("TEMPERATURE SYSTEM: something mischievous this way comes");
            TEMPERATURE_MAP.put(level, new TemperatureHandler(level));
        }

        return TEMPERATURE_MAP.get(level);
    }

    public static boolean chunkIsGenerated(LevelChunk chunk) {
        return getHandler((ServerLevel) chunk.getLevel()).map.getChunk(chunk.getPos()) != null;
    }

    public static void generateMap(MinecraftServer server) {
        Metallurgica.LOGGER.info("TEMPERATURE SYSTEM: generated");
        TEMPERATURE_MAP = new HashMap<>();
        server.getAllLevels().forEach(level -> TEMPERATURE_MAP.put(level, new TemperatureHandler(level)));
    }

    public static void generateChunk(LevelChunk chunk) {
        Metallurgica.LOGGER.info("generating{}", chunk.getPos());
        TemperatureMap temperatureMap = getHandler((ServerLevel) chunk.getLevel()).map;
        Metallurgica.LOGGER.info("got map");
        temperatureMap.generateChunk(chunk.getPos());
    }

//    public void addBlock(BlockPos pos) {
//        // level.getBlockEntity(pos).getBlockState().getBlock(); <- key for table
//        //TODO: add table for difusivity based on material
//        double temp = TempUtils.getTemperature(pos, level); //TODO: replace with better calcs
//        BlockTemperatureData data = new BlockTemperatureData(temp, 1.0);
//        map.put(pos, data);
//        Metallurgica.LOGGER.info("TEMPERATURE SYSTEM: added block");
//    }

    public void setBlockTemperature(BlockPos pos, double temperature) {
        BlockTemperatureData data = map.getBlock(pos);
        if(data != null) {
            data.setTemperature(temperature);
        }
    }

    public double getBlockTemperature(BlockPos pos) {
        BlockTemperatureData data = map.getBlock(pos);
        if(data == null) {
            return 0.0;
        }
        return data.getTemperature();
    }

    public void tick() {
        for(ChunkPos chunkPos : getLoadedChunkPositions()) {
            TemperatureMap.TemperatureChunk chunk = map.getChunk(chunkPos);
            int y = 0;
            for (BlockTemperatureData[][] temparrarr : chunk.data) {
                int x = 0;
                y++;
                for (BlockTemperatureData[] temparr : temparrarr) {
                    int z = 0;
                    x++;
                    for (BlockTemperatureData data : temparr) {
                        z++;
                        if(data != null) {
                            double temperature = data.getTemperature();
                            double diffusivity = data.getDiffusivity();
                            double sum = 0;

                            // general formula for FDM heat transfer: original_temp + dt * diffusivity * dx * (sum(surrounding_diffs) - #_diffs * original_temp)
                            // this simplifies to: original_temp + 0.05 * diffusivity * (sum(surrounding_diffs) - #_of_diffs * original_temp)
                            // since all ticks happen every 20th of a second (or 0.05 seconds) and all blocks are 1 block apart

                            if(y < chunk.max + chunk.min) {
                                sum += chunk.get(x, y + 1, z).getTemperature() - temperature;
                            }
                            if(y > 0) {
                                sum += chunk.get(x, y - 1, z).getTemperature() - temperature;
                            }

                            //have to check if horizontal blocks are loaded cus chunks
                            if(z < 16) {
                                sum += chunk.get(x, y, z + 1).getTemperature() - temperature;
                            }
//                            else if() {
//
//                            }
                            if(z > 0) {
                                sum += chunk.get(x, y, z - 1).getTemperature() - temperature;
                            }
//                            else if() {
//
//                            }

                            if(x < 16) {
                                sum += chunk.get(x + 1, y, z).getTemperature() - temperature;
                            }
//                            else if() {
//
//                            }
                            if(x > 0) {
                                sum += chunk.get(x - 1, y, z).getTemperature() - temperature;
                            }
//                            else if() {
//
//                            }

                            chunk.get(x, y, z).setTemperature(temperature + (0.05F * diffusivity * sum));
                        }
                    }
                }
            }
        }
    }
}
