package dev.metallurgists.metallurgica.registry.material.init;

import dev.metallurgists.metallurgica.foundation.material.block.IMaterialBlock;
import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey;
import dev.metallurgists.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.interfaces.IBlockRegistry;
import dev.metallurgists.metallurgica.registry.material.MetMaterials;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.tterrag.registrate.util.entry.BlockEntry;

public class MetMaterialBlocks {
    public static ImmutableTable.Builder<FlagKey<?>, Material, BlockEntry<? extends IMaterialBlock>> MATERIAL_BLOCKS_BUILDER = ImmutableTable.builder();

    public static Table<FlagKey<?>, Material, BlockEntry<? extends IMaterialBlock>> MATERIAL_BLOCKS;

    public static void generateMaterialBlocks(MetallurgicaRegistrate registrate) {
        for (Material material : MetMaterials.registeredMaterials.values()) {
            for (FlagKey<?> flagKey : FlagKey.getAllFlags()) {
                var flag = material.getFlag(flagKey);
                if (!material.noRegister(flagKey)) {
                    if (flag instanceof IBlockRegistry blockRegistry) {
                        registerMaterialBlock(blockRegistry, material, flagKey, registrate);
                    }
                }
            }
        }
    }

//
    private static void registerMaterialBlock(IBlockRegistry blockRegistry, Material material, FlagKey<?> flagKey, MetallurgicaRegistrate registrate) {
        MATERIAL_BLOCKS_BUILDER.put(flagKey, material, blockRegistry.registerBlock(material, blockRegistry, registrate));
    }
}
