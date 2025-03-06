package com.freezedown.metallurgica.content.machines.electolizer;

import com.simibubi.create.content.kinetics.base.ShaftVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;

public class ElectrolyzerVisual extends ShaftVisual<ElectrolyzerBlockEntity> {
    
    public ElectrolyzerVisual(VisualizationContext visualizationContext, ElectrolyzerBlockEntity blockEntity, float partialTick) {
        super(visualizationContext, blockEntity, partialTick);
    }
    
    @Override
    public void updateLight(float partialTick) {
        super.updateLight(partialTick);
    }
    
    @Override
    public void _delete() {
        super._delete();
    }
}
