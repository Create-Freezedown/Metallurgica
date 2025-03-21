package com.freezedown.metallurgica.foundation.data.custom.composition.fluid;

import com.freezedown.metallurgica.foundation.data.custom.composition.data.Element;
import com.freezedown.metallurgica.foundation.data.custom.composition.data.SubComposition;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientFluidCompositions {
    private static ClientFluidCompositions INSTANCE;
    private static final Map<FluidStack, FluidComposition> compositions = new HashMap<>();
    
    public static ClientFluidCompositions getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClientFluidCompositions();
        }
        return INSTANCE;
    }
    
    public void setCompositions(Map<FluidStack, FluidComposition> compositions) {
        ClientFluidCompositions.compositions.clear();
        ClientFluidCompositions.compositions.putAll(compositions);
    }
    
    public Map<FluidStack, FluidComposition> getCompositions() {
        return compositions;
    }
    
    public boolean hasComposition(FluidStack fluidStack) {
        boolean hasComposition = false;
        for (FluidStack stack : compositions.keySet()) {
            if (fluidStack.getFluid() == stack.getFluid()) {
                if (stack.hasTag()) {
                    hasComposition = stack.getTag().equals(fluidStack.getTag());
                } else {
                    hasComposition = true;
                }
                break;
            }
        }
        return hasComposition;
    }
    
    public FluidComposition getComposition(FluidStack fluidStack) {
        return compositions.get(fluidStack);
    }
    
    public List<SubComposition> getSubCompositions(FluidStack fluidStack) {
        return compositions.get(fluidStack).subCompositions();
    }
}
