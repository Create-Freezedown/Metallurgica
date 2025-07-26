package dev.metallurgists.metallurgica.content.fluids.faucet;

import com.simibubi.create.foundation.networking.BlockEntityDataPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;

public class FaucetActivationPacket extends BlockEntityDataPacket<FaucetBlockEntity> {
    private final boolean isPouring;
    protected final BlockPos pos;
    protected final FluidStack fluid;
    public FaucetActivationPacket(BlockPos pos, FluidStack fluid, boolean isPouring) {
        super(pos);
        this.isPouring = isPouring;
        this.pos = pos;
        this.fluid = fluid;
    }
    
    public FaucetActivationPacket(FriendlyByteBuf buffer) {
        super(buffer);
        this.isPouring = buffer.readBoolean();
        this.pos = buffer.readBlockPos();
        this.fluid = buffer.readFluidStack();
    }
    @Override
    protected void writeData(FriendlyByteBuf buffer) {
        buffer.writeBoolean(isPouring);
        buffer.writeBlockPos(pos);
        buffer.writeFluidStack(fluid);
    }
    /** Safely runs client side only code in a method only called on client */
    private static class HandleClient {
        private static void handle(FaucetActivationPacket packet) {
            assert Minecraft.getInstance().level != null;
            BlockEntity te = Minecraft.getInstance().level.getBlockEntity(packet.pos);
            if (te instanceof FaucetBlockEntity) {
                ((FaucetBlockEntity) te).onActivationPacket(packet.fluid, packet.isPouring);
            }
        }
    }
    @Override
    protected void handlePacket(FaucetBlockEntity blockEntity) {
        HandleClient.handle(this);
    }
}
