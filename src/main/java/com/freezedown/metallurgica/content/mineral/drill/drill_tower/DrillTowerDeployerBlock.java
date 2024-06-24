package com.freezedown.metallurgica.content.mineral.drill.drill_tower;

import com.freezedown.metallurgica.content.mineral.deposit.DepositManager;
import com.freezedown.metallurgica.content.mineral.deposit.MineralDepositBlock;
import com.freezedown.metallurgica.content.mineral.drill.drill_activator.DrillActivatorBlock;
import com.freezedown.metallurgica.foundation.util.SupportsDrillTower;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

@SupportsDrillTower
public class DrillTowerDeployerBlock extends Block implements IWrenchable, IBE<DrillTowerDeployerBlockEntity> {
    public DrillTowerDeployerBlock(Properties pProperties) {
        super(pProperties);
    }
    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return pLevel.getBlockState(pPos.above()).getBlock() instanceof DrillActivatorBlock;
    }
    @Override
    public Class<DrillTowerDeployerBlockEntity> getBlockEntityClass() {
        return DrillTowerDeployerBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<? extends DrillTowerDeployerBlockEntity> getBlockEntityType() {
        return null;
    }
    
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.FAIL;
        }
        DrillTowerDeployerBlockEntity blockEntity = (DrillTowerDeployerBlockEntity) pLevel.getBlockEntity(pPos);
        if (blockEntity != null) {
            blockEntity.findDeposit();
        }
        return InteractionResult.PASS;
    }
}
