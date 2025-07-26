package dev.metallurgists.metallurgica.content.primitive.ceramic;


import net.createmod.catnip.config.ConfigBase;

public class CeramicConfig extends ConfigBase {
    public final ConfigInt ceramicCookTime = i(2400, "ceramicCookTime", Comments.ceramicCookTime);
    public final ConfigBool allowBlazeBurners = b(true, "allowBlazeBurners", Comments.allowBlazeBurners);
    
    @Override
    public String getName() {
        return "Configure Ceramics";
    }
    
    private static class Comments {
        static String ceramicCookTime = "The time it takes for a ceramic to be cooked over a heat source, in ticks";
        static String allowBlazeBurners = "Whether or not blaze burners can be used to cook ceramics";
    }
}
