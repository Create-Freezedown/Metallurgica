package com.freezedown.metallurgica.content.temperature;

import com.freezedown.metallurgica.Metallurgica;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.networking.BlockEntityDataPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TemperatureUpdatePacket extends BlockEntityDataPacket<SmartBlockEntity> {
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
        HandleClient.handle(this);
    }
    
    /** Safely runs client side only code in a method only called on client */
    private static class HandleClient {
        private static void handle(TemperatureUpdatePacket packet) {
            assert Minecraft.getInstance().level != null;
            BlockEntity te = Minecraft.getInstance().level.getBlockEntity(packet.pos);
            if (te instanceof ITemperature) {
                ((ITemperature) te).setTemperatureClient(packet.temperature);
            }
        }
    }
}
