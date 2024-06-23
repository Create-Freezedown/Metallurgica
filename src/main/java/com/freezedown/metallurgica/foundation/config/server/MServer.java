package com.freezedown.metallurgica.foundation.config.server;

import com.freezedown.metallurgica.foundation.config.server.subcat.MStressConfig;
import com.simibubi.create.foundation.config.ConfigBase;

public class MServer extends ConfigBase {
    
    public final MStressConfig stressValues = this.nested(0, MStressConfig::new, "Fine tune the kinetic stats of individual components");
    
    @Override
    public String getName() {
        return "server";
    }
    
    private static class Comments {
    
    }
}
