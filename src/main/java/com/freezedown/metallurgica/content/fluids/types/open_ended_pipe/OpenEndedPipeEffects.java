package com.freezedown.metallurgica.content.fluids.types.open_ended_pipe;

import com.freezedown.metallurgica.content.fluids.types.Acid;
import com.freezedown.metallurgica.registry.MetallurgicaFluids;
import com.simibubi.create.content.fluids.OpenEndedPipe;

public class OpenEndedPipeEffects {
    
    public static void init() {
        OpenEndedPipe.registerEffectHandler(new MoltenMetalEffect());
        for (Acid acid : MetallurgicaFluids.getAcids()) {
            OpenEndedPipe.registerEffectHandler(new AcidEffect(acid));
        }
    }
    
}
