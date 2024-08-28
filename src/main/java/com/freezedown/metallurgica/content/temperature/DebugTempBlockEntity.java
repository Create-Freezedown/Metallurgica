package com.freezedown.metallurgica.content.temperature;

import com.freezedown.metallurgica.foundation.temperature.TempUtils;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class DebugTempBlockEntity extends SmartBlockEntity {
    private double temperature;
    public DebugTempBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    @Override
    public void tick() {
        super.tick();
        temperature = TempUtils.getCurrentTemperature(worldPosition.mutable(), level.getBiome(worldPosition).get());
    }
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    
    }
    
    public double getTemperature() {
        return temperature;
    }
    
    
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        temperature = compound.getDouble("Temperature");
        super.read(compound, clientPacket);
    }
    
    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        compound.putDouble("Temperature", temperature);
        super.write(compound, clientPacket);
    }
}
