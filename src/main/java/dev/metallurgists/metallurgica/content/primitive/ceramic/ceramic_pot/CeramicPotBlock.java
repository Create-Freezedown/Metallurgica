package dev.metallurgists.metallurgica.content.primitive.ceramic.ceramic_pot;

import dev.metallurgists.metallurgica.registry.MetallurgicaBlockEntities;
import dev.metallurgists.metallurgica.registry.MetallurgicaShapes;
import com.simibubi.create.content.fluids.transfer.GenericItemEmptying;
import com.simibubi.create.content.fluids.transfer.GenericItemFilling;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.fluid.FluidHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class CeramicPotBlock extends Block implements IBE<CeramicPotBlockEntity> {
    public CeramicPotBlock(Properties pProperties) {
        super(pProperties);
    }
    
    public static VoxelShape SHAPE = MetallurgicaShapes.ceramicPot;
    
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }
    
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }
    
    public VoxelShape getBlockSupportShape(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return SHAPE;
    }
    
    public VoxelShape getVisualShape(BlockState pState, BlockGetter pReader, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }
    
    public boolean useShapeForLightOcclusion(BlockState pState) {
        return true;
    }
    
    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        CeramicPotBlockEntity tankAt = world.getBlockEntity(pos) instanceof CeramicPotBlockEntity ? (CeramicPotBlockEntity) world.getBlockEntity(pos) : null;
        if (tankAt == null)
            return 0;
        return tankAt.luminosity;
    }
    
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult ray) {
        ItemStack heldItem = player.getItemInHand(hand);
        
        return onBlockEntityUse(world, pos, be -> {
            if (!heldItem.isEmpty()) {
                if (FluidHelper.tryEmptyItemIntoBE(world, player, hand, heldItem, be))
                    return InteractionResult.SUCCESS;
                if (FluidHelper.tryFillItemFromBE(world, player, hand, heldItem, be))
                    return InteractionResult.SUCCESS;
                
                if (GenericItemEmptying.canItemBeEmptied(world, heldItem)
                        || GenericItemFilling.canItemBeFilled(world, heldItem))
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
            return InteractionResult.SUCCESS;
        });
    }
    
    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.hasBlockEntity() && (state.getBlock() != newState.getBlock() || !newState.hasBlockEntity())) {
            BlockEntity be = world.getBlockEntity(pos);
            if (!(be instanceof CeramicPotBlockEntity))
                return;
            world.removeBlockEntity(pos);
        }
    }
    
    @Override
    public Class<CeramicPotBlockEntity> getBlockEntityClass() {
        return CeramicPotBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<? extends CeramicPotBlockEntity> getBlockEntityType() {
        return MetallurgicaBlockEntities.ceramicPot.get();
    }
}
