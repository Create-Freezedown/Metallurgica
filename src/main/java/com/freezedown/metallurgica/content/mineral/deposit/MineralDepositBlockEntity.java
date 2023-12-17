package com.freezedown.metallurgica.content.mineral.deposit;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class MineralDepositBlockEntity extends SmartBlockEntity {
    public final int baseMineralAmount;
    public int mineralAmount;
    
    public MineralDepositBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.baseMineralAmount = Create.RANDOM.nextInt(3000000);
        this.mineralAmount = this.baseMineralAmount;
    }
    
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }
    
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putInt("MineralAmount", this.baseMineralAmount);
        super.write(compound, clientPacket);
    }
    
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        this.mineralAmount = compound.getInt("MineralAmount");
    }
}
