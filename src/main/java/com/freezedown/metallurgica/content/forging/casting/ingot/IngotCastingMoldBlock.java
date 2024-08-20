package com.freezedown.metallurgica.content.forging.casting.ingot;

import com.freezedown.metallurgica.registry.MetallurgicaBlockEntities;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class IngotCastingMoldBlock extends HorizontalKineticBlock implements IBE<IngotCastingMoldBlockEntity> {
    public IngotCastingMoldBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<IngotCastingMoldBlockEntity> getBlockEntityClass() {
        return IngotCastingMoldBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<? extends IngotCastingMoldBlockEntity> getBlockEntityType() {
        return MetallurgicaBlockEntities.ingotCastingMold.get();
    }
    
    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return null;
    }
}
