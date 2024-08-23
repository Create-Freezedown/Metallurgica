package com.freezedown.metallurgica.content.fluids.types.open_ended_pipe;

import com.freezedown.metallurgica.content.fluids.types.Acid;
import com.freezedown.metallurgica.registry.MetallurgicaFluids;
import com.simibubi.create.content.fluids.OpenEndedPipe;

public class OpenEndedPipeEffects {
    
    public static void init() {
        OpenEndedPipe.registerEffectHandler(new MoltenMetalEffect());
        OpenEndedPipe.registerEffectHandler(new AcidEffect(MetallurgicaFluids.hydrochloricAcid.get()));
        OpenEndedPipe.registerEffectHandler(new AcidEffect(MetallurgicaFluids.sulfuricAcid.get()));
        OpenEndedPipe.registerEffectHandler(new AcidEffect(MetallurgicaFluids.sodiumHydroxide.get()));
        OpenEndedPipe.registerEffectHandler(new AcidEffect(MetallurgicaFluids.sodiumHypochlorite.get()));
    }
    
}
