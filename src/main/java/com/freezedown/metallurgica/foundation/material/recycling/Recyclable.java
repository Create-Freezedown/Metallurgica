package com.freezedown.metallurgica.foundation.material.recycling;

import com.freezedown.metallurgica.foundation.material.registry.Material;
import net.createmod.catnip.data.Pair;
import net.minecraft.world.level.ItemLike;
import org.jgrapht.alg.util.Triple;

import java.util.Map;

public interface Recyclable {

    Map<Material, Integer> recyclesInto(Material mainMaterial);
    Map<Material, Pair<Integer, Float>> discardChance(Material mainMaterial);
    Map<ItemLike, Pair<Integer, Float>> extraItems(Material mainMaterial);
}
