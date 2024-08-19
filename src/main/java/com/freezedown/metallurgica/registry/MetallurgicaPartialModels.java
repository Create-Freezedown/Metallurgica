package com.freezedown.metallurgica.registry;

import com.freezedown.metallurgica.Metallurgica;
import com.jozufozu.flywheel.core.PartialModel;

public class MetallurgicaPartialModels {
    public static final PartialModel

            channelSide = block("channel/block_side"),
            shakerPlatform = block("shaking_table/platform"),
            ceramicMixerStirrer = block("ceramic_mixer/stirrer")
            ;
    
    private static PartialModel block(String path) {
        return new PartialModel(Metallurgica.asResource("block/" + path));
    }
    
    public static void init() {
        // init static fields
    }
}
