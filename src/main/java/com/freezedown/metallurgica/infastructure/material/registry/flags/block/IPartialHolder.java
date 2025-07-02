package com.freezedown.metallurgica.infastructure.material.registry.flags.block;

import com.freezedown.metallurgica.infastructure.material.Material;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;

public interface IPartialHolder {

    ResourceLocation getModelLocation(Material material);

    JsonElement createModel(Material material);
}
