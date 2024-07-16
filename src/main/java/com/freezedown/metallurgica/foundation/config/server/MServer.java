package com.freezedown.metallurgica.foundation.config.server;

import com.freezedown.metallurgica.foundation.config.server.subcat.MMachineConfig;
import com.freezedown.metallurgica.foundation.config.server.subcat.MStressConfig;
import com.simibubi.create.foundation.config.ConfigBase;

public class MServer extends ConfigBase {
    
    public final MStressConfig stressValues = this.nested(0, MStressConfig::new, "Fine tune the kinetic stats of individual components");
    public final MMachineConfig machineConfig = this.nested(0, MMachineConfig::new, "Fine tune the behavior of machines");
    //public final MMachineConfig machineConfig = this.nested(0, MMachineConfig::new, "Fine tune the behavior of machines");
    @Override
    public String getName() {
        return "server";
    }
    
    private static class Comments {
    
    }
}
