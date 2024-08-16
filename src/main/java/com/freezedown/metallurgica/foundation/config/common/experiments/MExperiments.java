package com.freezedown.metallurgica.foundation.config.common.experiments;

import com.simibubi.create.foundation.config.ConfigBase;

public class MExperiments extends ConfigBase {
    
    public final MLeadExposure leadExposure = nested(0, MLeadExposure::new, Comments.leadExposure);
    
    @Override
    public String getName() {
        return "Metallurgica's Experimental Features";
    }
    
    private static class Comments {
        static String leadExposure = "Modify Metallurgica's Lead Exposure mechanic";
    }
}
