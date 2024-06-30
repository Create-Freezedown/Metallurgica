package com.freezedown.metallurgica.content.machines.reverbaratory;

import com.drmangotea.createindustry.registry.TFMGFluids;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public class ReverbaratoryCarbonOutputBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {
    public SmartFluidTankBehaviour tank;
    private boolean contentsChanged = true;
    protected LazyOptional<IFluidHandler> fluidCapability;
    
    public ReverbaratoryCarbonOutputBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.tank.getPrimaryHandler().setCapacity(8000);
    }
    
    protected SmartFluidTank createInventory() {
        return new SmartFluidTank(1000, this::onFluidStackChanged) {
            public boolean isFluidValid(FluidStack stack) {
                return stack.getFluid() == TFMGFluids.CARBON_DIOXIDE.get();
            }
        };
    }
    
    protected void onFluidStackChanged(FluidStack newFluidStack) {
        this.sendData();
    }
    
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        this.tank = (new SmartFluidTankBehaviour(SmartFluidTankBehaviour.OUTPUT, this, 1, 1000, true)).whenFluidUpdates(() -> {
            this.contentsChanged = true;
        });
        behaviours.add(this.tank);
        this.fluidCapability = LazyOptional.of(() -> {
            LazyOptional<? extends IFluidHandler> inputCap = this.tank.getCapability();
            return new CombinedTankWrapper(inputCap.orElse(null));
        });
    }
    
    public void invalidate() {
        super.invalidate();
        this.fluidCapability.invalidate();
    }
    
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        return cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ? this.fluidCapability.cast() : super.getCapability(cap, side);
    }
    
    public void notifyUpdate() {
        super.notifyUpdate();
    }
    
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        LazyOptional<IFluidHandler> handler = this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
        Optional<IFluidHandler> resolve = handler.resolve();
        if (!resolve.isPresent()) {
            return false;
        } else {
            IFluidHandler tank = (IFluidHandler)resolve.get();
            if (tank.getTanks() == 0) {
                return false;
            } else {
                LangBuilder mb = Lang.translate("generic.unit.millibuckets", new Object[0]);
                boolean isEmpty = true;
                
                for(int i = 0; i < tank.getTanks(); ++i) {
                    FluidStack fluidStack = tank.getFluidInTank(i);
                    if (!fluidStack.isEmpty()) {
                        Lang.fluidName(fluidStack).style(ChatFormatting.GRAY).forGoggles(tooltip, 1);
                        Lang.builder().add(Lang.number((double)fluidStack.getAmount()).add(mb).style(ChatFormatting.DARK_GREEN)).text(ChatFormatting.GRAY, " / ").add(Lang.number((double)tank.getTankCapacity(i)).add(mb).style(ChatFormatting.DARK_GRAY)).forGoggles(tooltip, 1);
                        isEmpty = false;
                    }
                }
                
                if (tank.getTanks() > 1) {
                    if (isEmpty) {
                        tooltip.remove(tooltip.size() - 1);
                    }
                    
                    return true;
                } else if (!isEmpty) {
                    return true;
                } else {
                    Lang.translate("gui.goggles.fluid_container.capacity", new Object[0]).add(Lang.number((double)tank.getTankCapacity(0)).add(mb).style(ChatFormatting.DARK_GREEN)).style(ChatFormatting.DARK_GRAY).forGoggles(tooltip, 1);
                    return true;
                }
            }
        }
    }
}
