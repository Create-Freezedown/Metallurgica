package dev.metallurgists.metallurgica.foundation.config.common;

import dev.metallurgists.metallurgica.foundation.config.common.dev.DevConfig;
import dev.metallurgists.metallurgica.foundation.config.common.experiments.MExperiments;
import dev.metallurgists.metallurgica.foundation.config.common.subcat.MVanillaWorldGen;
import dev.metallurgists.metallurgica.foundation.config.common.subcat.MWorldGen;
import net.createmod.catnip.config.ConfigBase;

public class MCommon extends ConfigBase {
    
    public final MWorldGen worldGen = nested(0, MWorldGen::new, MCommon.Comments.worldGen);
    public final MVanillaWorldGen vanillaWorldGen = nested(0, MVanillaWorldGen::new, MCommon.Comments.vanillaWorldGen);
    public final MExperiments experiments = nested(0, MExperiments::new, Comments.experiments);
    public final DevConfig dev = nested(0, DevConfig::new, "Developer Config");
    
    @Override
    public String getName() {
        return "common";
    }
    
    private static class Comments {
        static String worldGen = "Modify Metallurgica's impact on your terrain";
        static String vanillaWorldGen = "Modify Metallurgica's impact on Minecraft's ore generation";
        static String surfaceDeposits = "Modify the rarity of surface deposits";
        static String experiments = "Modify Metallurgica's experimental features";
    }
}
