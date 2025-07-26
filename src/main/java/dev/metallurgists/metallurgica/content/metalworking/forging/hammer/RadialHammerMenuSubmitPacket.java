package dev.metallurgists.metallurgica.content.metalworking.forging.hammer;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class RadialHammerMenuSubmitPacket extends SimplePacketBase {

    private final ItemStack stack;
    private final RadialHammerMenu.HammerMode newMode;

    public RadialHammerMenuSubmitPacket(ItemStack hammer, RadialHammerMenu.HammerMode newMode) {
        this.stack = hammer;
        this.newMode = newMode;
    }

    public RadialHammerMenuSubmitPacket(FriendlyByteBuf buffer) {
        this.stack = buffer.readItem();
        this.newMode = buffer.readEnum(RadialHammerMenu.HammerMode.class);
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeItem(stack);
        buffer.writeEnum(newMode);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null)
                return;
            CompoundTag tag = stack.getOrCreateTag();
            tag.putString("HammerMode", newMode.name());
            stack.setTag(tag);
        });
        return true;
    }
}
