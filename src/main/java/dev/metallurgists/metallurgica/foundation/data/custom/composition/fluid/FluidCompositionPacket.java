package dev.metallurgists.metallurgica.foundation.data.custom.composition.fluid;

import dev.metallurgists.metallurgica.Metallurgica;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;

public class FluidCompositionPacket extends SimplePacketBase {
    private Map<FluidStack, FluidComposition> compositions = new HashMap<>();
    
    public FluidCompositionPacket(Map<FluidStack, FluidComposition> compositions) {
        this.compositions = compositions;
    }
    
    public FluidCompositionPacket(FriendlyByteBuf buffer) {
        int size = buffer.readVarInt();
        FluidStack fluidStack = buffer.readFluidStack();
        for (int i = 0; i < size; i++) {
            FluidComposition fluidComposition = FluidComposition.fromNetwork(buffer);
            compositions.put(fluidStack, fluidComposition);
        }
    }
    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeVarInt(compositions.size());
        compositions.forEach((fluidStack, fluidComposition) -> {
            buffer.writeFluidStack(fluidStack);
            fluidComposition.writeToPacket(buffer);
        });
    }
    
    @Override
    public boolean handle(NetworkEvent.Context context) {
        if (context.getDirection() != NetworkDirection.PLAY_TO_CLIENT)
            return false;
        Metallurgica.LOGGER.info("Received fluid compositions packet");
        context.enqueueWork(() -> ClientFluidCompositions.getInstance().setCompositions(compositions));
        return true;
    }
}
