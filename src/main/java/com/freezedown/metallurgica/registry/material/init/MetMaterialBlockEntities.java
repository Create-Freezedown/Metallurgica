package com.freezedown.metallurgica.registry.material.init;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.material.block.entity.MaterialCogWheelBlockEntity;
import com.freezedown.metallurgica.foundation.material.block.renderer.MaterialCogWheelRenderer;
import com.freezedown.metallurgica.infastructure.material.Material;
import com.freezedown.metallurgica.infastructure.material.MaterialHelper;
import com.freezedown.metallurgica.infastructure.material.registry.flags.FlagKey;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.BlockFlag;
import com.freezedown.metallurgica.registry.material.MetMaterials;
import com.simibubi.create.content.kinetics.simpleRelays.SimpleKineticBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

import static com.freezedown.metallurgica.Metallurgica.registrate;

public class MetMaterialBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Metallurgica.ID);

    public static final RegistryObject<BlockEntityType<MaterialCogWheelBlockEntity>> materialCogwheel = BLOCK_ENTITY_TYPES.register("material_cogwheel", () -> BlockEntityType.Builder.of(MaterialCogWheelBlockEntity::new, gatherValidBlocks(FlagKey.COG_WHEEL, FlagKey.LARGE_COG_WHEEL).toArray(new Block[0])).build(null));


    private static List<Block> gatherValidBlocks(FlagKey<? extends BlockFlag> flagKey) {
        List<Block> validBlocks = new ArrayList<>();
        for (Material material : MetMaterials.registeredMaterials.values()) {
            if (material.hasFlag(flagKey)) {
                validBlocks.add(MaterialHelper.getBlock(material, flagKey));
            }
        }
        return validBlocks;
    }

    private static List<Block> gatherValidBlocks(FlagKey<? extends BlockFlag>... flagKeys) {
        List<Block> validBlocks = new ArrayList<>();
        for (Material material : MetMaterials.registeredMaterials.values()) {
            for (FlagKey<? extends BlockFlag> flagKey : flagKeys) {
                if (material.hasFlag(flagKey)) {
                    validBlocks.add(MaterialHelper.getBlock(material, flagKey));
                }
            }
        }
        return validBlocks;
    }

    public static void register(IEventBus bus) {
        BLOCK_ENTITY_TYPES.register(bus);
    }
}
