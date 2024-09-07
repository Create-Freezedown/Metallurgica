package com.freezedown.metallurgica.foundation.ponder;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.ponder.scenes.primitive.CeramicScenes;
import com.freezedown.metallurgica.registry.MetallurgicaBlocks;
import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import com.simibubi.create.foundation.ponder.PonderRegistry;

public class MPonderIndex {
    static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(Metallurgica.ID);
    
    public MPonderIndex() {
    }
    
    public static void register() {
        HELPER.forComponents(MetallurgicaBlocks.unfiredCeramicPot, MetallurgicaBlocks.unfiredCrucible, MetallurgicaBlocks.ceramicPot, MetallurgicaBlocks.crucible).addStoryBoard("ceramic_firing", CeramicScenes::ceramic_firing, MPonderTag.PRIMITIVE);
    }
    
    public static void registerTags() {
        PonderRegistry.TAGS.forTag(MPonderTag.PRIMITIVE).add(MetallurgicaBlocks.unfiredCeramicPot).add(MetallurgicaBlocks.unfiredCrucible).add(MetallurgicaBlocks.ceramicPot).add(MetallurgicaBlocks.crucible);
    }
}
