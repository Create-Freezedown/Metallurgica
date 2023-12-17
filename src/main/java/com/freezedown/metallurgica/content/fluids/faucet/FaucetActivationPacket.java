package com.freezedown.metallurgica.content.fluids.faucet;

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
    
    @Override
    protected void handlePacket(FaucetBlockEntity blockEntity) {
        if (Minecraft.getInstance().level == null) {
            return;
        }
        BlockEntity te = Minecraft.getInstance().level.getBlockEntity(this.pos);
        if (te instanceof FaucetBlockEntity) {
            ((FaucetBlockEntity) te).onActivationPacket(this.fluid, this.isPouring);
        }
    }
}
