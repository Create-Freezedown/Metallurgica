package com.freezedown.metallurgica.content.machines.shaking_table;

import com.simibubi.create.content.kinetics.base.ShaftVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;

public class ShakingTableInstance extends ShaftVisual<ShakingTableBlockEntity> {
    
    public ShakingTableInstance(VisualizationContext visualizationContext, ShakingTableBlockEntity blockEntity, float partialTicks) {
        super(visualizationContext, blockEntity, partialTicks);
    }
    
    @Override
    public void updateLight(float pt) {
        super.updateLight(pt);
    }
    
    @Override
    public void _delete() {
        super._delete();
    }
}

