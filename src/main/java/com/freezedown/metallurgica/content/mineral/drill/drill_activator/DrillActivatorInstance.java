package com.freezedown.metallurgica.content.mineral.drill.drill_activator;


import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.simibubi.create.content.kinetics.millstone.MillstoneRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;

public class DrillActivatorInstance extends SingleAxisRotatingVisual<DrillActivatorBlockEntity> {
    public DrillActivatorInstance(VisualizationContext context, DrillActivatorBlockEntity blockEntity) {
        super(materialManager, blockEntity);
    }
    
    @Override
    protected Instancer<RotatingData> getModel() {
        return getRotatingMaterial().getModel(AllPartialModels.MILLSTONE_COG, blockEntity.getBlockState());
    }
}
