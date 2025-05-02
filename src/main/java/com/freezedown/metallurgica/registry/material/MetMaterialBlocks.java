package com.freezedown.metallurgica.registry.material;

import com.freezedown.metallurgica.foundation.block.MaterialBlock;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.BlockFlag;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.registry.misc.MetallurgicaMaterials;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.RegisterGameTestsEvent;

public class MetMaterialBlocks {
    public static ImmutableTable.Builder<BlockFlag, Material, BlockEntry<? extends MaterialBlock>> MATERIAL_BLOCKS_BUILDER = ImmutableTable
            .builder();

    public static Table<BlockFlag, Material, BlockEntry<? extends MaterialBlock>> MATERIAL_BLOCKS;

    public static void generateMaterialBlocks(MetallurgicaRegistrate registrate) {
        for (MetallurgicaMaterials value : MetallurgicaMaterials.values()) {
            Material material = value.getMaterial();
            for (FlagKey<?> flagKey : FlagKey.getAllFlags()) {
                var flag = material.getFlag(flagKey);
                if (flag instanceof BlockFlag blockFlag) {
                    registerMaterialBlock(blockFlag, material, registrate);
                }
            }
        }
    }

//
    private static void registerMaterialBlock(BlockFlag flag, Material material, MetallurgicaRegistrate registrate) {
        MATERIAL_BLOCKS_BUILDER.put(flag, material, flag.registerBlock(material, flag, registrate));
    }
}
