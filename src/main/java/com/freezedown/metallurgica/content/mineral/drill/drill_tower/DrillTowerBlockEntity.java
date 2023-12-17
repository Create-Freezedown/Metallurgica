package com.freezedown.metallurgica.content.mineral.drill.drill_tower;

import com.freezedown.metallurgica.content.mineral.deposit.MineralDepositBlockEntity;
import com.freezedown.metallurgica.content.mineral.drill.drill_activator.DrillActivatorBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DrillTowerBlockEntity extends KineticBlockEntity {
    public DrillTowerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    public boolean isController;
    public boolean isDepositAttached;
    public int height;
    public float efficiency;
    public BlockPos depositPos;
    @Override
    public void tick() {
        super.tick();
        if (this.level == null) {
            return;
        }
        if (this.level.getBlockEntity(this.worldPosition.above()) instanceof DrillActivatorBlockEntity drillActivator) {
            isController = true;
            efficiency = drillActivator.efficiency;
        }
        if ((this.level.getBlockEntity(this.worldPosition.below()) instanceof MineralDepositBlockEntity mineralDeposit)) {
            isDepositAttached = true;
            depositPos = this.worldPosition.below();
        }
        getDepositPositionFromBelowDrill();
        sendEfficiencyToOtherDrills();
        if (isController) {
            height = getHeight();
        }
    }
    
    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putBoolean("isController", isController);
        compound.putBoolean("isDepositAttached", isDepositAttached);
        if (isController) {
            compound.putInt("height", height);
            compound.putFloat("efficiency", efficiency);
            if (depositPos != null) {
                compound.putInt("depositX", depositPos.getX());
                compound.putInt("depositY", depositPos.getY());
                compound.putInt("depositZ", depositPos.getZ());
            }
        }
        
    }
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        isController = compound.getBoolean("isController");
        isDepositAttached = compound.getBoolean("isDepositAttached");
        if (isController) {
            height = compound.getInt("height");
            efficiency = compound.getFloat("efficiency");
            if (compound.contains("depositX") ) {
                depositPos = new BlockPos(compound.getInt("depositX"), compound.getInt("depositY"), compound.getInt("depositZ"));
            }
        }
        
    }
    public void getDepositPositionFromBelowDrill() {
        if (this.level == null) {
            return;
        }
        if (this.level.getBlockEntity(this.worldPosition.below()) instanceof DrillTowerBlockEntity drillTower) {
            //if the above drill tower deposit location is null, set it to this deposit location
            if (drillTower.depositPos != null) {
                this.depositPos = drillTower.depositPos;
            }
        }
    }
    public void sendEfficiencyToOtherDrills() {
        if (this.level == null) {
            return;
        }
        if (this.level.getBlockEntity(this.worldPosition.above()) instanceof DrillActivatorBlockEntity drillActivator && isController) {
            this.efficiency = drillActivator.efficiency;
        }
        if (!(this.level.getBlockEntity(this.worldPosition.above()) instanceof DrillActivatorBlockEntity) || !(this.level.getBlockEntity(this.worldPosition.above()) instanceof DrillTowerBlockEntity drillTower)) {
            efficiency = 0.0f;
        }
        if (this.level.getBlockEntity(this.worldPosition.below()) instanceof DrillTowerBlockEntity drillTower) {
            drillTower.efficiency = efficiency;
        }
    }
    public int getHeight() {
        int height = 0;
        if (this.level == null) {
            return height;
        }
        for (int i = 0; i < 384; i++) {
            if (this.level.getBlockEntity(this.worldPosition.below(i)) instanceof DrillTowerBlockEntity drillTower) {
                height++;
                //Stop the loop if it hits the deposit position
                if (drillTower.depositPos != null) {
                    if (drillTower.depositPos.equals(this.worldPosition.below(i))) {
                        break;
                    }
                }
                //don't count anything below the deposit position
                if (drillTower.depositPos == null) {
                    break;
                }
            }
        }
        return height;
    }
}
