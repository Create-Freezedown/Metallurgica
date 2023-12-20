package com.freezedown.metallurgica.content.fluids.faucet;

import com.freezedown.metallurgica.foundation.util.BlockEntityHelper;
import com.freezedown.metallurgica.registry.MetallurgicaBlockEntities;
import com.freezedown.metallurgica.registry.MetallurgicaShapes;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Optional;

public class FaucetBlock extends Block implements IBE<FaucetBlockEntity> {
    public static final DirectionProperty FACING = BlockStateProperties.FACING_HOPPER;
    private static final EnumMap<Direction,VoxelShape> SHAPES = Maps.newEnumMap(ImmutableMap.of(
            Direction.DOWN,  MetallurgicaShapes.spoutputCeiling,
            Direction.NORTH, MetallurgicaShapes.spoutputWallNorth,
            Direction.SOUTH, MetallurgicaShapes.spoutputWallSouth,
            Direction.WEST,  MetallurgicaShapes.spoutputWallWest,
            Direction.EAST,  MetallurgicaShapes.spoutputWallEast
    ));
    
    public FaucetBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }
    
    /* Blockstate */
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction dir = context.getClickedFace();
        if (dir == Direction.UP) {
            dir = Direction.DOWN;
        }
        return this.defaultBlockState().setValue(FACING, dir);
    }
    
    @SuppressWarnings("deprecation")
    @Deprecated
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }
    
    @Override
    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
        return false;
    }
    
    
    /* Tile entity */
    
    
    @SuppressWarnings("deprecation")
    @Deprecated
    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }
        getFaucet(worldIn, pos).ifPresent(FaucetBlockEntity::activate);
        return InteractionResult.SUCCESS;
    }
    
    @SuppressWarnings("deprecation")
    @Deprecated
    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (worldIn.isClientSide()) {
            return;
        }
        getFaucet(worldIn, pos).ifPresent(faucet -> {
            faucet.neighborChanged(fromPos);
            faucet.handleRedstone(worldIn.hasNeighborSignal(pos));
        });
    }
    
    /**
     * Gets the facuet tile entity at the given position
     * @param world  World instance
     * @param pos    Faucet position
     * @return  Optional of faucet, empty if missing or wrong type
     */
    private Optional<FaucetBlockEntity> getFaucet(Level world, BlockPos pos) {
        return BlockEntityHelper.get(FaucetBlockEntity.class, world, pos);
    }
    
    /* Display */
    
    
    

    
    
    
    @Override
    public Class<FaucetBlockEntity> getBlockEntityClass() {
        return FaucetBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<? extends FaucetBlockEntity> getBlockEntityType() {
        return MetallurgicaBlockEntities.faucet.get();
    }
}
