package com.freezedown.metallurgica.foundation.config.server;

import com.freezedown.metallurgica.foundation.config.server.subcat.MKinetics;
import com.freezedown.metallurgica.foundation.config.server.subcat.MMachineConfig;
import net.createmod.catnip.config.ConfigBase;

public class MServer extends ConfigBase {
    
    public final MKinetics kinetics = this.nested(0, MKinetics::new, "Parameters and abilities of Metallurgica's kinetic mechanisms");
    public final MMachineConfig machineConfig = this.nested(0, MMachineConfig::new, "Fine tune the behavior of machines");
    
    //public final MMachineConfig machineConfig = this.nested(0, MMachineConfig::new, "Fine tune the behavior of machines");
    @Override
    public String getName() {
        return "server";
    }
    
    private static class Comments {
    
    }
}
