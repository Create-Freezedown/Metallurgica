package com.freezedown.metallurgica.content.temperature;

import com.freezedown.metallurgica.foundation.temperature.TempUtils;
import com.freezedown.metallurgica.foundation.util.ClientUtil;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class DebugTempBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation, ITemperature {
    private double temperature;
    public DebugTempBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    @Override
    public void lazyTick() {
        super.lazyTick();
        if (level == null)
            return;
        if (level.isClientSide)
            return;
        sendStuff();
    }
    
    @Override
    public void tick() {
        super.tick();
        if (level == null)
            return;
        if (level.isClientSide)
            return;
        syncToClient(worldPosition, level);
        temperature = TempUtils.getCurrentTemperature(worldPosition.mutable(), level);
    }
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    
    }
    
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
    protected void read(CompoundTag compound, boolean clientPacket) {
        temperature = compound.getDouble("Temperature");
        super.read(compound, clientPacket);
    }
    
    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        compound.putDouble("Temperature", temperature);
        super.write(compound, clientPacket);
    }
    
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return ClientUtil.currentTemperatureTooltip(tooltip, getTemperature());
    }
}
