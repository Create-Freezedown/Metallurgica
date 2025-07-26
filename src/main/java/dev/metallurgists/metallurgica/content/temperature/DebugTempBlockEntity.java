package dev.metallurgists.metallurgica.content.temperature;

import dev.metallurgists.metallurgica.foundation.util.ClientUtil;
import dev.metallurgists.metallurgica.foundation.temperature.server.TemperatureHandler;
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
    double temp;

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
    }
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

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

    @Override
    public void sendStuff() {
        sendData();
        setChanged();
    }
    
    
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        if(this.level instanceof ServerLevel) {
            TemperatureHandler.getHandler((ServerLevel) this.level).setBlockTemperature(this.getBlockPos(), compound.getDouble("temperature"));
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
