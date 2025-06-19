package com.freezedown.metallurgica.infastructure.material.registry.flags.block;

import com.freezedown.metallurgica.infastructure.material.Material;
import com.google.gson.JsonElement;

public interface ISpecialAssetGen {

    void generateAssets(Material material);
}
