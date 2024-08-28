package com.freezedown.metallurgica.foundation.temperature;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.util.ClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.GameType;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class TemperatureOverlay implements IGuiOverlay {
    public static final TemperatureOverlay INSTANCE = new TemperatureOverlay();
    
    @Override
    public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int width, int height) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || mc.gameMode.getPlayerMode() == GameType.SPECTATOR)
            return;
        
        LocalPlayer player = mc.player;
        if (player == null)
            return;
        if (!player.getPersistentData().contains("CurrentTemperature")) {
            //Metallurgica.LOGGER.debug("No temperature data found");
            return;
        }
        
        double temperature = player.getPersistentData()
                .getDouble("CurrentTemperature");
        
        poseStack.pushPose();
        poseStack.translate(width - 50, height - 50, 0);
        Component text = ClientUtil.temperature(temperature).component();
        int color = 0xFF_FFFFFF;
        if (temperature < 0)
            color = 0xFF_0000FF;
        mc.font.drawShadow(poseStack, text, 16, 5, color);
        
        poseStack.popPose();
    }
}
