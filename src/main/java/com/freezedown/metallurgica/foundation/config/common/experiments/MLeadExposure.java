package com.freezedown.metallurgica.foundation.config.common.experiments;

import com.simibubi.create.foundation.config.ConfigBase;

public class MLeadExposure extends ConfigBase {
    
    public final ConfigBool enableLeadExposure = b(true, "enableLeadExposure", Comments.enableLeadExposure);
    public final ConfigFloat leadExposureRange = f(5, 1, 64, "leadExposureRange", Comments.leadExposureRange);
    public final ConfigInt stageOneMin = i(72000, 1, 2147483647, "stageOneMin", Comments.stageOneMin);
    public final ConfigInt stageTwoMin = i(180000, 1, 2147483647, "stageTwoMin", Comments.stageTwoMin);
    public final ConfigInt stageThreeMin = i(288000, 1, 2147483647, "stageThreeMin", Comments.stageThreeMin);
    public final ConfigInt stageFourMin = i(432000, 1, 2147483647, "stageFourMin", Comments.stageFourMin);
    
    
    @Override
    public String getName() {
        return "Metallurgica's Lead Exposure";
    }
    
    private static class Comments {
        static String enableLeadExposure = "Enables or disables the lead exposure mechanic";
        static String leadExposureRange = "The range in which the player can be exposed to lead";
        static String stageOneMin = "The minimum time in ticks before the player reaches stage one of lead exposure";
        static String stageTwoMin = "The minimum time in ticks before the player reaches stage two of lead exposure";
        static String stageThreeMin = "The minimum time in ticks before the player reaches stage three of lead exposure";
        static String stageFourMin = "The minimum time in ticks before the player reaches stage four of lead exposure. At this stage, the counter will no longer increase";
    }
}
