package com.freezedown.metallurgica.content.machines.rotary_kiln.heater_segment;

import com.freezedown.metallurgica.registry.MetallurgicaBlockEntities;
import com.freezedown.metallurgica.registry.MetallurgicaBlocks;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class HeaterSegmentBlock extends HorizontalKineticBlock implements IBE<HeaterSegmentBlockEntity> {
    
    public HeaterSegmentBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING).getAxis();
    }
    
    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.getValue(HORIZONTAL_FACING).getAxis();
    }
    
    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return InteractionResult.PASS;
    }
    
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (!level.getBlockTicks()
                .hasScheduledTick(pos, this))
            level.scheduleTick(pos, this, 1);
    }
    
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand,
                                 BlockHitResult pHit) {
        return onBlockEntityUse(pLevel, pPos, heaterSegmentBlockEntity -> heaterSegmentBlockEntity.applyMaterialIfValid(pPlayer.getItemInHand(pHand)));
    }
    
    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        Direction direction = pState.getValue(HORIZONTAL_FACING);
        for (Direction side : Iterate.directions) {
            if (side == direction)
                continue;
            for (boolean secondary : Iterate.falseAndTrue) {
                Direction targetSide = secondary ? direction.getClockWise() : side;
                BlockPos structurePos = (secondary ? pPos.relative(side) : pPos).relative(targetSide);
                BlockState occupiedState = pLevel.getBlockState(structurePos);
                BlockState requiredStructure = MetallurgicaBlocks.heaterSegmentStructural.getDefaultState()
                        .setValue(HeaterSegmentStructuralBlock.FACING, targetSide);
                if (occupiedState == requiredStructure)
                    continue;
                if (!occupiedState.getMaterial()
                        .isReplaceable()) {
                    pLevel.destroyBlock(pPos, false);
                    return;
                }
                pLevel.setBlockAndUpdate(structurePos, requiredStructure);
            }
        }
    }
    
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
    
    @Override
    public Class<HeaterSegmentBlockEntity> getBlockEntityClass() {
        return HeaterSegmentBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<? extends HeaterSegmentBlockEntity> getBlockEntityType() {
        return MetallurgicaBlockEntities.heaterSegment.get();
    }
}
