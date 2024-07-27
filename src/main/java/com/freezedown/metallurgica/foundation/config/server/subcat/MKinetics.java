package com.freezedown.metallurgica.foundation.config.server.subcat;

import com.simibubi.create.foundation.config.ConfigBase;

public class MKinetics extends ConfigBase {
    public final MStressConfig stressValues;
    
    public MKinetics() {
        this.stressValues = (MStressConfig)this.nested(1, MStressConfig::new, new String[]{MKinetics.Comments.stress});
    }
    
    public String getName() {
        return "kinetics";
    }
    
    private static class Comments {
        static String stress = "Fine tune the kinetic stats of individual components";
        
        private Comments() {
        }
    }
}
