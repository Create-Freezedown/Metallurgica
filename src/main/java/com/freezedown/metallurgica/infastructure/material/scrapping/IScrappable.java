package com.freezedown.metallurgica.infastructure.material.scrapping;

import com.freezedown.metallurgica.infastructure.material.Material;
import net.createmod.catnip.data.Pair;
import net.minecraft.world.level.ItemLike;

import java.util.Map;

public interface IScrappable {

    ScrappingData getScrappingData(Material mainMaterial);
}
