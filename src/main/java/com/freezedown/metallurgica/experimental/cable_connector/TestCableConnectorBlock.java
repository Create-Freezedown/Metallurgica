package com.freezedown.metallurgica.experimental.cable_connector;

import com.drmangotea.tfmg.blocks.electricity.base.IHaveCables;
import com.drmangotea.tfmg.blocks.electricity.base.WallMountBlock;
import com.drmangotea.tfmg.blocks.electricity.base.cables.ConnectNeightborsPacket;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.drmangotea.tfmg.registry.TFMGShapes;
import com.freezedown.metallurgica.registry.MetallurgicaBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.PacketDistributor;

public class TestCableConnectorBlock extends WallMountBlock implements IBE<TestCableConnectorBlockEntity>, IHaveCables, IWrenchable {
    public static final BooleanProperty EXTENSION = BooleanProperty.create("extension");

    public TestCableConnectorBlock(BlockBehaviour.Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.defaultBlockState().setValue(EXTENSION, false));
    }

    public VoxelShape getShape(BlockState pState, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return pState.getValue(EXTENSION) ? TFMGShapes.CABLE_CONNECTOR_MIDDLE.get(pState.getValue(FACING)) : TFMGShapes.CABLE_CONNECTOR.get(pState.getValue(FACING));
    }

    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction direction = context.getClickedFace();
        return this.onBlockEntityUse(level, pos, (be) -> {
            be.sendStuff();
            return InteractionResult.SUCCESS;
        });
    }

    public void onPlace(BlockState pState, Level level, BlockPos pos, BlockState pOldState, boolean pIsMoving) {
        TFMGPackets.getChannel().send(PacketDistributor.ALL.noArg(), new ConnectNeightborsPacket(pos));
        this.withBlockEntityDo(level, pos, TestCableConnectorBlockEntity::onPlaced);
    }

    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
    }

    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        return pPlayer.getItemInHand(pHand).isEmpty() ? InteractionResult.SUCCESS : super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(EXTENSION);
    }

    public Class<TestCableConnectorBlockEntity> getBlockEntityClass() {
        return TestCableConnectorBlockEntity.class;
    }

    public BlockEntityType<? extends TestCableConnectorBlockEntity> getBlockEntityType() {
        return MetallurgicaBlockEntities.testCableConnector.get();
    }
}
