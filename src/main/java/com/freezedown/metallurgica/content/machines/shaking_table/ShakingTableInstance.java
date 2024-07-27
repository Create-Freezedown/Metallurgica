package com.freezedown.metallurgica.content.machines.shaking_table;

import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.content.kinetics.base.ShaftInstance;

public class ShakingTableInstance extends ShaftInstance<ShakingTableBlockEntity> {
    
    public ShakingTableInstance(MaterialManager materialManager, ShakingTableBlockEntity blockEntity) {
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

