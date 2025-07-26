package dev.metallurgists.metallurgica.foundation.material.block.entity;

import dev.metallurgists.metallurgica.registry.material.init.MetMaterialBlockEntities;
import com.simibubi.create.content.kinetics.simpleRelays.SimpleKineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class MaterialCogWheelBlockEntity extends SimpleKineticBlockEntity {
    public MaterialCogWheelBlockEntity(BlockPos pos, BlockState state) {
        super(MetMaterialBlockEntities.materialCogwheel.get(), pos, state);
    }
}
