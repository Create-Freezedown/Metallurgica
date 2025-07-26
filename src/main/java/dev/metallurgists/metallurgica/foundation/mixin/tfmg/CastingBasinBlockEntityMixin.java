package dev.metallurgists.metallurgica.foundation.mixin.tfmg;

import com.drmangotea.tfmg.content.machinery.metallurgy.casting_basin.CastingBasinBlockEntity;
import dev.metallurgists.metallurgica.content.temperature.ITemperature;
import dev.metallurgists.metallurgica.foundation.fluid.MoltenMetalFluid;
import dev.metallurgists.metallurgica.foundation.temperature.server.TemperatureHandler;
import dev.metallurgists.metallurgica.foundation.util.ClientUtil;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(value = CastingBasinBlockEntity.class, remap = false)
public abstract class CastingBasinBlockEntityMixin extends SmartBlockEntity implements ITemperature {

    @Shadow public LazyOptional<IFluidHandler> fluidCapability;
    @Shadow public FluidTank tank;
    @Unique
    @SideOnly(Side.CLIENT)
    double temp;

    public CastingBasinBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "write(Lnet/minecraft/nbt/CompoundTag;Z)V", at = @At("TAIL"))
    private void metallurgica$write(CompoundTag compound, boolean clientPacket, CallbackInfo ci) {
        if(clientPacket) {
            compound.putDouble("temperature", temp);
        } else {
            compound.putDouble("temperature", getTemperature());
        }
    }

    @Inject(method = "read(Lnet/minecraft/nbt/CompoundTag;Z)V", at = @At("TAIL"))
    private void metallurgica$read(CompoundTag compound, boolean clientPacket, CallbackInfo ci) {
        if(this.level instanceof ServerLevel) {
            TemperatureHandler.getHandler((ServerLevel) this.level).setBlockTemperature(this.getBlockPos(), compound.getDouble("temperature"));
        } else {
            temp = compound.getDouble("temperature");
        }
    }

    @Inject(method = "addToGoggleTooltip(Ljava/util/List;Z)Z", at = @At("HEAD"))
    private void metallurgica$addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking, CallbackInfoReturnable<Boolean> cir) {
        ClientUtil.currentTemperatureTooltip(tooltip, getTemperature());
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void metallurgica$tick(CallbackInfo ci) {
        if (level == null || level.isClientSide)
            return;
        assert level instanceof ServerLevel;
        syncToClient(worldPosition, level);

        var containedFluid = this.tank.getFluid().getFluid();
        boolean temperatureChanged = false;
        if (!temperatureChanged && containedFluid instanceof MoltenMetalFluid molten) {
            setTemperature(molten.getMeltingPoint());
            temperatureChanged = true;
        }
    }

    @Override
    public Level getLevel() {
        return this.level;
    }

    @Override
    public BlockPos getPos() {
        return this.worldPosition;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void setTemp(double temp) {
        this.temp = temp;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public double getTemp() {
        return this.temp;
    }

    @Override
    public void sendStuff() {
        sendData();
        setChanged();
    }
}
