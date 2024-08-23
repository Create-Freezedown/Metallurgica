package com.freezedown.metallurgica.content.temperature;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BlockStateTemperatureManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    
    public static List<Map<Double, BlockState>> blockStateTemperatures = new ArrayList<>();
    public static List<BlockState> blockStates = new ArrayList<>();
    
    public BlockStateTemperatureManager(Gson pGson, String pDirectory) {
        super(pGson, pDirectory);
    }
    
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        blockStateTemperatures.clear();
        blockStates.clear();
        
        for (Map.Entry<ResourceLocation, JsonElement> entry : resourceLocationJsonElementMap.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();
            
            try {
                BlockStateTemperature blockStateTemperature = BlockStateTemperature.CODEC.parse(JsonOps.INSTANCE, entry.getValue()).getOrThrow(true, e -> {
                    throw new IllegalArgumentException("Parsing error loading block state temperatures " + resourceLocation);
                });
                blockStateTemperatures.addAll(blockStateTemperatures(blockStateTemperature.temperatureStateMap()));
                blockStates.addAll(getBlockStates(blockStateTemperatures(blockStateTemperature.temperatureStateMap())));
            } catch (IllegalArgumentException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
    
    public List<BlockState> getBlockStates(List<Map<Double, BlockState>> blockStateTemperatures) {
        List<BlockState> list = new ArrayList<>();
        for (Map<Double, BlockState> map : blockStateTemperatures) {
            for (Map.Entry<Double, BlockState> entry : map.entrySet()) {
                list.add(entry.getValue());
            }
        }
        return list;
    }
    
    public List<Map<Double, BlockState>> blockStateTemperatures(List<Map<BlockStateTemperature.Temperature, BlockStateTemperature.State>> blockStateTemperatures) {
        List<Map<Double, BlockState>> list = new ArrayList<>();
        for (Map<BlockStateTemperature.Temperature, BlockStateTemperature.State> map : blockStateTemperatures) {
            for (Map.Entry<BlockStateTemperature.Temperature, BlockStateTemperature.State> entry : map.entrySet()) {
                list.add(Map.of(entry.getKey().get(), entry.getValue().get()));
            }
        }
        return list;
    }
    
    public boolean hasTemperature(BlockState blockState) {
        return blockStates.contains(blockState);
    }
    
    public double getTemperature(BlockState blockState) {
        for (Map<Double, BlockState> map : blockStateTemperatures) {
            for (Map.Entry<Double, BlockState> entry : map.entrySet()) {
                if (entry.getValue().equals(blockState)) {
                    return entry.getKey();
                }
            }
        }
        return 0;
    }
}
