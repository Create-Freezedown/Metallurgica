package com.freezedown.metallurgica.content.primitive.ceramic.ceramic_mixing_pot;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Dynamic;

public class CeramicMixingPotInstance extends BlockEntityInstance<CeramicMixingPotBlockEntity> implements DynamicInstance {
    private ModelData stirrer;
    
    public CeramicMixingPotInstance(MaterialManager materialManager, CeramicMixingPotBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        Instancer<ModelData> model = blockEntity.getRenderedStirrerInstance(getTransformMaterial());
        stirrer = model.createInstance();
        rotateStirrer();
    }
    
    @Override
    public void beginFrame() {
        if (stirrer == null)
            return;
        
        rotateStirrer();
    }
    
    private void rotateStirrer() {
        float angle = blockEntity.getIndependentAngle(AnimationTickHolder.getPartialTicks());
        
        stirrer.loadIdentity()
                .translate(getInstancePosition())
                .centre()
                .rotate(Direction.get(Direction.AxisDirection.POSITIVE, Direction.Axis.Y), angle)
                .unCentre();
    }
    
    @Override
    protected void remove() {
        if (stirrer != null)
            stirrer.delete();
    }
    
    @Override
    public void updateLight() {
        if (stirrer != null)
            relight(pos, stirrer);
    }
}
