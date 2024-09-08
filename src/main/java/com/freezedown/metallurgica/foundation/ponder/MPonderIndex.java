package com.freezedown.metallurgica.foundation.ponder;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.ponder.scenes.primitive.CeramicScenes;
import com.freezedown.metallurgica.foundation.ponder.scenes.primitive.DrillTowerScenes;
import com.freezedown.metallurgica.foundation.ponder.scenes.primitive.FluidScenes;
import com.freezedown.metallurgica.registry.MetallurgicaBlocks;
import com.freezedown.metallurgica.registry.MetallurgicaItems;
import com.freezedown.metallurgica.registry.MetallurgicaOre;
import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import com.simibubi.create.foundation.ponder.PonderRegistry;

public class MPonderIndex {
    static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(Metallurgica.ID);
    
    public MPonderIndex() {
    }
    
    public static void register() {
        HELPER.forComponents(MetallurgicaBlocks.unfiredCeramicPot, MetallurgicaBlocks.unfiredCrucible, MetallurgicaBlocks.ceramicPot, MetallurgicaBlocks.crucible).addStoryBoard("ceramic_firing", CeramicScenes::ceramic_firing, MPonderTag.PRIMITIVE);
        HELPER.forComponents(MetallurgicaBlocks.drillTower, MetallurgicaBlocks.drillActivator, MetallurgicaBlocks.drillExpansion).addStoryBoard("drill_tower", DrillTowerScenes::drill_tower, MPonderTag.MACHINERY);
        HELPER.forComponents(MetallurgicaBlocks.faucet).addStoryBoard("faucet", FluidScenes::faucet, MPonderTag.MACHINERY);
    }
    
    public static void registerTags() {
        PonderRegistry.TAGS.forTag(MPonderTag.PRIMITIVE).add(MetallurgicaBlocks.unfiredCeramicPot).add(MetallurgicaBlocks.unfiredCrucible).add(MetallurgicaBlocks.ceramicPot).add(MetallurgicaBlocks.crucible);
        PonderRegistry.TAGS.forTag(MPonderTag.MACHINERY).add(MetallurgicaBlocks.electrolyzer).add(MetallurgicaBlocks.shakingTable).add(MetallurgicaBlocks.drillTower).add(MetallurgicaBlocks.drillActivator).add(MetallurgicaBlocks.drillExpansion).add(MetallurgicaBlocks.faucet);
        PonderRegistry.TAGS.forTag(MPonderTag.METALS).add(MetallurgicaItems.bronzeIngot).add(MetallurgicaItems.arsenicalBronzeIngot).add(MetallurgicaItems.titaniumIngot).add(MetallurgicaItems.titaniumAluminideIngot);
        PonderRegistry.TAGS.forTag(MPonderTag.MINERALS).add(MetallurgicaOre.CASSITERITE.MATERIAL.raw());
    }
}
