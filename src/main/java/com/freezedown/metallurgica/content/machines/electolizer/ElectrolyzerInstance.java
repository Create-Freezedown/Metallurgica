package com.freezedown.metallurgica.content.machines.electolizer;

import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.content.kinetics.base.ShaftInstance;

public class ElectrolyzerInstance extends ShaftInstance<ElectrolyzerBlockEntity> {
    
    public ElectrolyzerInstance(MaterialManager materialManager, ElectrolyzerBlockEntity blockEntity) {
        super(materialManager, blockEntity);
    }
    
    @Override
    public void updateLight() {
        super.updateLight();
    }
    
    @Override
    public void remove() {
        super.remove();
    }
}
