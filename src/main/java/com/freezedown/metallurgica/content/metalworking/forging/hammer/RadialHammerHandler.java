package com.freezedown.metallurgica.content.metalworking.forging.hammer;

import com.simibubi.create.AllKeys;
import net.createmod.catnip.gui.ScreenOpener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class RadialHammerHandler {
    public static int COOLDOWN = 0;

    public static void clientTick() {
        if (COOLDOWN > 0 && !AllKeys.ROTATE_MENU.isPressed())
            COOLDOWN--;
    }

    public static void onKeyInput(int key, boolean pressed) {
        if (!pressed)
            return;

        if (key != AllKeys.ROTATE_MENU.getBoundCode())
            return;

        if (COOLDOWN > 0)
            return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.gameMode == null || mc.gameMode.getPlayerMode() == GameType.SPECTATOR)
            return;

        LocalPlayer player = mc.player;
        if (player == null)
            return;

        Level level = player.level();

        ItemStack heldItem = player.getMainHandItem();
        if (!(heldItem.getItem() instanceof ForgeHammerItem))
            return;

        HitResult objectMouseOver = mc.hitResult;

        CompoundTag hammerData = heldItem.getOrCreateTag();
        RadialHammerMenu.HammerMode currentMode = RadialHammerMenu.HammerMode.valueOf(hammerData.getString("HammerMode").toUpperCase());

        RadialHammerMenu.tryCreateFor(heldItem, currentMode, level).ifPresent(ScreenOpener::open);
    }
}
