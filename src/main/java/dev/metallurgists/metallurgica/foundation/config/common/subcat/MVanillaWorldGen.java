package dev.metallurgists.metallurgica.foundation.config.common.subcat;


import net.createmod.catnip.config.ConfigBase;

public class MVanillaWorldGen extends ConfigBase {
    public static final int FORCED_UPDATE_VERSION = 1;
    
    public final ConfigBool disableDefaultOres = b(true, "disableDefaultOres", Comments.disableDefaultOres);
    public final ConfigBool disableExtraGold = b(true, "disableExtraGold", Comments.disableExtraGold);
    public final ConfigBool disableNetherDefaultOres = b(true, "disableNetherDefaultOres", Comments.disableNetherDefaultOres);
    public final ConfigBool disableDefaultCrystalFormations = b(true, "disableDefaultCrystalFormations", Comments.disableDefaultCrystalFormations);
    
    
    @Override
    public String getName() {
        return "vanilla_worldgen_modifications.v" + FORCED_UPDATE_VERSION;
    }
    
    private static class Comments {
        static String disableDefaultOres = "Disables the default ore generation of Minecraft. E.g. Iron Ore";
        static String disableExtraGold = "Disables the extra gold generation of Minecraft. E.g. Gold Ore in Badlands";
        static String disableNetherDefaultOres = "Disables the default nether ore generation of Minecraft. E.g. Nether Quartz Ore";
        static String disableDefaultCrystalFormations = "Disables the default crystal formations of Minecraft. E.g. Amethyst Geodes";
    }
}
