package com.freezedown.metallurgica.content.metalworking.casting;

import com.freezedown.metallurgica.content.metalworking.casting.ingot.IngotCastingMoldBlockEntity;
import com.simibubi.create.AllTags;
import com.simibubi.create.api.behaviour.spouting.BlockSpoutingBehaviour;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public enum SpoutCastingBehaviour implements BlockSpoutingBehaviour {
    INSTANCE;

    IngotCastingMoldBlockEntity castingMoldBE;
    @Override
    public int fillBlock(Level level, BlockPos pos, SpoutBlockEntity spout, FluidStack availableFluid, boolean simulate) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        

        if (blockEntity instanceof IngotCastingMoldBlockEntity mold) {
            castingMoldBE = mold;
        } else {
            return 0;
        }
        
        IFluidHandler handler = castingMoldBE.getCapability(ForgeCapabilities.FLUID_HANDLER, Direction.UP)
                .orElse(null);
        if (handler == null)
            return 0;
        if (handler.getTanks() != 1)
            return 0;
        
        if (!handler.isFluidValid(0, availableFluid))
            return 0;
        
        if (!availableFluid.getFluid().is(AllTags.AllFluidTags.FAN_PROCESSING_CATALYSTS_SPLASHING.tag))
            return 0;
        
        int amount = availableFluid.getAmount();
        FluidStack containedFluid = handler.getFluidInTank(0);
        if (!containedFluid.isEmpty() && amount > 50) {
            double temperature = castingMoldBE.getTemperature();
            if (temperature <= 20)
                return 0;
            if (temperature > 120) {
                castingMoldBE.decreaseTemperature(100);
            } else {
                castingMoldBE.setTemperature(20);
            }
            return 50;
            
        }
        return 0;
    }
}
