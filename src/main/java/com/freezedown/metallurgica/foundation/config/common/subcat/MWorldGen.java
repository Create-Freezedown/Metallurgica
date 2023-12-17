package com.freezedown.metallurgica.foundation.config.common.subcat;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.world.MetallurgicaOreFeatureConfigEntries;
import com.simibubi.create.foundation.config.ConfigBase;
import net.minecraftforge.common.ForgeConfigSpec;

public class MWorldGen extends ConfigBase {
    
    /**
     * Increment this number if all worldgen config entries should be overwritten
     * in this update. Worlds from the previous version will overwrite potentially
     * changed values with the new defaults.
     */
    public static final int FORCED_UPDATE_VERSION = 2;
    
    public final ConfigBool disable = b(false, "disableWorldGen", MWorldGen.Comments.disable);
    
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
    }
}
