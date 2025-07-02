package com.freezedown.metallurgica.infastructure.material.scrapping;

import com.freezedown.metallurgica.infastructure.material.Material;
import net.createmod.catnip.data.Pair;
import net.minecraft.world.level.ItemLike;

import java.util.Map;

public interface Scrappable {

    Map<Material, Integer> scrapsInto(Material mainMaterial);
    Map<Material, Pair<Integer, Float>> discardChance(Material mainMaterial);
    Map<ItemLike, Pair<Integer, Float>> extraItems(Material mainMaterial);
}
