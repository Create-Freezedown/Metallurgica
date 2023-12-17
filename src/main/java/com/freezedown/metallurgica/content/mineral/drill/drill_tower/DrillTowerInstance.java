package com.freezedown.metallurgica.content.mineral.drill.drill_tower;

import com.freezedown.metallurgica.registry.MetallurgicaPartialModels;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import net.minecraft.core.Direction;

public class DrillTowerInstance extends KineticBlockEntityInstance<DrillTowerBlockEntity> implements DynamicInstance {
    private final RotatingData drillBit;
    private final DrillTowerBlockEntity drillTower;
    public DrillTowerInstance(MaterialManager materialManager, DrillTowerBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        drillTower = blockEntity;
        
        drillBit = materialManager.defaultCutout()
                .material(AllMaterialSpecs.ROTATING)
                .getModel(MetallurgicaPartialModels.drillBitCastIron, blockState)
                .createInstance();
        drillBit.setRotationAxis(Direction.Axis.Y);
    }
    @Override
    public void beginFrame() {
        transformBit();
    }
    private void transformBit() {
        float speed = drillTower.efficiency;
        drillBit.setPosition(getInstancePosition()).setRotationalSpeed((speed / 4) * drillTower.efficiency);
    }
    @Override
    public void updateLight() {
        super.updateLight();
        relight(pos, drillBit);
    }
    
    @Override
    public void remove() {
        drillBit.delete();
    }
}
