package com.freezedown.metallurgica.content.mineral.drill.drill_display;

import com.freezedown.metallurgica.content.mineral.drill.drill_activator.DrillActivatorBlock;
import com.freezedown.metallurgica.registry.MetallurgicaBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class DrillDisplayBlock extends Block implements IBE<DrillDisplayBlockEntity>, IWrenchable {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public DrillDisplayBlock(Properties pProperties) {
        super(pProperties);
    }
    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return DrillActivatorBlock.isActivator(pLevel.getBlockState(pPos.relative(getAttachedDirection(pState))));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACING));
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Level level = pContext.getLevel();
        BlockPos clickedPos = pContext.getClickedPos();
        Direction face = pContext.getClickedFace();
        if (face.getAxis() == Direction.Axis.Y) {
            face = pContext.getHorizontalDirection().getOpposite();
        }
        BlockState state = super.getStateForPlacement(pContext).setValue(FACING, face.getOpposite());
        if (!canSurvive(state, level, clickedPos))
            return null;
        return state;
    }
    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }
    public static Direction getAttachedDirection(BlockState state) {
        return state.getValue(FACING);
    }
    @Override
    public Class<DrillDisplayBlockEntity> getBlockEntityClass() {
        return DrillDisplayBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<? extends DrillDisplayBlockEntity> getBlockEntityType() {
        return MetallurgicaBlockEntities.drillDisplay.get();
    }
}
