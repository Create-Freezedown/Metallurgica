package com.freezedown.metallurgica.content.mineral.drill.drill_tower;

import com.freezedown.metallurgica.content.mineral.deposit.DepositManager;
import com.freezedown.metallurgica.content.mineral.deposit.MineralDepositBlock;
import com.freezedown.metallurgica.content.mineral.drill.drill_activator.DrillActivatorBlock;
import com.freezedown.metallurgica.foundation.util.SupportsDrillTower;
import com.freezedown.metallurgica.registry.MetallurgicaBlocks;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

@SupportsDrillTower
public class DrillTowerBlock extends Block implements IWrenchable {
    public static final BooleanProperty PLACED_BY_TOWER_DEPLOYER = BooleanProperty.create("placed_by_tower_deployer");
    public DrillTowerBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(PLACED_BY_TOWER_DEPLOYER, false));
    }
    
    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        for (BlockState deposit : DepositManager.deposits) {
            if (pLevel.getBlockState(pPos.below()).getBlock() == deposit.getBlock()) {
                return true;
            }
        }
        return pLevel.getBlockState(pPos.below()).getBlock() instanceof MineralDepositBlock || pLevel.getBlockState(pPos.below()).getBlock() instanceof DrillTowerBlock || pLevel.getBlockState(pPos.above()).getBlock() instanceof DrillTowerBlock || pLevel.getBlockState(pPos.above()).getBlock() instanceof DrillActivatorBlock;
    }
    public BlockState getStateForPlacement(BlockPlaceContext placeContext) {
        return this.defaultBlockState().setValue(PLACED_BY_TOWER_DEPLOYER, false);
    }
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(PLACED_BY_TOWER_DEPLOYER);
    }
    
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        super.neighborChanged(pState, pLevel, pPos, pBlock, pFromPos, pIsMoving);
        if (!this.canSurvive(pState, pLevel, pPos)) {
            pLevel.destroyBlock(pPos, false);
        }
        BlockState above = pLevel.getBlockState(pPos.above());
        if (!(above.getBlock().getClass().isAnnotationPresent(SupportsDrillTower.class))) {
            pLevel.destroyBlock(pPos, false);
        }
    }
    
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        super.onPlace(pState, pLevel, pPos, pOldState, pIsMoving);
        if (!this.canSurvive(pState, pLevel, pPos)) {
            pLevel.destroyBlock(pPos, false);
        }
        BlockState below = pLevel.getBlockState(pPos.below());
        if (below.isAir() && pState.getValue(PLACED_BY_TOWER_DEPLOYER)) {
            pLevel.setBlock(pPos.below(), MetallurgicaBlocks.drillTower.getDefaultState().setValue(PLACED_BY_TOWER_DEPLOYER, true), 3);
            pState.setValue(PLACED_BY_TOWER_DEPLOYER, false);
        }
    }
}
