package com.freezedown.metallurgica.content.metalworking.casting.ingot;

import com.freezedown.metallurgica.registry.MetallurgicaBlockEntities;
import com.freezedown.metallurgica.registry.MetallurgicaShapes;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class IngotCastingMoldBlock extends HorizontalKineticBlock implements IBE<IngotCastingMoldBlockEntity> {
    public IngotCastingMoldBlock(Properties properties) {
        super(properties);
    }
    
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return MetallurgicaShapes.ingotCastingMold.get(pState.getValue(HORIZONTAL_FACING));
    }
    
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return MetallurgicaShapes.ingotCastingMold.get(pState.getValue(HORIZONTAL_FACING));
    }
    
    public VoxelShape getBlockSupportShape(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return MetallurgicaShapes.ingotCastingMold.get(pState.getValue(HORIZONTAL_FACING));
    }
    
    public VoxelShape getVisualShape(BlockState pState, BlockGetter pReader, BlockPos pPos, CollisionContext pContext) {
        return MetallurgicaShapes.ingotCastingMold.get(pState.getValue(HORIZONTAL_FACING));
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
