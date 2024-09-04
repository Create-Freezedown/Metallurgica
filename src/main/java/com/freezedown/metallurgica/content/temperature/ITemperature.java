package com.freezedown.metallurgica.content.temperature;

import com.freezedown.metallurgica.content.fluids.faucet.FaucetActivationPacket;
import com.freezedown.metallurgica.registry.MetallurgicaPackets;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.PacketDistributor;

public interface ITemperature {
    LevelAccessor getLevel();
    
    double getTemperature();
    
    void setTemperatureClient(double temperature);
    
    void sendStuff();
    
    default void syncToClient(BlockPos pos, Level level) {
        MetallurgicaPackets.sendToClientsNear(new TemperatureUpdatePacket(pos, getTemperature()), level, pos);
    }
}
