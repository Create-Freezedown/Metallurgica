package com.freezedown.metallurgica.content.machines.rotary_kiln.segment;

import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;

public class RotaryKilnSegmentInstance extends KineticBlockEntityInstance<RotaryKilnSegmentBlockEntity> {
    
    protected final RotatingData shaft;
    protected final RotatingData wheel;
    
    
    public RotaryKilnSegmentInstance(MaterialManager materialManager, RotaryKilnSegmentBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        
        shaft = setup(getRotatingMaterial().getModel(shaft())
                .createInstance());
        wheel = setup(getRotatingMaterial().getModel(blockState)
                .createInstance());
    }
    
    @Override
    public void update() {
        updateRotation(shaft);
        updateRotation(wheel);
    }
    
    @Override
    public void updateLight() {
        relight(pos, shaft, wheel);
    }
    
    @Override
    public void remove() {
        shaft.delete();
        wheel.delete();
    }
}
