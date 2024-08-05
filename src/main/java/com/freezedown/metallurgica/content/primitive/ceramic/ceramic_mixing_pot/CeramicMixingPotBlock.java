package com.freezedown.metallurgica.content.primitive.ceramic.ceramic_mixing_pot;

import com.freezedown.metallurgica.registry.MetallurgicaBlocks;
import com.freezedown.metallurgica.registry.MetallurgicaShapes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.Create;
import com.simibubi.create.content.fluids.transfer.GenericItemEmptying;
import com.simibubi.create.content.fluids.transfer.GenericItemFilling;
import com.simibubi.create.content.processing.basin.BasinBlock;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import static com.freezedown.metallurgica.registry.MetallurgicaBlockEntities.ceramicMixingPot;

public class CeramicMixingPotBlock extends Block implements IBE<CeramicMixingPotBlockEntity> {
    public static VoxelShape SHAPE = MetallurgicaShapes.ceramicPot;
    
    public CeramicMixingPotBlock(Properties pProperties) {
        super(pProperties);
    }
    
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
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, worldIn, pos, newState);
    }
    
    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (player.isSpectator())
            return InteractionResult.PASS;
        
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
                        && !be.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
                        .map(iFluidHandler -> iFluidHandler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE))
                        .orElse(FluidStack.EMPTY)
                        .isEmpty()) {
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.PASS;
            }
            if (player.isShiftKeyDown()) {
                IItemHandlerModifiable inv = be.itemCapability.orElse(new ItemStackHandler(1));
                boolean success = false;
                for (int slot = 0; slot < inv.getSlots(); slot++) {
                    ItemStack stackInSlot = inv.getStackInSlot(slot);
                    if (stackInSlot.isEmpty())
                        continue;
                    player.getInventory()
                            .placeItemBackInInventory(stackInSlot);
                    inv.setStackInSlot(slot, ItemStack.EMPTY);
                    success = true;
                }
                if (success)
                    worldIn.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, .2f,
                            1f + Create.RANDOM.nextFloat());
            } else {
                be.stir();
                if (!player.getItemInHand(handIn).is(AllItems.EXTENDO_GRIP.get()))
                    player.causeFoodExhaustion(32 * AllConfigs.server().kinetics.crankHungerMultiplier.getF());
            }
            return InteractionResult.SUCCESS;
        });
        
    }
    
    @Override
    public void updateEntityAfterFallOn(BlockGetter worldIn, Entity entityIn) {
        super.updateEntityAfterFallOn(worldIn, entityIn);
        if (!MetallurgicaBlocks.ceramicMixingPot.has(worldIn.getBlockState(entityIn.blockPosition())))
            return;
        if (!(entityIn instanceof ItemEntity))
            return;
        if (!entityIn.isAlive())
            return;
        ItemEntity itemEntity = (ItemEntity) entityIn;
        withBlockEntityDo(worldIn, entityIn.blockPosition(), be -> {
            
            // Tossed items bypass the quarter-stack limit
            be.inputInventory.withMaxStackSize(64);
            ItemStack insertItem = ItemHandlerHelper.insertItem(be.inputInventory, itemEntity.getItem()
                    .copy(), false);
            be.inputInventory.withMaxStackSize(16);
            
            if (insertItem.isEmpty()) {
                itemEntity.discard();
                return;
            }
            
            itemEntity.setItem(insertItem);
        });
    }
    
    
    @Override
    public Class<CeramicMixingPotBlockEntity> getBlockEntityClass() {
        return CeramicMixingPotBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<? extends CeramicMixingPotBlockEntity> getBlockEntityType() {
        return ceramicMixingPot.get();
    }
    
    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }
}
