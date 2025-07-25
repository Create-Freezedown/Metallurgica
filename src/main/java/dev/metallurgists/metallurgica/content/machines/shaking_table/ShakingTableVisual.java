package dev.metallurgists.metallurgica.content.machines.shaking_table;

import com.simibubi.create.content.kinetics.base.ShaftVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;

public class ShakingTableVisual extends ShaftVisual<ShakingTableBlockEntity> {
    
    public ShakingTableVisual(VisualizationContext visualizationContext, ShakingTableBlockEntity blockEntity, float partialTicks) {
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

