package com.freezedown.metallurgica.foundation.ponder.scenes;

import com.freezedown.metallurgica.foundation.ponder.MetallurgicaPonderTags;
import com.freezedown.metallurgica.foundation.ponder.scenes.primitive.CeramicScenes;
import com.freezedown.metallurgica.foundation.ponder.scenes.primitive.DrillTowerScenes;
import com.freezedown.metallurgica.foundation.ponder.scenes.primitive.FluidScenes;
import com.freezedown.metallurgica.registry.MetallurgicaBlocks;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class MetallurgicaPonderScenes {

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.forComponents(MetallurgicaBlocks.unfiredCeramicPot, MetallurgicaBlocks.unfiredCrucible, MetallurgicaBlocks.ceramicPot, MetallurgicaBlocks.crucible)
                .addStoryBoard("ceramic_firing", CeramicScenes::ceramic_firing, MetallurgicaPonderTags.PRIMITIVE);

        HELPER.forComponents(MetallurgicaBlocks.faucet)
                .addStoryBoard("faucet", FluidScenes::faucet, MetallurgicaPonderTags.METALS);

        HELPER.forComponents(MetallurgicaBlocks.drillTower, MetallurgicaBlocks.drillActivator, MetallurgicaBlocks.drillExpansion)
                .addStoryBoard("drill_tower", DrillTowerScenes::drill_tower, MetallurgicaPonderTags.MACHINERY);
    }
}
