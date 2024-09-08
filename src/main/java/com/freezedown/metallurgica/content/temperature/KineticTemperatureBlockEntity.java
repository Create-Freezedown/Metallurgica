package com.freezedown.metallurgica.content.temperature;

import com.freezedown.metallurgica.foundation.block_entity.IntelligentKineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class KineticTemperatureBlockEntity extends IntelligentKineticBlockEntity implements ITemperature {
    private double temperature;
    
    public KineticTemperatureBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }
    
    @Override
    public double getTemperature() {
        return temperature;
    }
    
    @Override
    public void setTemperatureClient(double temperature) {
        this.temperature = temperature;
    }
    
    @Override
    public void sendStuff() {
        sendData();
        setChanged();
    }
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    
    }
    
    public void decreaseTemperature(double amount) {
        temperature -= amount;
    }
    
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
    
    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putDouble("temperature", temperature);
    }
    
    @Override
    public void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        temperature = compound.getDouble("temperature");
    }
}
