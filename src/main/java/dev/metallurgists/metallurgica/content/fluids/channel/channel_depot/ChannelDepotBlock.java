package dev.metallurgists.metallurgica.content.fluids.channel.channel_depot;

import dev.metallurgists.metallurgica.registry.MetallurgicaBlockEntities;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.fluids.transfer.GenericItemEmptying;
import com.simibubi.create.content.fluids.transfer.GenericItemFilling;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.fluid.FluidHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
@SuppressWarnings("removal")
public class ChannelDepotBlock extends Block implements IBE<ChannelDepotBlockEntity>, IWrenchable {
    public static final DirectionProperty FACING = BlockStateProperties.FACING_HOPPER;
    public ChannelDepotBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.DOWN));
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_206840_1_) {
        super.createBlockStateDefinition(p_206840_1_.add(FACING));
    }
    
    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos.above());
        if (blockEntity instanceof BasinOperatingBlockEntity)
            return false;
        return true;
    }
    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        if (!context.getLevel().isClientSide)
            withBlockEntityDo(context.getLevel(), context.getClickedPos(),
                    bte -> bte.onWrenched(context.getClickedFace()));
        return InteractionResult.SUCCESS;
    }
    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn,
                                 BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(handIn);
        
        return onBlockEntityUse(worldIn, pos, be -> {
            if (!heldItem.isEmpty()) {
                if (FluidHelper.tryEmptyItemIntoBE(worldIn, player, handIn, heldItem, be))
                    return InteractionResult.SUCCESS;
                if (FluidHelper.tryFillItemFromBE(worldIn, player, handIn, heldItem, be))
                    return InteractionResult.SUCCESS;
                
                if (GenericItemEmptying.canItemBeEmptied(worldIn, heldItem)
                        || GenericItemFilling.canItemBeFilled(worldIn, heldItem))
                    return InteractionResult.SUCCESS;
                if (heldItem.getItem()
                        .equals(Items.SPONGE)
                        && !be.getCapability(ForgeCapabilities.FLUID_HANDLER)
                        .map(iFluidHandler -> iFluidHandler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE))
                        .orElse(FluidStack.EMPTY)
                        .isEmpty()) {
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.PASS;
            }
            be.onEmptied();
            return InteractionResult.SUCCESS;
        });
    }
    @Override
    public VoxelShape getInteractionShape(BlockState p_199600_1_, BlockGetter p_199600_2_, BlockPos p_199600_3_) {
        return AllShapes.BASIN_RAYTRACE_SHAPE;
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AllShapes.BASIN_BLOCK_SHAPE;
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
        if (ctx instanceof EntityCollisionContext && ((EntityCollisionContext) ctx).getEntity() instanceof ItemEntity)
            return AllShapes.BASIN_COLLISION_SHAPE;
        return getShape(state, reader, pos, ctx);
    }
    
    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, worldIn, pos, newState);
    }
    @Override
    public Class<ChannelDepotBlockEntity> getBlockEntityClass() {
        return ChannelDepotBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<? extends ChannelDepotBlockEntity> getBlockEntityType() {
        return MetallurgicaBlockEntities.channelDepot.get();
    }
    public static boolean canOutputTo(BlockGetter world, BlockPos basinPos, Direction direction) {
        BlockPos neighbour = basinPos.relative(direction);
        BlockPos output = neighbour.below();
        BlockState blockState = world.getBlockState(neighbour);
        
        if (!blockState.getCollisionShape(world, neighbour)
                .isEmpty()) {
            return false;
        }
        //Adapt to use Channels
        
        // else {
        //   BlockEntity blockEntity = world.getBlockEntity(output);
        //   if (blockEntity instanceof BeltBlockEntity) {
        //       BeltBlockEntity belt = (BeltBlockEntity) blockEntity;
        //       return belt.getSpeed() == 0 || belt.getMovementFacing() != direction.getOpposite();
        //   }
        //}
        //DirectBeltInputBehaviour directBeltInputBehaviour =
        //        BlockEntityBehaviour.get(world, output, DirectBeltInputBehaviour.TYPE);
        //if (directBeltInputBehaviour != null)
        //    return directBeltInputBehaviour.canInsertFromSide(direction);
        return false;
    }
    
    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }
}
