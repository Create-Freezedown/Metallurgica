/*

package com.freezedown.metallurgica.registry;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.data.custom.composition.Element;
import com.freezedown.metallurgica.foundation.material.MaterialEntry;
import com.freezedown.metallurgica.foundation.material.MetalEntry;
import com.freezedown.metallurgica.registry.MetallurgicaElements;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.foundation.worldgen.config.MOreFeatureConfigEntry;
import com.freezedown.metallurgica.foundation.worldgen.config.MTypedDepositFeatureConfigEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;


public enum MetallurgicaSand {
    BLACK_SAND(),
    RED_SAND(),
    ;

    public MOreFeatureConfigEntry DEPOSIT;
    public MOreFeatureConfigEntry CLUSTER;
    public MTypedDepositFeatureConfigEntry LARGE_DEPOSIT;
    public final MaterialEntry MATERIAL;


    private static Block getBlock(ResourceLocation id) {
        return ForgeRegistries.BLOCKS.getValue(id);
    }

    MetallurgicaSand() {
        MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().creativeModeTab(() -> Metallurgica.materialItemGroup);
        MATERIAL = registrate.material(this.name().toLowerCase(), false),
    }
    }
}*/
