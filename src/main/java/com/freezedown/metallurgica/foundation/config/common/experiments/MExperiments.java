package com.freezedown.metallurgica.foundation.config.common.experiments;


import net.createmod.catnip.config.ConfigBase;

public class MExperiments extends ConfigBase {
    
    public final MLeadExposure leadExposure = nested(0, MLeadExposure::new, Comments.leadExposure);
    public final MChemicalBurns chemicalBurns = nested(0, MChemicalBurns::new, Comments.chemicalBurns);

    @Override
    public String getName() {
        return "Metallurgica's Experimental Features";
    }
    
    private static class Comments {
        static String leadExposure = "Modify Metallurgica's Lead Exposure mechanic";
        static String chemicalBurns = "Modify Metallurgica's Chemical Burn mechanic";
    }
}
