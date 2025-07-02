package com.freezedown.metallurgica.foundation.temperature.server;

import com.freezedown.metallurgica.Metallurgica;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;

import java.util.ArrayList;
import java.util.List;

public class TemperatureMap {
    private final List<ArrayList<TemperatureChunk>> ppChunks;
    private final List<ArrayList<TemperatureChunk>> pnChunks;
    private final List<ArrayList<TemperatureChunk>> npChunks;
    private final List<ArrayList<TemperatureChunk>> nnChunks;

    ServerLevel level;

    public TemperatureMap(ServerLevel l) {
        level = l;

        ppChunks = new ArrayList<>(100);
        pnChunks = new ArrayList<>(100);
        npChunks = new ArrayList<>(100);
        nnChunks = new ArrayList<>(100);

        for (int i = 0; i < 100; i++) {
            ppChunks.add(new ArrayList<>(100));
            pnChunks.add(new ArrayList<>(100));
            npChunks.add(new ArrayList<>(100));
            nnChunks.add(new ArrayList<>(100));

            for (int j = 0; j < 100; j++) {
                ppChunks.get(i).add(null);
                pnChunks.get(i).add(null);
                npChunks.get(i).add(null);
                nnChunks.get(i).add(null);
            }
        }
    }

    public TemperatureChunk getChunk(ChunkPos pos) {
        int x = pos.x;
        int z = pos.z;
        if(x < 0) {
            x = -x;
            if(z < 0) { // nn
                z = -z;
                return getChunkUnsafe(nnChunks, x, z);
            } else {    // np
                return getChunkUnsafe(npChunks, x, z);
            }
        } else {
            if(z < 0) { //pn
                z = -z;
                return getChunkUnsafe(pnChunks, x, z);
            } else {    //pp
                return getChunkUnsafe(ppChunks, x, z);
            }
        }
    }

    public TemperatureChunk getChunkUnsafe(List<ArrayList<TemperatureChunk>> chunks, int x, int z) {
        if(chunks.get(x).get(z) == null) {
            TemperatureChunk newChunk = new TemperatureChunk(this.level);
            newChunk.generate(this.level.getChunk(x, z));
            chunks.get(x).set(z, newChunk);
            return newChunk;
        }
        return chunks.get(x).get(z);
    }

    public BlockTemperatureData getBlock(int x, int y, int z) {
        TemperatureChunk chunk;
        if(x < 0) {
            x = -x;
            if(z < 0) { // nn
                z = -z;
                chunk = getChunkUnsafe(nnChunks, Math.floorDiv(x, 16), Math.floorDiv(z, 16));
            } else {    // np
                chunk = getChunkUnsafe(npChunks, Math.floorDiv(x, 16), Math.floorDiv(z, 16));
            }
        } else {
            if(z < 0) { //pn
                z = -z;
                chunk = getChunkUnsafe(pnChunks, Math.floorDiv(x, 16), Math.floorDiv(z, 16));
            } else {    //pp
                chunk = getChunkUnsafe(ppChunks, Math.floorDiv(x, 16), Math.floorDiv(z, 16));
            }
        }
        return chunk.get(x, y, z);
    }

    public BlockTemperatureData getBlock(BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        return getBlock(x, y, z);
    }

    public void generateChunk(ChunkPos pos) {
        TemperatureChunk tChunk = getChunk(pos);
        Metallurgica.LOGGER.info("got temp chunk");
        LevelChunk lChunk = level.getChunk(pos.x, pos.z);
        Metallurgica.LOGGER.info("got level chunk");
        tChunk.generate(lChunk);
    }

    public static class TemperatureChunk {
        //                  y x z
        BlockTemperatureData[][][] data;
        int min;
        int max;

        TemperatureChunk(ServerLevel level) {
            min = -level.getMinBuildHeight();
            max = level.getMaxBuildHeight();
            int height = max + min;

            data = new BlockTemperatureData[height][16][16];
        }

        public void generate(LevelChunk chunk) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 0; y < max + min; y++) {
                        int s = (y >> 4) - (chunk.getMinBuildHeight() >> 4);

                        LevelChunkSection[] sections = chunk.getSections();

                        if(s < 0) continue;
                        if(s >= sections.length) continue;

                        LevelChunkSection levelchunksection = sections[s];

                        if(levelchunksection.hasOnlyAir()) continue;
                        if(levelchunksection.getBlockState(x, y & 15, z).isAir()) continue;

                        data[y][x][z] = new BlockTemperatureData(0.0, 1.0);
                    }
                }
            }
            Metallurgica.LOGGER.info("generated");
        }

        public BlockTemperatureData get(int x, int y, int z) {
            return data[y + min][x % 16][z % 16];
        }

    }
}
