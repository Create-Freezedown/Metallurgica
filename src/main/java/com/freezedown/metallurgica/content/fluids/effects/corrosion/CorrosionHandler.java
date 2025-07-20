package com.freezedown.metallurgica.content.fluids.effects.corrosion;

import com.freezedown.metallurgica.foundation.item.lining.tank_lining.TankLiningBehaviour;
import com.freezedown.metallurgica.foundation.item.lining.tank_lining.TankLiningStats;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import lombok.AccessLevel;
import lombok.Getter;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;

import java.util.List;

public class CorrosionHandler {

    @Getter(AccessLevel.PRIVATE)
    private TankLiningBehaviour liningBehaviour;

    @Getter
    private final CorrosionHandler instance;

    private CorrosionHandler(TankLiningBehaviour liningBehaviour) {
        this.liningBehaviour = liningBehaviour;
        this.instance = this;
    }

    public static CorrosionHandler create(TankLiningBehaviour liningBehaviour) {
        return new CorrosionHandler(liningBehaviour);
    }

    public void tick() {
        Table<Integer, FluidStack, Corrosive> acidsTable = testAcids();
        TankLiningStats liningStats = getLiningBehaviour().getLiningStats();

        if (getLiningBehaviour().getWorld().random.nextFloat() < 0.02f && !acidsTable.isEmpty()) {
            damageLining(liningStats, acidsTable.values().stream().toList());
        }
    }

    private float acidityMultiplier(List<Corrosive> corrosives) {
        float sum = 0;
        for (int i = 0; i < corrosives.size(); i++) {
            sum += corrosives.get(i).getAcidity();
        }
        float result = sum / corrosives.size();
        return result < 1 ? 1.0f : result;
    }

    private void damageLining(TankLiningStats liningStats, List<Corrosive> corrosives) {
        int toDamage = 4;
        if (liningStats != null && liningStats.getCorrosionResistance() != null) {
            toDamage = 2;
        }
        //toDamage *= (int) acidityMultiplier(corrosives);
        if (liningStats != null) liningStats.hurtAndBreak(toDamage);
    }

    SmartBlockEntity getTankBE() {
        return getLiningBehaviour().blockEntity;
    }

    Table<Integer, FluidStack, Corrosive> testAcids() {
        ImmutableTable.Builder<Integer, FluidStack, Corrosive> corrosiveFluids = new ImmutableTable.Builder<>();
        if (getTankBE().getCapability(ForgeCapabilities.FLUID_HANDLER).isPresent()) {
            LazyOptional<? extends IFluidHandler> fluidCapability = getTankBE().getCapability(ForgeCapabilities.FLUID_HANDLER);
            var fluidHandler = fluidCapability.orElse(null);
            if (fluidHandler != null || fluidHandler instanceof EmptyHandler) {
                for (int tank = 0; tank <= fluidHandler.getTanks() - 1; tank++) {
                    FluidStack fluidStack = fluidHandler.getFluidInTank(tank);
                    if (fluidStack.getFluid() instanceof Corrosive corrosive) {
                        corrosiveFluids.put(tank, fluidStack, corrosive);
                    }
                }
            }
        }
        return corrosiveFluids.build();
    }
}
