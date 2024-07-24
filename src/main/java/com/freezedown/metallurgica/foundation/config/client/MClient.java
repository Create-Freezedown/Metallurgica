package com.freezedown.metallurgica.foundation.config.client;

import com.simibubi.create.foundation.config.ConfigBase;

public class MClient extends ConfigBase {
    
    public final ConfigBool renderExcavationParticles = b(true, "renderExcavationParticles", Comments.renderExcavationParticles);
    public final ConfigBool appendTextToItems = b(true, "appendTextToItems", Comments.appendTextToItems);
    @Override
    public String getName() {
        return "client";
    }
    
    private static class Comments {
        static String renderExcavationParticles = "Whether or not to render particles when excavating from deposits";
        static String appendTextToItems = "Whether or not items like alloys should have extra information appended to their display name";
    }
}
