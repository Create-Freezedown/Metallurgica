package com.freezedown.metallurgica.content.temperature;

import com.freezedown.metallurgica.foundation.temperature.TemperatureUpdatePacket;
import com.freezedown.metallurgica.registry.MetallurgicaPackets;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public interface ITemperature {
    LevelAccessor getLevel();
    
    double getTemperature();

    void setTemperature(double temperature);
    
    void sendStuff();
    
    default void syncToClient(BlockPos pos, Level level) {
        MetallurgicaPackets.sendToClientsNear(new TemperatureUpdatePacket(pos, getTemperature()), level, pos);
    }
}
