package com.freezedown.metallurgica.content.mineral.drill.drill_tower;

import com.freezedown.metallurgica.content.mineral.deposit.DepositManager;
import com.freezedown.metallurgica.content.mineral.deposit.MineralDepositBlock;
import com.freezedown.metallurgica.registry.MetallurgicaBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DrillTowerBlock extends Block implements IWrenchable {
    public DrillTowerBlock(Properties pProperties) {
        super(pProperties);
    }
    
    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        for (BlockState deposit : DepositManager.deposits) {
            if (pLevel.getBlockState(pPos.below()).getBlock() == deposit.getBlock()) {
                return true;
            }
        }
        return pLevel.getBlockState(pPos.below()).getBlock() instanceof MineralDepositBlock || pLevel.getBlockState(pPos.below()).getBlock() instanceof DrillTowerBlock;
    }
}
