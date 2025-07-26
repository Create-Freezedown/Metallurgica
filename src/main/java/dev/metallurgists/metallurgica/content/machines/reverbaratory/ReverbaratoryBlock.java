package dev.metallurgists.metallurgica.content.machines.reverbaratory;

import dev.metallurgists.metallurgica.registry.MetallurgicaBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ReverbaratoryBlock extends HorizontalDirectionalBlock implements IBE<ReverbaratoryBlockEntity> {
    public ReverbaratoryBlock(Properties pProperties) {
        super(pProperties);
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        super.createBlockStateDefinition(builder);
    }
    
    @Nullable
    public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }
    
    @Override
    public Class<ReverbaratoryBlockEntity> getBlockEntityClass() {
        return ReverbaratoryBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<? extends ReverbaratoryBlockEntity> getBlockEntityType() {
        return MetallurgicaBlockEntities.reverbaratory.get();
    }
}
