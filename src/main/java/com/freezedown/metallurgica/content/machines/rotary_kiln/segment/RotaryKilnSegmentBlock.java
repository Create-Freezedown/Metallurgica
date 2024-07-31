package com.freezedown.metallurgica.content.machines.rotary_kiln.segment;

import com.freezedown.metallurgica.registry.MetallurgicaBlockEntities;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RotaryKilnSegmentBlock extends HorizontalKineticBlock implements IBE<RotaryKilnSegmentBlockEntity> {
    public RotaryKilnSegmentBlock(Properties pProperties) {
        super(pProperties);
    }
    public static final VoxelShape SHAPE = Block.box(-8.0D, -8.0D, -8.0D, 24.0D, 24.0D, 24.0D);
    
    
    
    public VoxelShape getOcclusionShape(BlockState p_60578_, BlockGetter p_60579_, BlockPos p_60580_) {
        return SHAPE;
    }
    
    
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
    
    
    public boolean useShapeForLightOcclusion(BlockState p_54582_) {
        return true;
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
    public Class<RotaryKilnSegmentBlockEntity> getBlockEntityClass() {
        return RotaryKilnSegmentBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<? extends RotaryKilnSegmentBlockEntity> getBlockEntityType() {
        return MetallurgicaBlockEntities.rotaryKilnSegment.get();
    }
    
    @Override
    public float getParticleTargetRadius() {
        return 1.5f;
    }
    
    @Override
    public float getParticleInitialRadius() {
        return 1.25f;
    }
}
