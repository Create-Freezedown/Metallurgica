package dev.metallurgists.metallurgica.content.temperature;

import dev.metallurgists.metallurgica.foundation.block_entity.IntelligentKineticBlockEntity;
import dev.metallurgists.metallurgica.foundation.temperature.server.TemperatureHandler;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class KineticTemperatureBlockEntity extends IntelligentKineticBlockEntity implements ITemperature {
    ///ONLY USE ON CLIENT-SIDE
    @SideOnly(Side.CLIENT)
    private double temp;

    @Override
    public BlockPos getPos() {
        return getBlockPos();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void setTemp(double t) {
        temp = t;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public double getTemp() {
        return temp;
    }
    
    public KineticTemperatureBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }
    
    @Override
    public void sendStuff() {
        sendData();
        setChanged();
    }
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}
    
    public void decreaseTemperature(double amount) {
        setTemperature(getTemperature() - amount);
    }
    
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
