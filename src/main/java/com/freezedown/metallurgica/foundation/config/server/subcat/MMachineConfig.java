package com.freezedown.metallurgica.foundation.config.server.subcat;

import com.simibubi.create.foundation.config.ConfigBase;

public class MMachineConfig extends ConfigBase {
    
    @Override
    public String getName() {
        return "machineConfig";
    }
    
    public final ConfigInt reverbaratoryPrimaryOutputCapacity = i(8000, "reverbaratoryPrimaryOutputCapacity", Comments.reverbaratoryPrimaryOutputCapacity);
    public final ConfigInt reverbaratorySlagOutputCapacity = i(8000, "reverbaratorySlagOutputCapacity", Comments.reverbaratorySlagOutputCapacity);
    
    public final ConfigInt genericCarbonDioxideOutputCapacity = i(4000, "genericCarbonDioxideOutputCapacity", Comments.genericCarbonDioxideOutputCapacity);
    
    private static class Comments {
        static String reverbaratoryPrimaryOutputCapacity = "The maximum capacity of the primary output of the reverbaratory";
        static String reverbaratorySlagOutputCapacity = "The maximum capacity of the slag output of the reverbaratory";
        
        static String genericCarbonDioxideOutputCapacity = "The maximum capacity of the carbon dioxide output of a generic multiblock";
    }
}
