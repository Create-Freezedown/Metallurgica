package com.freezedown.metallurgica.content.metalworking.forging.hammer;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.GameData;

public class RadialHammerMenuSubmitPacket extends SimplePacketBase {

    private final ItemStack hammer;
    private final RadialHammerMenu.HammerMode newMode;

    public RadialHammerMenuSubmitPacket(ItemStack hammer, RadialHammerMenu.HammerMode newMode) {
        this.hammer = hammer;
        this.newMode = newMode;
    }

    public RadialHammerMenuSubmitPacket(FriendlyByteBuf buffer) {
        this.hammer = buffer.readItem();
        this.newMode = buffer.readEnum(RadialHammerMenu.HammerMode.class);
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeItem(hammer);
        buffer.writeEnum(newMode);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            Level level = player.level();
            CompoundTag hammerData = hammer.getOrCreateTag();
            RadialHammerMenu.HammerMode currentMode = RadialHammerMenu.HammerMode.valueOf(hammerData.getString("HammerMode").toUpperCase());

            if (currentMode == newMode)
                return;

            hammerData.putString("HammerMode", currentMode.getSerializedName());
        });
        return true;
    }
}
