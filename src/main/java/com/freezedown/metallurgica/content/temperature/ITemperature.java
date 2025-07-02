package com.freezedown.metallurgica.content.temperature;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.temperature.TemperatureUpdatePacket;
import com.freezedown.metallurgica.foundation.temperature.server.TemperatureHandler;
import com.freezedown.metallurgica.registry.MetallurgicaPackets;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ITemperature {
    LevelAccessor getLevel();
    BlockPos getPos();

    ///ONLY USE ON CLIENT-SIDE
    @SideOnly(Side.CLIENT)
    void setTemp(double temp);

    ///ONLY USE ON CLIENT-SIDE
    @SideOnly(Side.CLIENT)
    double getTemp();
    
    default double getTemperature() {
        Level l = (Level) getLevel();
        if(l != null) {
            if (l instanceof ServerLevel) {
                return TemperatureHandler.getHandler((ServerLevel) l).getBlockTemperature(getPos());
            } else {
                return getTemp();
            }
        } else {
            Metallurgica.LOGGER.error("TEMPERATURE SYSTEM: level is null");
            return 0.0;
        }
    }

    default void setTemperature(double temperature) {
        Level l = (Level) getLevel();
        if(l != null) {
            if (l instanceof ServerLevel) {
                TemperatureHandler.getHandler((ServerLevel) l).setBlockTemperature(getPos(), temperature);
            } else {
                setTemp(temperature);
            }
        } else {
            Metallurgica.LOGGER.error("TEMPERATURE SYSTEM: level is null");
        }
    }
    
    void sendStuff();
    
    default void syncToClient(BlockPos pos, Level level) {
        MetallurgicaPackets.sendToClientsNear(new TemperatureUpdatePacket(pos, getTemperature()), level, pos);
    }
}
