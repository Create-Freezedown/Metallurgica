package dev.metallurgists.metallurgica.experimental.exposure_effects;

import dev.metallurgists.metallurgica.registry.MetallurgicaTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public enum ExposureMinerals {
    LEAD()
    ;
    
    ExposureMinerals() {
    }
    
    public static boolean isExposureMineral(String name) {
        for (ExposureMinerals mineral : values()) {
            if (mineral.name().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
    
    public String getTimerName() {
        return name().toLowerCase() + "ExposureTimer";
    }
    
    public TagKey<Block> getBlockTag() {
        return MetallurgicaTags.modBlockTag("causes_" + name().toLowerCase() + "_exposure");
    }
    
    public TagKey<Item> getItemTag() {
        return MetallurgicaTags.modItemTag("causes_" + name().toLowerCase() + "_exposure");
    }
}
