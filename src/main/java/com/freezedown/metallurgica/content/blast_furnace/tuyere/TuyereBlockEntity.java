package com.freezedown.metallurgica.content.blast_furnace.tuyere;

import com.freezedown.metallurgica.registry.MetallurgicaFluids;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.List;

public class TuyereBlockEntity extends SmartBlockEntity {
    protected FluidTank tankInventory = this.createInventory();
    private boolean contentsChanged = true;
    public SmartFluidTankBehaviour tuyereTank;
    protected LazyOptional<IFluidHandler> fluidCapability;
    public TuyereBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.fluidCapability = LazyOptional.of(() -> {
            return this.tankInventory;
        });
    }
    protected SmartFluidTank createInventory() {
        return new SmartFluidTank(4000, this::onFluidStackChanged) {
            public boolean isFluidValid(FluidStack stack) {
                return stack.getFluid().isSame(MetallurgicaFluids.preheatedAir.get());
            }
            
            public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
                return FluidStack.EMPTY;
            }
        };
    }
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        this.tuyereTank = (new SmartFluidTankBehaviour(SmartFluidTankBehaviour.INPUT, this, 1, 4000, true)).whenFluidUpdates(() -> {
            this.contentsChanged = true;
        });
        behaviours.add(this.tuyereTank);
        this.fluidCapability = LazyOptional.of(() -> {
            LazyOptional<? extends IFluidHandler> inputCap = this.tuyereTank.getCapability();
            return new CombinedTankWrapper(inputCap.orElse(null));
        });
    }
    
    protected void onFluidStackChanged(FluidStack newFluidStack) {
        if (this.hasLevel()) {
            if (!this.level.isClientSide) {
                this.setChanged();
                this.sendData();
            }
        }
    }
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        this.tankInventory.readFromNBT(compound.getCompound("TankContent"));
    }
    
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("TankContent", this.tankInventory.writeToNBT(new CompoundTag()));
    }
}
