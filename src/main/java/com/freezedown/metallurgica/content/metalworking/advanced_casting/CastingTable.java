package com.freezedown.metallurgica.content.metalworking.advanced_casting;

import com.freezedown.metallurgica.registry.MetallurgicaBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CastingTable extends Block implements IWrenchable, IBE<CastingTableBlockEntity> {
    private static final VoxelShape SHAPE = Shapes.join(
            Shapes.block(),
            Shapes.or(
                    Block.box(3.0D, 0.0D, 0.0D, 13.0D, 10.0D, 16.0D),
                    Block.box(0.0D, 0.0D, 3.0D, 16.0D, 10.0D, 13.0D),
                    Block.box(2.0D, 12.0D, 2.0D, 14.0D, 16.0D, 14.0D)
            ), BooleanOp.ONLY_FIRST);
    public CastingTable(Properties pProperties) {
        super(pProperties);
    }
    
    @Deprecated
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
    @Deprecated
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        if (player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }
        BlockEntity te = world.getBlockEntity(pos);
        if (te instanceof CastingTableBlockEntity) {
            ((CastingTableBlockEntity) te).interact(player, hand);
            return InteractionResult.SUCCESS;
        }
        return super.use(state, world, pos, player, hand, rayTraceResult);
    }
    
    @Override
    public Class<CastingTableBlockEntity> getBlockEntityClass() {
        return CastingTableBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<? extends CastingTableBlockEntity> getBlockEntityType() {
        return MetallurgicaBlockEntities.castingTable.get();
    }
}
