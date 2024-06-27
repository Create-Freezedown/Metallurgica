package com.freezedown.metallurgica.content.machines.electolizer;

import com.freezedown.metallurgica.registry.MetallurgicaBlockEntities;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;

public class ElectrolyzerBlock extends HorizontalKineticBlock implements IBE<ElectrolyzerBlockEntity> {
    public ElectrolyzerBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        return !AllBlocks.BASIN.has(worldIn.getBlockState(pos.below()));
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction prefferedSide = getPreferredHorizontalFacing(context);
        if (prefferedSide != null)
            return defaultBlockState().setValue(HORIZONTAL_FACING, prefferedSide);
        return super.getStateForPlacement(context);
    }
    
    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING)
                .getAxis();
    }
    
    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.getValue(HORIZONTAL_FACING)
                .getAxis();
    }
    @Override
    public float getParticleTargetRadius() {
        return .85f;
    }
    
    @Override
    public SpeedLevel getMinimumRequiredSpeedLevel() {
        return SpeedLevel.FAST;
    }
    
    @Override
    public Class<ElectrolyzerBlockEntity> getBlockEntityClass() {
        return ElectrolyzerBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<? extends ElectrolyzerBlockEntity> getBlockEntityType() {
        return MetallurgicaBlockEntities.electrolyzer.get();
    }
    
    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }
}
