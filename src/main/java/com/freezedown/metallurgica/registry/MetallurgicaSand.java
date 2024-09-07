
package com.freezedown.metallurgica.registry;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.material.MaterialEntry;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.foundation.worldgen.config.MSandFeatureConfigEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;


public enum MetallurgicaSand {
    BLACK_SAND(false),
    RED_SAND(true),
    QUARTZ_SAND(false)
    ;

    public MSandFeatureConfigEntry CLUSTER;
    public final MaterialEntry MATERIAL;


    private static Block getBlock(ResourceLocation id) {
        return ForgeRegistries.BLOCKS.getValue(id);
    }

    MetallurgicaSand(boolean existing) {
        if (existing != true) {
            MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.materialItemGroup);
            MATERIAL = registrate.material(this.name().toLowerCase(), false);
        }
    }
    MetallurgicaSand(boolean existing, int clusterSize, int clusterFrequency, int clusterMinHeight, int clusterMaxHeight,) {
        if (existing != true) {
            MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.materialItemGroup);
            MATERIAL = registrate.material(this.name().toLowerCase(), false);


        }

    }
}
