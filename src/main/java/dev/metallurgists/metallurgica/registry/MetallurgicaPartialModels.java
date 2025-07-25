package dev.metallurgists.metallurgica.registry;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class MetallurgicaPartialModels {
    public static final PartialModel

            channelSide = block("channel/block_side"),
            shakerPlatform = block("shaking_table/platform"),
            ceramicMixerStirrer = block("ceramic_mixer/stirrer"),
            workedMetal = block("metalworking/worked_metal"),
            smallNozzle = block("nozzle/small"),
            mediumNozzle = block("nozzle/medium"),
            largeNozzle = block("nozzle/large")
            ;
    
    private static PartialModel block(String path) {
        return PartialModel.of(Metallurgica.asResource("block/" + path));
    }
    
    public static void init() {
        // init static fields
    }
}
