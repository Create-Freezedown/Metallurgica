package com.freezedown.metallurgica.foundation.config.common.subcat;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.world.MetallurgicaOreFeatureConfigEntries;
import com.simibubi.create.foundation.config.ConfigBase;
import net.minecraftforge.common.ForgeConfigSpec;

public class MWorldGen extends ConfigBase {
    
    /**
     * Increment this number if all worldgen config entries should be overwritten
     * in this update. Worlds from the previous version will overwrite potentially
     * changed values with the new defaults.
     */
    public static final int FORCED_UPDATE_VERSION = 2;
    
    public final ConfigBool disable = b(false, "disableWorldGen", Comments.disable);
    
    @Override
    public void registerAll(ForgeConfigSpec.Builder builder) {
        super.registerAll(builder);
        MetallurgicaOreFeatureConfigEntries.fillConfig(builder, Metallurgica.ID);
    }
    
    @Override
    public String getName() {
        return "worldgen.v" + FORCED_UPDATE_VERSION;
    }
    
    private static class Comments {
        static String disable = "Prevents all worldgen added by Metallurgica from taking effect";
        static String magnetite = "Modify magnetite ore generation";
        static String nativeCopper = "Modify native copper ore generation";
        static String bauxite = "Modify bauxite ore generation";
        static String nativeGold = "Modify native gold ore generation";
    }
}
