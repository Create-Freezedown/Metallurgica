package com.freezedown.metallurgica.content.items.temperature;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.temperature.server.TemperatureHandler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class ThermometerItem extends Item {
    public ThermometerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        if(level.isClientSide()) {
            ClientLevel clientLevel = (ClientLevel) level;
            //TODO: something
            return InteractionResult.PASS;
        } else {
            ServerLevel serverLevel = (ServerLevel) level;
            TemperatureHandler handler = TemperatureHandler.getHandler(serverLevel);
            Metallurgica.LOGGER.info("TEMPERATURE SYSTEM: measured " + handler.getBlockTemperature(ctx.getClickedPos()));
            return InteractionResult.SUCCESS;
        }
    }
}
