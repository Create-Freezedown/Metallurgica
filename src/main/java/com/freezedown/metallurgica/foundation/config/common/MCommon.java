package com.freezedown.metallurgica.foundation.config.common;

import com.freezedown.metallurgica.foundation.config.common.subcat.MWorldGen;
import com.simibubi.create.foundation.config.ConfigBase;

public class MCommon extends ConfigBase {
    
    public final MWorldGen worldGen = nested(0, MWorldGen::new, MCommon.Comments.worldGen);
    
    @Override
    public String getName() {
        return "common";
    }
    
    private static class Comments {
        static String worldGen = "Modify Metallurgica's impact on your terrain";
    }
}
