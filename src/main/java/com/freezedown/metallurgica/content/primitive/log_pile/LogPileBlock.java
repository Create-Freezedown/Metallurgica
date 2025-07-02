package com.freezedown.metallurgica.content.primitive.log_pile;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class LogPileBlock extends Block {
    public static final IntegerProperty LAYERS  = IntegerProperty.create("layers", 1, 4);
    
    public LogPileBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LAYERS, 1));
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LAYERS);
        super.createBlockStateDefinition(builder);
    }
    
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return getShape(pState.getValue(LAYERS));
    }
    
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return getShape(pState.getValue(LAYERS));
    }
    
    public VoxelShape getBlockSupportShape(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return getShape(pState.getValue(LAYERS));
    }
    
    public VoxelShape getVisualShape(BlockState pState, BlockGetter pReader, BlockPos pPos, CollisionContext pContext) {
        return getShape(pState.getValue(LAYERS));
    }
    
    public boolean useShapeForLightOcclusion(BlockState pState) {
        return true;
    }
    
    public float getShadeBrightness(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return pState.getValue(LAYERS) == 4 ? 0.2F : 1.0F;
    }
    
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockState state = pLevel.getBlockState(pPos.below());
        return Block.isFaceFull(state.getCollisionShape(pLevel, pPos.below()), Direction.UP) || state.is(this) && state.getValue(LAYERS) == 4;
    }
    
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState state = pContext.getLevel().getBlockState(pContext.getClickedPos());
        if (state.is(this.asBlock())) {
            int layer = state.getValue(LAYERS);
            return state.setValue(LAYERS, Math.min(4, layer + 1));
        } else {
            return super.getStateForPlacement(pContext);
        }
    }
    
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        return !pState.canSurvive(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }
    
    protected static VoxelShape getShape(int pLayers) {
        return switch (pLayers) {
            case 2 -> Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
            case 3 -> Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0);
            case 4 -> Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);
            default -> Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0);
        };
    }
    
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);
        boolean isBlockItem = heldItem.is(this.asItem());
        if (isBlockItem) {
            int layers = state.getValue(LAYERS);
            if (layers < 4) {
                if (!world.isClientSide) {
                    world.setBlock(pos, state.setValue(LAYERS, layers + 1), 3);
                    if (!player.isCreative()) {
                        heldItem.shrink(1);
                    }
                }
                return InteractionResult.CONSUME;
            }
        }
        return super.use(state, world, pos, player, hand, hit);
    }


    public static LootTable.Builder buildLootTable(LogPileBlock block) {
        LootItemCondition.Builder survivesExplosion = ExplosionCondition.survivesExplosion();
        LootTable.Builder builder = LootTable.lootTable();
        for (int level : LogPileBlock.LAYERS.getPossibleValues()) {
            ItemLike drop = block.asItem();
            LootPool.Builder poolBuilder = LootPool.lootPool();
            poolBuilder.add(LootItem.lootTableItem(drop)
                    .when(survivesExplosion)
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                            .setProperties(StatePropertiesPredicate.Builder.properties()
                                    .hasProperty(LogPileBlock.LAYERS, level))));
            builder.withPool(poolBuilder.apply(SetItemCountFunction.setCount(ConstantValue.exactly(level))));
        }
        return builder;
    }

    public static LootTable.Builder charcoalLootTable(LogPileBlock block) {
        LootItemCondition.Builder survivesExplosion = ExplosionCondition.survivesExplosion();
        LootTable.Builder builder = LootTable.lootTable();
        for (int level : LogPileBlock.LAYERS.getPossibleValues()) {
            ItemLike drop = Items.CHARCOAL;
            LootPool.Builder poolBuilder = LootPool.lootPool();
            poolBuilder.add(LootItem.lootTableItem(drop)
                    .when(survivesExplosion)
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                            .setProperties(StatePropertiesPredicate.Builder.properties()
                                    .hasProperty(LogPileBlock.LAYERS, level))));
            builder.withPool(poolBuilder.apply(SetItemCountFunction.setCount(ConstantValue.exactly(level))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)));
        }
        return builder;
    }
}
