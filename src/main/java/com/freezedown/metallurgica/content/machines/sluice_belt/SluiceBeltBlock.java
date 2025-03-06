package com.freezedown.metallurgica.content.machines.sluice_belt;

import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SluiceBeltBlock extends HorizontalKineticBlock implements IBE<SluiceBeltBlockEntity>, ProperWaterloggedBlock {

    public SluiceBeltBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState blockState) {
        return null;
    }

    @Override
    public Class<SluiceBeltBlockEntity> getBlockEntityClass() {
        return null;
    }

    @Override
    public BlockEntityType<? extends SluiceBeltBlockEntity> getBlockEntityType() {
        return null;
    }
}
