package dev.metallurgists.metallurgica.content.mineral.drill.drill_tower;

import dev.metallurgists.metallurgica.content.mineral.deposit.DepositManager;
import dev.metallurgists.metallurgica.registry.MetallurgicaBlocks;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class DrillTowerDeployerBlockEntity extends SmartBlockEntity {
    public DrillTowerDeployerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    
    }
    
    public void findDeposit() {
        if (level == null) {
            return;
        }
        for (int i = 0; i < this.getBlockPos().getY() + 64; i++) {
            
            BlockPos checkedPos = new BlockPos(this.getBlockPos().getX(), (this.getBlockPos().getY() - 1) - i, this.getBlockPos().getZ());
            if (!DepositManager.hasDepositProperties(level.getBlockState(checkedPos))) {
                return;
            }
            
            if (!(level.getBlockState(new BlockPos(checkedPos)).is(MetallurgicaBlocks.drillTower.get()))) {
                return;
            }
            
            level.setBlock(this.getBlockPos(), MetallurgicaBlocks.drillTower.get().defaultBlockState().setValue(DrillTowerBlock.PLACED_BY_TOWER_DEPLOYER, true), 3);
            
        }
    }
}
