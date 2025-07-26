package dev.metallurgists.metallurgica.foundation.config.common.experiments;

import net.createmod.catnip.config.ConfigBase;

public class MWorkplaceHazards extends ConfigBase {

    public final ConfigBool mechanicalPressCrushing = b(false, "Mechanical Press Crushing", Comments.mechanicalPressCrushing);
    @Override
    public String getName() {
        return "Configure Workplace Hazards";
    }

    private static class Comments {
        static String mechanicalPressCrushing = "Should Mechanical Presses crush entities beneath them.";
    }
}
