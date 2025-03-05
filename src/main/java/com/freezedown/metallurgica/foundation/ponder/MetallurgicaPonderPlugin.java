package com.freezedown.metallurgica.foundation.ponder;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.ponder.scenes.MetallurgicaPonderScenes;
import net.createmod.ponder.api.level.PonderLevel;
import net.createmod.ponder.api.registration.*;
import net.minecraft.resources.ResourceLocation;

public class MetallurgicaPonderPlugin implements PonderPlugin {

    @Override
    public String getModId() {
        return Metallurgica.ID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        MetallurgicaPonderScenes.register(helper);
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        MetallurgicaPonderTags.register(helper);
    }

    @Override
    public void registerSharedText(SharedTextRegistrationHelper helper) {

    }

    @Override
    public void onPonderLevelRestore(PonderLevel ponderLevel) {

    }

    @Override
    public void indexExclusions(IndexExclusionHelper helper) {

    }
}
