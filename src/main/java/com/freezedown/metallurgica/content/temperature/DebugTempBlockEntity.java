package com.freezedown.metallurgica.content.temperature;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.temperature.TempUtils;
import com.freezedown.metallurgica.foundation.util.ClientUtil;
import com.freezedown.metallurgica.infastructure.temperature.TemperatureHandler;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class DebugTempBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation, ITemperature {

    ///ONLY USE ON CLIENT-SIDE
    @SideOnly(Side.CLIENT)
    private double temp;

    public DebugTempBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    @Override
    public void lazyTick() {
        super.lazyTick();
        if (level == null || level.isClientSide)
            return;
        sendStuff();
    }
    
    @Override
    public void tick() {
        super.tick();
        if (level == null || level.isClientSide)
            return;
        assert level instanceof ServerLevel;
        syncToClient(worldPosition, level);
        TemperatureHandler.setBlockTemperature((ServerLevel) level, worldPosition, TempUtils.getCurrentTemperature(worldPosition.mutable(), level));
    }
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

    @Override
    public double getTemperature() {
        if(this.level != null) {
            if (this.level instanceof ServerLevel) {
                return TemperatureHandler.getBlockTemperature((ServerLevel) this.level, this.getBlockPos());
            } else {
                return temp;
            }
        } else {
            Metallurgica.LOGGER.error("TEMPERATURE SYSTEM: level is null");
            return 0.0;
        }
    }
    
    @Override
    public void setTemperature(double temperature) {
        if(this.level != null) {
            if (this.level instanceof ServerLevel) {
                TemperatureHandler.setBlockTemperature((ServerLevel) this.level, this.getBlockPos(), temperature);
            } else {
                temp = temperature;
            }
        } else {
            Metallurgica.LOGGER.error("TEMPERATURE SYSTEM: level is null");
        }
    }
    
    @Override
    public void sendStuff() {
        sendData();
        setChanged();
    }
    
    
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        if(this.level instanceof ServerLevel) {
            TemperatureHandler.setBlockTemperature((ServerLevel) this.level, this.getBlockPos(), compound.getDouble("temperature"));
        } else {
            temp = compound.getDouble("temperature");
        }
        super.read(compound, clientPacket);
    }
    
    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        if(clientPacket) {
            compound.putDouble("temperature", temp);
        } else {
            compound.putDouble("temperature", getTemperature());
        }
        super.write(compound, clientPacket);
    }
    
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return ClientUtil.currentTemperatureTooltip(tooltip, getTemperature());
    }
}
