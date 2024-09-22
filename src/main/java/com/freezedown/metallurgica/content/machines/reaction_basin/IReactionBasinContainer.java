package com.freezedown.metallurgica.content.machines.reaction_basin;

import com.simibubi.create.foundation.blockEntity.IMultiBlockEntityContainer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

public interface IReactionBasinContainer extends IMultiBlockEntityContainer {
    default boolean hasTank() { return false; }
    
    default int getTankSize(int tank) {	return 0; }
    
    default void setTankSize(int tank, int blocks) {}
    
    default IFluidTank getTank(int tank) { return null; }
    
    default FluidStack getFluid(int tank) {	return FluidStack.EMPTY; }
    
    default boolean hasInventory() { return false; }
}
