package com.freezedown.metallurgica.content.fluids.types.open_ended_pipe;

import com.freezedown.metallurgica.registry.MetallurgicaFluids;
import com.freezedown.metallurgica.registry.MetallurgicaTags;
import com.simibubi.create.api.effect.OpenPipeEffectHandler;
import com.simibubi.create.api.registry.SimpleRegistry;

public class OpenEndedPipeEffects {
    
    public static void init() {
        OpenPipeEffectHandler.REGISTRY.registerProvider(SimpleRegistry.Provider.forFluidTag(MetallurgicaTags.AllFluidTags.MOLTEN_METAL.tag, new MoltenMetalHandler()));
        OpenPipeEffectHandler.REGISTRY.register(MetallurgicaFluids.hydrochloricAcid.getSource(), new AcidHandler());
        OpenPipeEffectHandler.REGISTRY.register(MetallurgicaFluids.sulfuricAcid.getSource(), new AcidHandler());
        OpenPipeEffectHandler.REGISTRY.register(MetallurgicaFluids.sodiumHydroxide.getSource(), new AcidHandler());
        OpenPipeEffectHandler.REGISTRY.register(MetallurgicaFluids.sodiumHypochlorite.getSource(), new AcidHandler());
    }
    
}
