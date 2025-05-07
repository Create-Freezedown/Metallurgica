package com.freezedown.metallurgica.foundation.temperature;

import com.freezedown.metallurgica.content.temperature.ITemperature;
import com.freezedown.metallurgica.foundation.temperature.server.TemperatureHandler;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.networking.BlockEntityDataPacket;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TemperatureUpdatePacket extends BlockEntityDataPacket<SmartBlockEntity> {
    @SideOnly(Side.CLIENT)
    private double temperature;
    
    public TemperatureUpdatePacket(BlockPos pos, double temperature) {
        super(pos);
        this.temperature = temperature;
    }
    
    public TemperatureUpdatePacket(FriendlyByteBuf buffer) {
        super(buffer);
        this.temperature = buffer.readDouble();
    }
    
    @Override
    protected void writeData(FriendlyByteBuf buffer) {
        buffer.writeDouble(temperature);
    }
    
    @Override
    protected void handlePacket(SmartBlockEntity blockEntity) {
        Level level = blockEntity.getLevel();
        if(level instanceof ServerLevel) {
            handleServer(this, (ServerLevel) level);
        } else {
            handleClient(this, (ClientLevel) level);
        }
    }
    
    /** Safely runs client side only code in a method only called on client */
    @SideOnly(Side.CLIENT)
    private static void handleClient(TemperatureUpdatePacket packet, ClientLevel level) {
        BlockEntity te = level.getBlockEntity(packet.pos);
        if (te instanceof ITemperature) {
            ((ITemperature) te).setTemperature(packet.temperature);
        }
    }

    /** Safely runs server side only code in a method only called on server */
    @SideOnly(Side.SERVER)
    private static void handleServer(TemperatureUpdatePacket packet, ServerLevel level) {
        BlockEntity te = level.getBlockEntity(packet.pos);
        TemperatureHandler.getHandler(level).setBlockTemperature(packet.pos, packet.temperature);
    }
}
