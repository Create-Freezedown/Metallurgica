package dev.metallurgists.metallurgica.content.mineral.deposit;

import dev.metallurgists.metallurgica.Metallurgica;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepositManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    
    public static Map<BlockState, Deposit> depositType = new HashMap<>();
    public static List<BlockState> deposits = new ArrayList<>();
    
    public DepositManager() {
        super(GSON, "metallurgica_utilities/deposits");
    }
    
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        depositType.clear();
        deposits.clear();
        
        for (Map.Entry<ResourceLocation, JsonElement> entry : resourceLocationJsonElementMap.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();
            
            if (resourceLocation.getPath().startsWith("_")) {
                continue;
            }
            
            try {
                BlockState deposit = getDepositBlockState(resourceLocation);
                Deposit depositProperties = Deposit.CODEC.parse(JsonOps.INSTANCE, entry.getValue()).getOrThrow(true, Metallurgica.LOGGER::error);
                if (depositProperties != null) {
                    depositType.put(deposit, depositProperties);
                    deposits.add(deposit);
                }
            } catch (IllegalArgumentException | JsonParseException jsonParseException) {
                Metallurgica.LOGGER.error("Parsing error loading deposits {}", resourceLocation, jsonParseException);
            }
        }
        Metallurgica.LOGGER.info("Load Complete for {} deposits", deposits.size());
    }
    
    protected static BlockState getDepositBlockState(ResourceLocation resourceLocation) {
        return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(resourceLocation.getNamespace(), removeExtension(resourceLocation).replace(".json", ""))).defaultBlockState();
    }
    
    public static Deposit getDepositProperties(BlockState deposit) {
        return depositType.get(deposit);
    }
    public static Deposit getDepositPropertiesOrNull(BlockState deposit) {
        return depositType.getOrDefault(deposit, null);
    }
    
    public static boolean hasDepositProperties(BlockState deposit) {
        return depositType.containsKey(deposit);
    }
    
    public static List<Deposit> getAllDepositProperties() {
        List<Deposit> properties = new ArrayList<>();
        for (Map.Entry<BlockState, Deposit> entry : DepositManager.depositType.entrySet()) {
            properties.add(entry.getValue());
        }
        return properties;
    }
    
    public static void setProperties(Map<BlockState, Deposit> properties) {
        DepositManager.depositType = properties;
    }
    
    public static String removeExtension(ResourceLocation resourceLocation) {
        String path = resourceLocation.getPath(); // Get the full path from ResourceLocation
        String[] pathElements = path.split("/");
        return pathElements[pathElements.length - 1];
    }
}
