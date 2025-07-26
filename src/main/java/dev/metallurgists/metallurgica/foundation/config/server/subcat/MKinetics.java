package dev.metallurgists.metallurgica.foundation.config.server.subcat;


import net.createmod.catnip.config.ConfigBase;

public class MKinetics extends ConfigBase {
    public final MStress stressValues;
    
    public MKinetics() {
        this.stressValues = this.nested(1, MStress::new, Comments.stress);
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
