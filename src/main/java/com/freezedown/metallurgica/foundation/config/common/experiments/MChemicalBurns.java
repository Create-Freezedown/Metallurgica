package com.freezedown.metallurgica.foundation.config.common.experiments;

import com.simibubi.create.foundation.config.ConfigBase;

public class MChemicalBurns extends ConfigBase {
    public final ConfigFloat chemicalBurnHealthReduction = f(1.5f, 0f, 4f, "Chemical Burn Health Reduction", Comments.chemicalBurnHealthReduction);

    @Override
    public String getName() {
        return "Metallurgica's Chemical Burns";
    }
    public  static  class Comments{
        static  String chemicalBurnHealthReduction = "set to 0 to show weakness";
    }
}
