package com.freezedown.metallurgica.foundation.util;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.config.MetallurgicaConfigs;
import com.freezedown.metallurgica.foundation.material.MaterialHelper;
import com.freezedown.metallurgica.foundation.temperature.TemperatureUpdatePacket;
import com.freezedown.metallurgica.foundation.temperature.server.TemperatureHandler;
import com.freezedown.metallurgica.registry.material.MetMaterials;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;

import java.util.List;

public final class DebugHandler {

    private DebugHandler() {}

    private static final String PREFIX = ChatFormatting.YELLOW + "[Metallurgica] " + ChatFormatting.RESET;

    public static void onDrawDebugText(List<String> left) {
        Minecraft mc = Minecraft.getInstance();
        Level world = mc.level;
        if (mc.options.renderDebug && MetallurgicaConfigs.client().debugInfo.get()) {
            left.add("");
            String version = ModList.get().getModContainerById(Metallurgica.ID).get().getModInfo().getVersion().toString();
            int materialCount = MetMaterials.registeredMaterials.size();
            left.add(PREFIX + "Version: " + version + ", Materials: " + materialCount);
        }
    }
}
