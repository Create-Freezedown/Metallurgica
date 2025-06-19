package com.freezedown.metallurgica.foundation.material.block.entity;

import com.freezedown.metallurgica.registry.material.init.MetMaterialBlockEntities;
import com.simibubi.create.content.kinetics.simpleRelays.SimpleKineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class MaterialCogWheelBlockEntity extends SimpleKineticBlockEntity {
    public MaterialCogWheelBlockEntity(BlockPos pos, BlockState state) {
        super(MetMaterialBlockEntities.materialCogwheel.get(), pos, state);
    }
}
