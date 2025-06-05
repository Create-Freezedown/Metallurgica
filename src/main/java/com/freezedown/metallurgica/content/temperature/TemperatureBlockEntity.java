package com.freezedown.metallurgica.content.temperature;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.block_entity.IntelligentBlockEntity;
import com.freezedown.metallurgica.foundation.temperature.server.TemperatureHandler;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TemperatureBlockEntity extends IntelligentBlockEntity implements ITemperature {
    public TemperatureBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    ///ONLY USE ON CLIENT-SIDE
    @SideOnly(Side.CLIENT)
    private double temp;

    @SideOnly(Side.CLIENT)
    @Override
    public double getTemp() {
        return temp;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void setTemp(double temperature) {
        temp = temperature;
    }

    @Override
    public BlockPos getPos() {
        return getBlockPos();
    }

    @Override
    public void sendStuff() {
        sendData();
        setChanged();
    }
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}
    
    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        if(clientPacket) {
            compound.putDouble("temperature", temp);
        } else {
            compound.putDouble("temperature", getTemperature());
        }
    }
    
    @Override
    public void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        if(this.level instanceof ServerLevel) {
            TemperatureHandler.getHandler((ServerLevel) this.level).setBlockTemperature(this.getBlockPos(), compound.getDouble("temperature"));
        } else {
            temp = compound.getDouble("temperature");
        }
    }
}
