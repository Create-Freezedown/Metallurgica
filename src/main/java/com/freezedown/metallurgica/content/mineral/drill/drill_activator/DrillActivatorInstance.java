package com.freezedown.metallurgica.content.mineral.drill.drill_activator;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;

public class DrillActivatorInstance extends SingleRotatingInstance<DrillActivatorBlockEntity> {
    public DrillActivatorInstance(MaterialManager materialManager, DrillActivatorBlockEntity blockEntity) {
        super(materialManager, blockEntity);
    }
    
    @Override
    protected Instancer<RotatingData> getModel() {
        return getRotatingMaterial().getModel(AllPartialModels.MILLSTONE_COG, blockEntity.getBlockState());
    }
}
