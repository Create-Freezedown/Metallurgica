package com.freezedown.metallurgica.content.primitive.log_pile;

import com.freezedown.metallurgica.content.primitive.log_pile.charred_pile.CharredLogPileBlockEntity;
import net.createmod.catnip.config.ConfigBase;

public class LogPileConfig extends ConfigBase {
    
    public final ConfigInt logPileBurnTime = i(12000, "logPileBurnTime", Comments.logPileBurnTime);
    public final ConfigInt logPileDestructionTime = i(600, "logPileDestructionTime", Comments.logPileDestructionTime);
    public final ConfigEnum<CharredLogPileBlockEntity.AirExposure> airExposure = e(CharredLogPileBlockEntity.AirExposure.TAG_SPECIFIC, "airExposure", Comments.airExposure);
    @Override
    public String getName() {
        return "Configure Log Pile";
    }
    
    private static class Comments {
        static String logPileBurnTime = "The burn time of a log pile, in ticks";
        static String logPileDestructionTime = "The time it takes for a charred log pile to be destroyed if it is exposed to air, in ticks";
        static String[] airExposure = new String[]{"What blocks aren't considered air for the purpose of determining if a charred log pile is exposed to air", "Using ALL BLOCKS will make any full block not count as air", "Using TAG_SPECIFIC will make only blocks with the tag 'metallurgica:air_blocking' not count as air"};
    }
}
